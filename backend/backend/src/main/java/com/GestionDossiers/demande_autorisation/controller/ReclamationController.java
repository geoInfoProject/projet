package com.GestionDossiers.demande_autorisation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GestionDossiers.demande_autorisation.entities.Reclamation;

@RestController
@RequestMapping("/reclamations")
public class ReclamationController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/envoyer")
    public String envoyerReclamation(@RequestBody Reclamation reclamation) {
        // Récupérer l'email du client depuis l'objet "Client" lié à la réclamation
        String clientEmail = reclamation.getClient().getMail();
        

        // Créer l'email avec le sujet et la description dynamiques, incluant l'ID du client
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("khaoulaaadia@gmail.com");  // Utilisez une adresse d'expéditeur dédiée
        message.setReplyTo(clientEmail);  // L'adresse email du client pour la réponse
        message.setTo("aadia.khaoula20@gmail.com"); // Le destinataire (peut être une adresse fixe ou dynamique)
        message.setSubject("Réclamation: " + reclamation.getObjet());
        message.setText("Description: " + reclamation.getDetails() + "\n\nID du client: " + reclamation.getClient().getId());


        try {
            mailSender.send(message);
            return "Réclamation envoyée avec succès!";
        } catch (Exception e) {
            return "Échec de l'envoi de la réclamation : " + e.getMessage();
        }
    }
}
