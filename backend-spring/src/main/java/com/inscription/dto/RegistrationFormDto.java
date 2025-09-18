package com.inscription.dto;

import com.inscription.entity.RegistrationForm;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RegistrationFormDto {
    private Long id;
    private Long userId;

    // Section 1: Informations Personnelles
    @NotBlank(message = "Le nom est obligatoire")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s-']+$", message = "Le nom ne doit contenir que des lettres")
    private String lastName;

    @NotBlank(message = "Le prénom est obligatoire")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s-']+$", message = "Le prénom ne doit contenir que des lettres")
    private String firstName;

    private List<String> middleNames;

    @NotNull(message = "Le sexe est obligatoire")
    private RegistrationForm.Gender gender;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate birthDate;

    @NotBlank(message = "La nationalité est obligatoire")
    private String nationality;

    @NotNull(message = "Le type de pièce d'identité est obligatoire")
    private RegistrationForm.IdDocumentType idDocumentType;

    @NotBlank(message = "Le numéro de pièce d'identité est obligatoire")
    private String idDocumentNumber;

    // Section 2: Documents (chemins)
    private String diplomaPath;
    private String idCardFrontPath;
    private String idCardBackPath;
    private String birthCertificatePath;
    private String photoPath;
    private List<String> additionalDiplomasPaths;

    // Section 3: Parcours Académique
    private String lastInstitution;
    private String specialization;
    private LocalDate studyStartDate;
    private LocalDate studyEndDate;

    // Section 4: Coordonnées
    @NotBlank(message = "L'email de contact est obligatoire")
    @Email(message = "Format d'email invalide")
    private String contactEmail;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Format de téléphone invalide")
    private String phoneNumber;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;
    private String city;
    private String postalCode;
    private String country;

    // Contact d'urgence
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;

    // Workflow
    private RegistrationForm.FormStatus status;
    private Integer currentStep;
    private Double completionPercentage;
    private LocalDateTime submittedAt;
    private LocalDateTime validatedAt;
    private Long assignedAgentId;
    private String rejectionReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}