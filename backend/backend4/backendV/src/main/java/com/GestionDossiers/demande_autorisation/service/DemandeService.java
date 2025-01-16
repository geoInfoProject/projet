package com.GestionDossiers.demande_autorisation.service;
import com.GestionDossiers.demande_autorisation.entities.Status;
import com.GestionDossiers.demande_autorisation.dto.DemandeDTO;
import com.GestionDossiers.demande_autorisation.entities.Client;
import com.GestionDossiers.demande_autorisation.entities.Commun;
import com.GestionDossiers.demande_autorisation.entities.Demande;
import com.GestionDossiers.demande_autorisation.entities.Document;
import com.GestionDossiers.demande_autorisation.repository.CommunRepository;
import com.GestionDossiers.demande_autorisation.repository.DemandeRepository;
import com.GestionDossiers.demande_autorisation.repository.StatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private CommunRepository communRepository;

    @Autowired
    private StatusRepository statusRepository;  // Inject StatusRepository here

    /**
     * Crée une nouvelle demande.
     *
     * @param demande la demande à créer.
     * @return la demande créée.
     */
    public Demande createDemande(Demande demande) {
        if (demande == null) {
            throw new IllegalArgumentException("La demande ne peut pas être nulle.");
        }

        // Définir la date actuelle pour la demande
        demande.setDateDemande(new Date());

        // Trouver la commune correspondant à la localisation donnée
        Commun commune = communRepository.findByLocation(demande.getLocalisation());

        // Définir le status en fonction de la commune trouvée ou non
        Status status;
        if (commune == null) {
            // Si aucune commune trouvée, définir le status à 2
            status = statusRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Le status avec ID 2 n'existe pas."));
        } else {
            // Si une commune est trouvée, définir la commune et le status à 1 par défaut
            demande.setCommune(commune);
            status = statusRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Le status avec ID 1 n'existe pas."));
        }
        demande.setStatus(status);

        // Vérifier que la demande contient exactement 3 documents
        if (demande.getDocuments() == null || demande.getDocuments().size() != 3) {
            throw new RuntimeException("La demande doit contenir exactement 3 documents.");
        }

        // Associer chaque document à la demande
        demande.getDocuments().forEach(doc -> doc.setDemande(demande));

        // Sauvegarder et retourner la demande
        return demandeRepository.save(demande);
    }



    public Demande getDemandeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de la demande ne peut pas être nul.");
        }
        return demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec l'ID : " + id));
    }

    public Demande updateDemande(Demande demande) {
        if (demande == null || demande.getId() == null) {
            throw new IllegalArgumentException("La demande et son ID ne peuvent pas être nuls.");
        }

        if (!demandeRepository.existsById(demande.getId())) {
            throw new RuntimeException("Impossible de mettre à jour : Demande non trouvée avec l'ID : " + demande.getId());
        }

        // Ensure that documents are linked to the demande before saving
        if (demande.getDocuments() != null) {
            demande.getDocuments().forEach(doc -> doc.setDemande(demande));
        }

        return demandeRepository.save(demande);
    }

    public List<DemandeDTO> getAllDemandes() {
        return demandeRepository.findAllDemandeDTOs();
    }

    public List<Demande> rechercherDemande(String statut, String type, String commune) {
        // Check for null and avoid unnecessary queries
        if (statut == null && type == null && commune == null) {
            throw new IllegalArgumentException("Au moins un critère de recherche doit être spécifié.");
        }
        return demandeRepository.searchDemande(statut, type, commune);
    }

    public List<Demande> rechercherDemandeParClientObligatoire(Long clientId, String statut, String type, String commune) {
        if (clientId == null) {
            throw new IllegalArgumentException("L'ID du client est obligatoire pour cette recherche.");
        }
        return demandeRepository.rechercherParClient(clientId, statut, type, commune);
    }

    public List<Demande> getDemandesByCin(String cin) {
        if (cin == null || cin.isEmpty()) {
            throw new IllegalArgumentException("Le CIN ne peut pas être nul ou vide.");
        }

        return demandeRepository.findAllByClient_Cin(cin);
    }

    @Transactional
public Demande modifierStatut(Long demandeId, String statut) {
    // Récupérer la demande en fonction de l'ID
    Demande demande = demandeRepository.findById(demandeId)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

    // Récupérer le statut en fonction de son nom
    Status status = statusRepository.findByStatut(statut)
            .orElseThrow(() -> new RuntimeException("Statut non trouvé"));

    // Associer le nouveau statut à la demande
    demande.setStatus(status);

    // Sauvegarder et retourner la demande mise à jour
    return demandeRepository.save(demande);
}  
    /**
     * Supprime une demande par ID.
     *
     * @param id l'ID de la demande à supprimer.
     */
    @Autowired
    private FileUploadService fileUploadService;

    public void deleteDemande(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de la demande ne peut pas être nul.");
        }

        if (!demandeRepository.existsById(id)) {
            throw new RuntimeException("Demande non trouvée avec l'ID : " + id);
        }

        // Supprime la demande
        demandeRepository.deleteById(id);

        // Supprime le répertoire correspondant aux documents
        fileUploadService.deleteDirectory(String.valueOf(id));
    }
    /**
     * Récupère les informations de la commune associée à une demande via son ID.
     *
     * @param demandeId l'ID de la demande.
     * @return les informations de la commune correspondante.
     */
    public Commun getCommuneByDemandeId(Long demandeId) {
        // Vérifier que l'ID de la demande n'est pas nul
        if (demandeId == null) {
            throw new IllegalArgumentException("L'ID de la demande ne peut pas être nul.");
        }

        // Récupérer la demande avec son ID
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec l'ID : " + demandeId));

        // Retourner la commune associée
        return demande.getCommune();
        
    }
    public Client getClientByDemandeId(Long id) {
        return demandeRepository.findClientByDemandeId(id)
                .orElseThrow(() -> new RuntimeException("Aucun client trouvé pour la demande avec ID : " + id));
    }
   

   
}