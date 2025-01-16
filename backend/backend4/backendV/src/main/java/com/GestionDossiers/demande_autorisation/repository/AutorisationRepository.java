package com.GestionDossiers.demande_autorisation.repository;

import com.GestionDossiers.demande_autorisation.entities.Autorisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorisationRepository extends JpaRepository<Autorisation, Long> {
    // JpaRepository fournit déjà les méthodes CRUD par défaut, y compris findById.
}
