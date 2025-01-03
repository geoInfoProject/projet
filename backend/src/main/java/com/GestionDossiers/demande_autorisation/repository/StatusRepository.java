package com.GestionDossiers.demande_autorisation.repository;

import com.GestionDossiers.demande_autorisation.entities.Status;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByStatut(String statut); // Utiliser "statut" au lieu de "nom"
}
