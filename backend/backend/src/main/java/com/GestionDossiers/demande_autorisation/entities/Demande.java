package com.GestionDossiers.demande_autorisation.entities;
import org.locationtech.jts.geom.Point;
import com.GestionDossiers.demande_autorisation.deserialization.PointSerializer;
import com.GestionDossiers.demande_autorisation.deserialization.PointDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDemande;
    
    @Column(name = "x", nullable = false)
    private float x;

    @Column(name = "y", nullable = false)
    private float y;


    @JsonDeserialize(using = PointDeserializer.class)
    @JsonSerialize(using = PointSerializer.class)
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point localisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false) // Correspond au nom exact dans la table demande
    private Client client;
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autorisation_id", nullable = false)
    private Autorisation autorisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commune_id", nullable = true) // nullable = true pour permettre les demandes sans commune
    private Commun commune;


    @Column(name = "verification", nullable = false)
    private boolean verification;


    @Column(name = "description")
    private String description;
   
    
   

    @OneToMany(mappedBy = "demande", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Document> documents;

    // Getters et Setters
    public Long getId() {
        return id;
        
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    public Point getLocalisation() {
        return localisation;
    }

    public void setLocalisation(Point localisation) {
        this.localisation = localisation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Autorisation getAutorisation() {
        return autorisation;
    }

    public void setAutorisation(Autorisation autorisation) {
        this.autorisation = autorisation;
    }

    public Commun getCommune() {
        return commune;
    }

    public void setCommune(Commun commune) {
        this.commune = commune;
    }
 // Getters et setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
 // Getters et setters
    public Boolean getVerification() {
        return verification;
    }

    public void setVerification(Boolean verification) {
        this.verification = verification;
    }
    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
}}
