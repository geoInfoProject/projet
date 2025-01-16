package com.GestionDossiers.demande_autorisation.controller;

import com.GestionDossiers.demande_autorisation.dto.DemandeDTO;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.GestionDossiers.demande_autorisation.entities.Client;
import com.GestionDossiers.demande_autorisation.entities.Commun;
import com.GestionDossiers.demande_autorisation.entities.Demande;
import com.GestionDossiers.demande_autorisation.entities.Document;
import com.GestionDossiers.demande_autorisation.service.DemandeService;
import com.GestionDossiers.demande_autorisation.service.FileUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createDemande(
            @RequestParam("demande") String demandeJson,
            @RequestParam("documents") List<MultipartFile> files) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Demande demande = mapper.readValue(demandeJson, Demande.class);

        // Vérification des fichiers
        if (files == null || files.size() != 3) {
            throw new RuntimeException("Exactement 3 documents doivent être fournis pour créer une demande.");
        }

        // Sauvegarder la demande
        Demande savedDemande = demandeService.createDemande(demande);

        // Traiter et enregistrer les documents
        List<Document> documents = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.trim().isEmpty()) {
                throw new RuntimeException("Le fichier n'a pas de nom ou est vide.");
            }

            // Enregistrer le fichier
            String filePath = fileUploadService.saveFile(file, savedDemande.getId().toString(), fileName);
            if (filePath == null || filePath.trim().isEmpty()) {
                throw new RuntimeException("Erreur lors de l'enregistrement du fichier.");
            }

            // Créer un objet Document
            Document document = new Document();
            document.setName(fileName);
            document.setPath(filePath);
            document.setType(file.getContentType());
            documents.add(document);
        }

        // Associer les documents à la demande
        savedDemande.setDocuments(documents);
        demandeService.updateDemande(savedDemande);

        // Retourner un message de succès
        return ResponseEntity.ok("Demande créée avec succès.");
    }

    /**
     * Récupérer toutes les demandes.
     */
    @GetMapping
    public ResponseEntity<List<DemandeDTO>> getAllDemandes() {
        return ResponseEntity.ok(demandeService.getAllDemandes());
    }

    /**
     * Récupérer une demande par ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        return ResponseEntity.ok(demandeService.getDemandeById(id));
    }

    /**
     * Récupérer une demande par  CIN du client.
     */
    @GetMapping("/client")
    public ResponseEntity<List<Demande>> getDemandesByCin(@RequestParam String cin) {
        List<Demande> demandes = demandeService.getDemandesByCin(cin);
        return ResponseEntity.ok(demandes);
    }

    /**
     * Recherche des demandes avec des critères facultatifs.
     */
    @GetMapping("/recherche")
    public List<Demande> rechercherDemandes(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String commune) {
        return demandeService.rechercherDemande(statut, type, commune);
    }
    

    /**
     * Recherche des demandes avec ID client obligatoire et critères facultatifs.
     */
    @GetMapping("/rechercheParClient")
    public List<Demande> rechercherDemandesParClient(
            @RequestParam Long clientId,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String commune) {
        return demandeService.rechercherDemandeParClientObligatoire(clientId, statut, type, commune);
    }

    @PutMapping("/modifierStatut")
    public ResponseEntity<Demande> modifierStatut(@RequestParam Long demandeId, @RequestParam String statut) {
        try {
            Demande demande = demandeService.modifierStatut(demandeId, statut);
            return ResponseEntity.ok(demande);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    /**
     * Suppression d'une demande par ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDemande(@PathVariable Long id) {
        try {
            demandeService.deleteDemande(id);
            return ResponseEntity.ok("Demande supprimée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    /**
     * Récupère les informations de la commune associée à une demande via son ID.
     *
     * @param demandeId l'ID de la demande.
     * @return les informations de la commune correspondante.
     */
    @GetMapping("/{demandeId}/commune")
    public ResponseEntity<Commun> getCommuneByDemandeId(@PathVariable Long demandeId) {
        try {
            Commun commune = demandeService.getCommuneByDemandeId(demandeId);
            if (commune == null) {
                return ResponseEntity.notFound().build(); // 404 si la commune n'est pas trouvée
            }
            return ResponseEntity.ok(commune);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 en cas d'erreur
        }
    }
    @GetMapping("/{id}/client")
    public ResponseEntity<Client> getClientByDemandeId(@PathVariable Long id) {
        Client client = demandeService.getClientByDemandeId(id);
        return ResponseEntity.ok(client);
    }
    /**
     * Récupère les documents associés à une demande et génère des URLs de téléchargement.
     */
    @GetMapping("/{demandeId}/documents")
    public ResponseEntity<List<String>> getDocumentsUrls(@PathVariable Long demandeId) {
        // Récupérer la demande avec ses documents
        Demande demande = demandeService.getDemandeById(demandeId);
        if (demande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si la demande n'existe pas
        }

        List<String> fileUrls = new ArrayList<>();
        // Pour chaque document, générer l'URL de téléchargement
        for (Document document : demande.getDocuments()) {
            String fileName = document.getName();
            String fileUrl = fileUploadService.generateFileDownloadUrl(demandeId.toString(), fileName);
            fileUrls.add(fileUrl);
        }

        return ResponseEntity.ok(fileUrls); // Retourner les URLs
    }

    // Méthode pour télécharger un fichier
    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam String demandeId, @RequestParam String fileName) {
        try {
            String projectRootPath = System.getProperty("user.dir");
            String filePath = Paths.get(projectRootPath, "documents", demandeId, fileName).toString();
            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 si le fichier n'existe pas
            }

            // Charger le fichier comme une ressource
            Path path = (Path) Paths.get(file.getAbsolutePath());
            Resource resource = new UrlResource(path.toUri());

            // Retourner le fichier avec un header pour le téléchargement
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 en cas d'erreur
        }
    }

}
