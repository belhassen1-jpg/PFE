package com.example.pidev.Services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class FileStorageService {

   @Value("${app.file.storage.location}")

    private String storageLocation;

    public String storeFile(MultipartFile file) throws IOException {
        // Générer un nom de fichier unique pour éviter les conflits
        String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "-" + file.getOriginalFilename());

        // Résoudre le chemin de stockage
        Path storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
        Path targetLocation = storagePath.resolve(fileName);

        // Créer le dossier de stockage s'il n'existe pas
        Files.createDirectories(storagePath);

        // Copier le fichier dans le dossier de stockage
        Files.copy(file.getInputStream(), targetLocation);

        // Retourner le chemin relatif du fichier stocké
        return fileName;
    }
}
