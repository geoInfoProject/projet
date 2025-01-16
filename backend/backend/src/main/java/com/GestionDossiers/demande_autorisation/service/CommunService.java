package com.GestionDossiers.demande_autorisation.service;


import com.GestionDossiers.demande_autorisation.entities.Commun;
import com.GestionDossiers.demande_autorisation.repository.CommunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunService {

    @Autowired
    private CommunRepository communRepository;

    // Méthode pour obtenir toutes les communes
    public List<Commun> getAllCommunes() {
        return communRepository.findAll();
    }
}