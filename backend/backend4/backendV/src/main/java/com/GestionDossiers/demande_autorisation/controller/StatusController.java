package com.GestionDossiers.demande_autorisation.controller;

import com.GestionDossiers.demande_autorisation.entities.Status;
import com.GestionDossiers.demande_autorisation.service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    // Endpoint pour récupérer la liste des statuts
    @GetMapping
    public List<Status> getStatuses() {
        return statusService.getAllStatuses();
    }
}
