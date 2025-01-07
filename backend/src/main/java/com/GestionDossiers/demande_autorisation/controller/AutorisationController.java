package com.GestionDossiers.demande_autorisation.controller;
import com.GestionDossiers.demande_autorisation.entities.Autorisation;
import com.GestionDossiers.demande_autorisation.service.AutorisationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/autorisations")
public class AutorisationController {

    private final AutorisationService autorisationService;

    public AutorisationController(AutorisationService autorisationService) {
        this.autorisationService = autorisationService;
    }

    // Endpoint pour récupérer la liste des autorisations
    @GetMapping
    public List<Autorisation> getAutorisations() {
        return autorisationService.getAllAutorisations();
    }
}
