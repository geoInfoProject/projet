package com.GestionDossiers.demande_autorisation.entities;

import jakarta.persistence.*;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    private String type; 

    @ManyToOne // ou une autre relation appropriée
    @JoinColumn(name = "demande_id", nullable = false) // La colonne dans la table de base de données
    private Demande demande;


    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }
}
