package com.GestionDossiers.demande_autorisation.service;

import com.GestionDossiers.demande_autorisation.entities.Autorisation;
import com.GestionDossiers.demande_autorisation.repository.AutorisationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorisationService {

    private final AutorisationRepository autorisationRepository;

    public AutorisationService(AutorisationRepository autorisationRepository) {
        this.autorisationRepository = autorisationRepository;
    }

    // Méthode pour récupérer toutes les autorisations
    public List<Autorisation> getAllAutorisations() {
        return autorisationRepository.findAll();
    }
}
