package com.GestionDossiers.demande_autorisation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    public String saveFile(MultipartFile file, String demandeId, String fileName) {
        try {
            // Obtenir le chemin de la racine du projet
            String projectRootPath = System.getProperty("user.dir"); // Racine du projet
            System.out.println("Racine du projet : " + projectRootPath);

            // Construire le chemin du répertoire : {racine_du_projet}/documents/{demandeId}
            String directoryPath = Paths.get(projectRootPath, "documents", demandeId).toString();
            System.out.println("Répertoire de destination : " + directoryPath);

            // Créer le répertoire s'il n'existe pas
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean created = directory.mkdirs(); // Crée tous les répertoires manquants
                if (created) {
                    System.out.println("Répertoire créé : " + directoryPath);
                } else {
                    throw new RuntimeException("Échec de la création du répertoire : " + directoryPath);
                }
            }

            // Construire le chemin complet du fichier
            String filePath = Paths.get(directoryPath, fileName).toString();
            System.out.println("Chemin complet du fichier : " + filePath);

            // Sauvegarder le fichier
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);

            return filePath; // Retourner le chemin complet du fichier
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier : " + e.getMessage(), e);
        }
    }
    public void deleteDirectory(String demandeId) {
        // Obtenir le chemin de la racine du projet
        String projectRootPath = System.getProperty("user.dir");
        String directoryPath = Paths.get(projectRootPath, "documents", demandeId).toString();

        // Supprimer le dossier et son contenu
        File directory = new File(directoryPath);
        if (directory.exists()) {
            deleteRecursively(directory); // Supprime récursivement les fichiers et sous-dossiers
            System.out.println("Répertoire supprimé : " + directoryPath);
        } else {
            System.out.println("Aucun répertoire trouvé pour l'ID de demande : " + demandeId);
        }
    }

    // Méthode utilitaire pour supprimer un répertoire récursivement
    private void deleteRecursively(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                deleteRecursively(subFile);
            }
        }
        file.delete(); // Supprime le fichier ou répertoire
    }

}
