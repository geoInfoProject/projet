package com.GestionDossiers.demande_autorisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GestionDossiers.demande_autorisation.entities.Document;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}