package com.GestionDossiers.demande_autorisation.service;

import com.GestionDossiers.demande_autorisation.entities.Status;
import com.GestionDossiers.demande_autorisation.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    // Méthode pour récupérer la liste des statuts
    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }
}
