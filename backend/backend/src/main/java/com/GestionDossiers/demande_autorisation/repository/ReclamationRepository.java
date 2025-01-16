package com.GestionDossiers.demande_autorisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GestionDossiers.demande_autorisation.entities.Reclamation;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {
}