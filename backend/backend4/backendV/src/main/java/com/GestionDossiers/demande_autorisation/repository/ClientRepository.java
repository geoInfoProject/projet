package com.GestionDossiers.demande_autorisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GestionDossiers.demande_autorisation.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByEmail(String email);
}
