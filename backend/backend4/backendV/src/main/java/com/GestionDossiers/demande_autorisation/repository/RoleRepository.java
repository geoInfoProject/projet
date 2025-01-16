package com.GestionDossiers.demande_autorisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GestionDossiers.demande_autorisation.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role); // Pour chercher un rôle spécifique
}