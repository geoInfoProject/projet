package com.GestionDossiers.demande_autorisation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.GestionDossiers.demande_autorisation.entities.Client;
import com.GestionDossiers.demande_autorisation.service.ClientService;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/register")
    public String registerClient(@RequestBody Client client) {
        clientService.creerCompte(client);
        return "Compte créé avec succès ! Veuillez vérifier votre email.";
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("email") String email) {
        clientService.verifierCompte(email);
        return "Votre compte a été vérifié avec succès !";
    }
}
