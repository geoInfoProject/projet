package com.GestionDossiers.demande_autorisation.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "utilisateur") // Attention au mot-clé réservé. Ajouter des guillemets doubles si nécessaire.
public class utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id;

    @JsonProperty("gmail")
    @Column(name = "gmail", nullable = false, unique = true)
    private String gmail;

    @JsonIgnore // Empêche la sérialisation du mot de passe
    @JsonProperty("motDePasse")
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @JsonProperty("role")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role", nullable = false)
    private Role role;

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
