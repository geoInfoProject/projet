package com.GestionDossiers.demande_autorisation.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.GestionDossiers.demande_autorisation.entities.Client;
import com.GestionDossiers.demande_autorisation.repository.ClientRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/send-pdf")
    public ResponseEntity<String> sendPdfToUser(@RequestParam("clientId") int clientId) {
        try {
            // Récupérer le client depuis la base de données
            Client client = clientRepository.findById(clientId).orElse(null);

            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Client introuvable avec l'ID spécifié.");
            }

            String email = client.getMail(); // Email du client
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Le client n'a pas d'email associé.");
            }

            // Chemin du fichier PDF existant
            File pdfFile = new File("src/main/resources/static/document.pdf");

            // Vérifier si le fichier existe
            if (!pdfFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Le fichier PDF n'existe pas.");
            }

            // Envoyer l'email avec le PDF attaché
            sendEmailWithAttachment(email, pdfFile);

            return ResponseEntity.ok("Le document PDF a été envoyé avec succès au client !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erreur lors de l'envoi du document PDF.");
        }
    }

    private void sendEmailWithAttachment(String email, File pdfFile) throws MessagingException {
        // Création du message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Votre document PDF");
        helper.setText("Veuillez trouver en pièce jointe le document que vous avez demandé.");

        // Ajout du fichier PDF en pièce jointe
        helper.addAttachment(pdfFile.getName(), pdfFile);

        // Envoi de l'email
        mailSender.send(message);
    }
}
