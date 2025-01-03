package com.GestionDossiers.demande_autorisation.controller;


import com.GestionDossiers.demande_autorisation.entities.Commun;
import com.GestionDossiers.demande_autorisation.service.CommunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommunController {

    @Autowired
    private CommunService communService;

    @GetMapping("/communes")
    public List<Commun> getAllCommunes() {
        return communService.getAllCommunes();
    }
}