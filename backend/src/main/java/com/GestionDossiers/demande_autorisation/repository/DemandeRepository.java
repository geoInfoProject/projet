package com.GestionDossiers.demande_autorisation.repository;

import com.GestionDossiers.demande_autorisation.entities.Demande;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemandeRepository extends JpaRepository<Demande, Long> {

    @Query("SELECT d FROM Demande d WHERE " +
            "(:statut IS NULL OR d.status.statut = :statut) AND " +
            "(:type IS NULL OR d.autorisation.type = :type) AND " +
            "(:commune IS NULL OR d.commune.libelle = :commune)")
    List<Demande> searchDemande(
            @Param("statut") String statut, 
            @Param("type") String type, 
            @Param("commune") String commune);
    // Méthode pour récupérer une demande par ID et CIN
    Optional<Demande> findByIdAndClient_Cin(Long id, String cin);
    @Query("SELECT d FROM Demande d WHERE " +
            "d.client.id = :clientId AND " +
            "(:statut IS NULL OR d.status.statut = :statut) AND " +
            "(:type IS NULL OR d.autorisation.type = :type) AND " +
            "(:commune IS NULL OR d.commune.libelle = :commune)")
    List<Demande> rechercherParClient(
        @Param("clientId") Long clientId, // Changer ici aussi
        @Param("statut") String statut, 
        @Param("type") String type, 
        @Param("commune") String commune);

    @Query("SELECT d FROM Demande d WHERE d.id = :id AND d.client.cin = :cin")
    Optional<Demande> findByIdAndCin(@Param("id") Long id, @Param("cin") String cin);

}