package com.inscription.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "registration_form_id")
    private RegistrationForm registrationForm;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private String originalName;
    private String fileName;
    private String filePath;
    private String mimeType;
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    private ValidationStatus validationStatus = ValidationStatus.PENDING;

    private String validationNotes;
    private String checksum; // Pour détecter les doublons

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    private Long validatedBy;

    public enum DocumentType {
        DIPLOMA, ID_CARD_FRONT, ID_CARD_BACK, BIRTH_CERTIFICATE, PHOTO, ADDITIONAL_DIPLOMA
    }

    public enum ValidationStatus {
        PENDING, VALID, INVALID, SUSPICIOUS
    }

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}