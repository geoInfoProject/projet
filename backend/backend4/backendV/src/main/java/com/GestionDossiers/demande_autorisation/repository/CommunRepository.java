package com.GestionDossiers.demande_autorisation.repository;


import com.GestionDossiers.demande_autorisation.entities.Commun;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunRepository extends JpaRepository<Commun, Integer> {

    @Query(value = "SELECT * FROM commun c WHERE ST_Intersects(ST_SetSRID(c.geom, 4326), ST_SetSRID(:point, 4326)) = true", nativeQuery = true)
    Commun findByLocation(@Param("point") Point point);
    // Méthode pour récupérer toutes les communes
    List<Commun> findAll();
}