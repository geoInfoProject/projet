package com.GestionDossiers.demande_autorisation.dto;

import java.util.Date;

public class DemandeDTO {
    private Long id;
    private Date dateDemande;
    private float x;
    private float y;
    private String nomCommune;
    private String cinClient;
    private String nomClient;
    private String prenomClient;
    private String status;
    private String autorisation;

    // Constructeur
    public DemandeDTO(Long id, Date dateDemande, float x, float y, String nomCommune,
                      String cinClient, String nomClient, String prenomClient,
                      String status, String autorisation) {
        this.id = id;
        this.dateDemande = dateDemande;
        this.x = x;
        this.y = y;
        this.nomCommune = nomCommune;
        this.cinClient = cinClient;
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.status = status;
        this.autorisation = autorisation;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public String getCinClient() {
        return cinClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public String getStatus() {
        return status;
    }

    public String getAutorisation() {
        return autorisation;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public void setCinClient(String cinClient) {
        this.cinClient = cinClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAutorisation(String autorisation) {
        this.autorisation = autorisation;
    }
}
