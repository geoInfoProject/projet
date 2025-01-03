package com.GestionDossiers.demande_autorisation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GestionDossiers.demande_autorisation.entities.Role;
import com.GestionDossiers.demande_autorisation.entities.utilisateur;
import com.GestionDossiers.demande_autorisation.repository.RoleRepository;
import com.GestionDossiers.demande_autorisation.repository.UtilisateurRepository;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    public utilisateur ajouterUtilisateurGestionnaire(String gmail, String motDePasse) {
        Role gestionnaireRole = roleRepository.findByRole("Gestionnaire");
        if (gestionnaireRole == null) {
            throw new RuntimeException("Rôle Gestionnaire introuvable !");
        }

        utilisateur utilisateur = new utilisateur();
        utilisateur.setGmail(gmail);
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setRole(gestionnaireRole);

        return utilisateurRepository.save(utilisateur);
    }
}
