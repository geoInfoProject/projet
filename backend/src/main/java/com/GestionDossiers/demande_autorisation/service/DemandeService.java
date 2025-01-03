package com.GestionDossiers.demande_autorisation.service;
import com.GestionDossiers.demande_autorisation.entities.Status;
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

        // Set the current date for the demande
        demande.setDateDemande(new Date());

        // Find the commune corresponding to the given location
        Commun commune = communRepository.findByLocation(demande.getLocalisation());
        if (commune == null) {
            throw new RuntimeException("Aucune commune trouvée pour la localisation fournie.");
        }
        demande.setCommune(commune);

        // Ensure that the demande contains exactly 3 documents
        if (demande.getDocuments() == null || demande.getDocuments().size() != 3) {
            throw new RuntimeException("La demande doit contenir exactement 3 documents.");
        }

        // Set the demande for each document
        demande.getDocuments().forEach(doc -> doc.setDemande(demande));

        // Save and return the demande
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

    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
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

    public Demande getDemandeByIdAndCin(Long id, String cin) {
        if (id == null || cin == null || cin.isEmpty()) {
            throw new IllegalArgumentException("L'ID et le CIN ne peuvent pas être nuls ou vides.");
        }

        return demandeRepository.findByIdAndClient_Cin(id, cin)
                .orElseThrow(() -> new RuntimeException("Aucune demande trouvée avec l'ID : " + id + " et le CIN : " + cin));
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

}