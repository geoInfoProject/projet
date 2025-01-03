package com.GestionDossiers.demande_autorisation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {

	public String saveFile(MultipartFile file, String demandeId, String fileName) {
	    try {
	        // Construire le chemin du répertoire : C:/documents/{demandeId}
	        String directoryPath = "C:/documents/" + demandeId;
	        System.out.println("Répertoire de destination : " + directoryPath);

	        // Créer le répertoire s'il n'existe pas
	        File directory = new File(directoryPath);
	        if (!directory.exists()) {
	            boolean created = directory.mkdirs(); // Crée tous les répertoires manquants
	            if (created) {
	                System.out.println("Répertoire créé : " + directoryPath);
	            } else {
	                System.out.println("Échec de la création du répertoire : " + directoryPath);
	            }
	        }

	        // Construire le chemin complet du fichier
	        String filePath = directoryPath + "/" + fileName;
	        System.out.println("Chemin complet du fichier : " + filePath);

	        // Sauvegarder le fichier
	        File destinationFile = new File(filePath);
	        file.transferTo(destinationFile);

	        return filePath; // Retourner le chemin complet du fichier
	    } catch (IOException e) {
	        throw new RuntimeException("Erreur lors de l'enregistrement du fichier : " + e.getMessage(), e);
	    }
	}

    
}