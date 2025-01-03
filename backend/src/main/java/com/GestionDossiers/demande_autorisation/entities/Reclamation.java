package com.GestionDossiers.demande_autorisation.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reclamation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id;

    @Column(name = "objet", nullable = false)
    @JsonProperty("objet")
    private String objet;

    @Column(name = "details", nullable = false)
    @JsonProperty("details")
    private String details;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idClient", referencedColumnName = "idClient", nullable = false)
    @JsonProperty("client")
    private Client client;


    @Column(name = "date_creation", nullable = true)
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation;

    // Initialisation automatique de la date lors de la création
    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }

    // Getters pour éviter les problèmes avec Lombok
    public String getObjet() {
        return objet;
    }

    public String getDetails() {
        return details;
    }

    public Client getClient() {
        return client;
    }
    
    public int getId() {
        return id;
    }
}
