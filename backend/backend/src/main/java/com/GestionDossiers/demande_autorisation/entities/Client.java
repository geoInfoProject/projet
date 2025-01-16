package com.GestionDossiers.demande_autorisation.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "client")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("idClient")
    private int idClient;

    @JsonProperty("cin")
    @Column(name = "cin", nullable = false, unique = true)
    private String cin;

    @JsonProperty("nom")
    @Column(name = "nom", nullable = false)
    private String nom;

    @JsonProperty("prenom")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @JsonProperty("email")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonProperty("motDePasse")
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @JsonProperty("verif")
    @Column(name = "verif", nullable = false)
    private boolean verif;

    // Getters et Setters
    public int getId() {
        return idClient;
    }

    public void setId(int id) {
        this.idClient = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean isVerif() {
        return verif;
    }

    public void setVerif(boolean verif) {
        this.verif = verif;
    }

	public void setAuthentification(boolean b) {
		// TODO Auto-generated method stub
		
	}
}