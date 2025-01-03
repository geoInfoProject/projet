package com.GestionDossiers.demande_autorisation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.GestionDossiers.demande_autorisation.entities.Client;
import com.GestionDossiers.demande_autorisation.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    //@Autowired
    //private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;


    public void creerCompte(Client client) {
        // Hacher le mot de passe avant de le stocker
        //client.setMotDePasse(passwordEncoder.encode(client.getMotDePasse()));
        clientRepository.save(client);
        
     // Envoyer un email de vérification
        envoyerEmailVerification(client);
    }
   
    private void envoyerEmailVerification(Client client) {
        String verificationUrl = "http://localhost:8080/api/verify?email=" + client.getMail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(client.getMail());
        message.setSubject("Vérification de votre compte");
        message.setText("Cliquez sur le lien suivant pour vérifier votre compte : " + verificationUrl);

        mailSender.send(message);
    }

    public void verifierCompte(String email) {
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            client.setVerif(true);
            clientRepository.save(client);
        }
    }
}


