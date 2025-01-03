package com.GestionDossiers.demande_autorisation.controller;
import com.GestionDossiers.demande_autorisation.entities.Demande;
import com.GestionDossiers.demande_autorisation.entities.Document;
import com.GestionDossiers.demande_autorisation.service.DemandeService;
import com.GestionDossiers.demande_autorisation.service.FileUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * Création d'une demande avec des documents associés.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Demande createDemande(
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
        return demandeService.updateDemande(savedDemande);
    }

    /**
     * Récupérer toutes les demandes.
     */
    @GetMapping
    public ResponseEntity<List<Demande>> getAllDemandes() {
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
     * Récupérer une demande par ID et CIN du client.
     */
    @GetMapping("/{id}/client")
    public ResponseEntity<Demande> getDemandeByIdAndCin(
            @PathVariable Long id,
            @RequestParam String cin) {
        return ResponseEntity.ok(demandeService.getDemandeByIdAndCin(id, cin));
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



}