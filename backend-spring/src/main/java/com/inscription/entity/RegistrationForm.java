package com.inscription.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "registration_forms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Section 1: Informations Personnelles
    @NotBlank
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s-']+$", message = "Nom invalide")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s-']+$", message = "Prénom invalide")
    private String firstName;

    @ElementCollection
    private List<String> middleNames;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Past(message = "Date de naissance invalide")
    private LocalDate birthDate;

    @NotBlank
    private String nationality;

    @Enumerated(EnumType.STRING)
    private IdDocumentType idDocumentType;

    @NotBlank
    private String idDocumentNumber;

    // Section 2: Documents (stockage des chemins)
    private String diplomaPath;
    private String idCardFrontPath;
    private String idCardBackPath;
    private String birthCertificatePath;
    private String photoPath;

    @ElementCollection
    private List<String> additionalDiplomasPaths;

    // Section 3: Parcours Académique
    private String lastInstitution;
    private String specialization;
    private LocalDate studyStartDate;
    private LocalDate studyEndDate;

    // Section 4: Coordonnées
    @NotBlank
    @Email
    private String contactEmail;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Numéro de téléphone invalide")
    private String phoneNumber;

    @NotBlank
    private String address;
    private String city;
    private String postalCode;
    private String country;

    // Personne à contacter en urgence
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;

    // Workflow
    @Enumerated(EnumType.STRING)
    private FormStatus status = FormStatus.DRAFT;

    private Integer currentStep = 1;
    private Double completionPercentage = 0.0;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    private Long assignedAgentId;
    private String rejectionReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Gender {
        MALE, FEMALE, NON_BINARY
    }

    public enum IdDocumentType {
        CNI, PASSPORT, BIRTH_CERTIFICATE
    }

    public enum FormStatus {
        DRAFT, SUBMITTED, UNDER_REVIEW, VALIDATED, REJECTED, APPEAL
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}