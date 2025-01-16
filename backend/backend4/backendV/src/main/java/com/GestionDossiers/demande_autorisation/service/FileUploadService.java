package com.GestionDossiers.demande_autorisation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    /**
     * Méthode pour sauvegarder un fichier.
     * @param file Le fichier à sauvegarder.
     * @param demandeId L'identifiant de la demande.
     * @param fileName Le nom du fichier à sauvegarder.
     * @return Le chemin complet du fichier sauvegardé.
     */
    public String saveFile(MultipartFile file, String demandeId, String fileName) {
        try {
            // Obtenir la racine du projet
            String projectRootPath = System.getProperty("user.dir");
            String directoryPath = Paths.get(projectRootPath, "documents", demandeId).toString();

            // Créer le répertoire s'il n'existe pas
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new RuntimeException("Échec de la création du répertoire.");
                }
            }

            // Construire le chemin complet du fichier
            String filePath = Paths.get(directoryPath, fileName).toString();

            // Sauvegarder le fichier
            file.transferTo(new File(filePath));

            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier : " + e.getMessage(), e);
        }
    }

    /**
     * Méthode pour générer une URL pour le téléchargement d'un fichier.
     * @param demandeId L'identifiant de la demande.
     * @param fileName Le nom du fichier.
     * @return L'URL de téléchargement du fichier.
     */
    public String generateFileDownloadUrl(String demandeId, String fileName) {
        // Construire une URL appropriée selon vos besoins
        String baseUrl = "http://localhost:8080/api/demandes/downloadFile";
        return baseUrl + "?demandeId=" + demandeId + "&fileName=" + fileName;
    }

    /**
     * Méthode pour supprimer un répertoire et son contenu.
     * @param demandeId L'identifiant de la demande.
     */
    public void deleteDirectory(String demandeId) {
        // Obtenir la racine du projet
        String projectRootPath = System.getProperty("user.dir");
        String directoryPath = Paths.get(projectRootPath, "documents", demandeId).toString();

        // Supprimer le répertoire et son contenu
        File directory = new File(directoryPath);
        if (directory.exists()) {
            deleteRecursively(directory);
            System.out.println("Répertoire supprimé : " + directoryPath);
        } else {
            System.out.println("Aucun répertoire trouvé pour l'ID de demande : " + demandeId);
        }
    }

    /**
     * Méthode utilitaire pour supprimer un répertoire récursivement.
     * @param file Le fichier ou répertoire à supprimer.
     */
    private void deleteRecursively(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                deleteRecursively(subFile);
            }
        }
        file.delete();
    }
}
