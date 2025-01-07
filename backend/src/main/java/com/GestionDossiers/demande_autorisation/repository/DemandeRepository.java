package com.GestionDossiers.demande_autorisation.repository;

import com.GestionDossiers.demande_autorisation.dto.DemandeDTO;
import com.GestionDossiers.demande_autorisation.entities.Client;
import com.GestionDossiers.demande_autorisation.entities.Demande;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemandeRepository extends JpaRepository<Demande, Long> {

	@Query("SELECT new com.GestionDossiers.demande_autorisation.dto.DemandeDTO(" +
		       "d.id, d.dateDemande, d.x, d.y, c.libelle, cl.cin, cl.nom, cl.prenom, s.statut, a.type) " +
		       "FROM Demande d " +
		       "JOIN d.commune c " +
		       "JOIN d.client cl " +
		       "JOIN d.status s " +
		       "JOIN d.autorisation a")
		List<DemandeDTO> findAllDemandeDTOs();



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

    
    List<Demande> findAllByClient_Cin(String cin);
    @Query("SELECT d.client FROM Demande d WHERE d.id = :id")
    Optional<Client> findClientByDemandeId(@Param("id") Long id);

}