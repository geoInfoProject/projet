package com.GestionDossiers.demande_autorisation.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.GestionDossiers.demande_autorisation.deserialization.MultiPolygonSerializer;
import org.locationtech.jts.geom.MultiPolygon;
import jakarta.persistence.*;

@Entity
public class Commun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gid;

    private String libelle;

    @Column(columnDefinition = "geometry(MultiPolygon, 4326)", nullable = false)
    @JsonSerialize(using = MultiPolygonSerializer.class)  // Utilisation du sérialiseur personnalisé
    private MultiPolygon geom;

    // Getters et Setters
    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public MultiPolygon getGeom() {
        return geom;
    }

    public void setGeom(MultiPolygon geom) {
        this.geom = geom;
    }
}