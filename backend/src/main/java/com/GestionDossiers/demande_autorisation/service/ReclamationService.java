package com.GestionDossiers.demande_autorisation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GestionDossiers.demande_autorisation.entities.Reclamation;
import com.GestionDossiers.demande_autorisation.entities.Client;
import com.GestionDossiers.demande_autorisation.repository.ReclamationRepository;

import jakarta.mail.MessagingException;

@Service
public class ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private EmailService emailService;

    public Reclamation creerReclamation(Reclamation reclamation) throws MessagingException {
        // Sauvegarde dans la base de données
        Reclamation savedReclamation = reclamationRepository.save(reclamation);

        // Envoi de l'email via EmailService
        emailService.sendEmail(
            "aadia.khaoula20@gmail.com", 
            reclamation.getObjet(), 
            reclamation.getDetails()
        );

        return savedReclamation;
    }
}
