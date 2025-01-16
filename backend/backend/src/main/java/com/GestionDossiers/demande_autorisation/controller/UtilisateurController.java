package com.GestionDossiers.demande_autorisation.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GestionDossiers.demande_autorisation.service.UtilisateurService;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/ajouter-gestionnaire")
    public ResponseEntity<String> ajouterGestionnaire(@RequestBody Map<String, String> request) {
        String gmail = request.get("gmail");
        String motDePasse = request.get("motDePasse");

        try {
            utilisateurService.ajouterUtilisateurGestionnaire(gmail, motDePasse);
            return ResponseEntity.ok("Utilisateur gestionnaire ajouté avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }
}
