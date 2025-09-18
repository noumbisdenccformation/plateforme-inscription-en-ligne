package com.inscription.service;

import com.inscription.entity.Document;
import com.inscription.entity.RegistrationForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.max-size}")
    private long maxFileSize;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png"
    );

    private static final List<String> ALLOWED_PDF_TYPES = Arrays.asList(
        "application/pdf"
    );

    public String uploadDocument(MultipartFile file, Document.DocumentType type, Long userId) {
        try {
            // Validation de la taille
            if (file.getSize() > maxFileSize) {
                throw new RuntimeException("Fichier trop volumineux. Taille maximum : " + (maxFileSize / 1024 / 1024) + "MB");
            }

            // Validation du type MIME
            validateFileType(file, type);

            // Génération du nom de fichier unique
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            
            // Création du dossier utilisateur
            Path userDir = Paths.get(uploadDir, "user_" + userId);
            Files.createDirectories(userDir);
            
            // Sauvegarde du fichier
            Path filePath = userDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            log.info("Document {} uploadé avec succès pour l'utilisateur {}", fileName, userId);
            
            return filePath.toString();
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload du document : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'upload du document");
        }
    }

    public boolean validateDocuments(RegistrationForm form) {
        try {
            // Vérification de la présence des documents obligatoires
            boolean hasRequiredDocuments = 
                form.getDiplomaPath() != null &&
                form.getIdCardFrontPath() != null &&
                form.getIdCardBackPath() != null &&
                form.getBirthCertificatePath() != null &&
                form.getPhotoPath() != null;

            if (!hasRequiredDocuments) {
                log.warn("Documents manquants pour le formulaire {}", form.getId());
                return false;
            }

            // Vérification de l'existence des fichiers
            List<String> documentPaths = Arrays.asList(
                form.getDiplomaPath(),
                form.getIdCardFrontPath(),
                form.getIdCardBackPath(),
                form.getBirthCertificatePath(),
                form.getPhotoPath()
            );

            for (String path : documentPaths) {
                if (path != null && !Files.exists(Paths.get(path))) {
                    log.warn("Fichier manquant : {}", path);
                    return false;
                }
            }

            // Détection de doublons (checksum)
            if (hasDuplicateDocuments(documentPaths)) {
                log.warn("Documents dupliqués détectés pour le formulaire {}", form.getId());
                return false;
            }

            log.info("Validation automatique réussie pour le formulaire {}", form.getId());
            return true;

        } catch (Exception e) {
            log.error("Erreur lors de la validation des documents : {}", e.getMessage());
            return false;
        }
    }

    public String calculateChecksum(Path filePath) {
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(fileBytes);
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("Erreur lors du calcul du checksum : {}", e.getMessage());
            return null;
        }
    }

    private void validateFileType(MultipartFile file, Document.DocumentType type) {
        String mimeType = file.getContentType();
        
        switch (type) {
            case PHOTO:
                if (!ALLOWED_IMAGE_TYPES.contains(mimeType)) {
                    throw new RuntimeException("Format de photo invalide. Formats acceptés : JPG, PNG");
                }
                break;
            case DIPLOMA:
            case BIRTH_CERTIFICATE:
            case ADDITIONAL_DIPLOMA:
                if (!ALLOWED_PDF_TYPES.contains(mimeType)) {
                    throw new RuntimeException("Format de document invalide. Format accepté : PDF");
                }
                break;
            case ID_CARD_FRONT:
            case ID_CARD_BACK:
                if (!ALLOWED_IMAGE_TYPES.contains(mimeType)) {
                    throw new RuntimeException("Format de pièce d'identité invalide. Formats acceptés : JPG, PNG");
                }
                break;
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    private boolean hasDuplicateDocuments(List<String> documentPaths) {
        try {
            for (int i = 0; i < documentPaths.size(); i++) {
                for (int j = i + 1; j < documentPaths.size(); j++) {
                    String path1 = documentPaths.get(i);
                    String path2 = documentPaths.get(j);
                    
                    if (path1 != null && path2 != null) {
                        String checksum1 = calculateChecksum(Paths.get(path1));
                        String checksum2 = calculateChecksum(Paths.get(path2));
                        
                        if (checksum1 != null && checksum1.equals(checksum2)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            log.error("Erreur lors de la détection de doublons : {}", e.getMessage());
            return false;
        }
    }
}