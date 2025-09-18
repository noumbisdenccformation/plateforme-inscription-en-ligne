package com.inscription.service;

import com.inscription.dto.RegistrationFormDto;
import com.inscription.entity.RegistrationForm;
import com.inscription.entity.User;
import com.inscription.repository.RegistrationFormRepository;
import com.inscription.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegistrationService {

    private final RegistrationFormRepository registrationFormRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final DocumentService documentService;

    public RegistrationFormDto createOrUpdateForm(Long userId, RegistrationFormDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        RegistrationForm form = registrationFormRepository.findByUserId(userId)
                .orElse(new RegistrationForm());

        if (form.getId() == null) {
            form.setUser(user);
        }

        // Mapper les données
        mapDtoToEntity(dto, form);
        
        // Calculer le pourcentage de completion
        form.setCompletionPercentage(calculateCompletionPercentage(form));
        
        // Validation de l'âge (≥16 ans)
        if (form.getBirthDate() != null) {
            int age = Period.between(form.getBirthDate(), LocalDateTime.now().toLocalDate()).getYears();
            if (age < 16) {
                throw new RuntimeException("L'âge minimum requis est de 16 ans");
            }
        }

        RegistrationForm savedForm = registrationFormRepository.save(form);
        
        // Sauvegarde automatique
        log.info("Formulaire sauvegardé automatiquement pour l'utilisateur {}", userId);
        
        return mapEntityToDto(savedForm);
    }

    public RegistrationFormDto getFormByUserId(Long userId) {
        Optional<RegistrationForm> form = registrationFormRepository.findByUserId(userId);
        return form.map(this::mapEntityToDto).orElse(null);
    }

    public RegistrationFormDto submitForm(Long userId) {
        RegistrationForm form = registrationFormRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Formulaire non trouvé"));

        if (form.getCompletionPercentage() < 100.0) {
            throw new RuntimeException("Le formulaire doit être complété à 100% avant soumission");
        }

        form.setStatus(RegistrationForm.FormStatus.SUBMITTED);
        form.setSubmittedAt(LocalDateTime.now());
        
        RegistrationForm savedForm = registrationFormRepository.save(form);
        
        // Envoyer email de confirmation
        emailService.sendSubmissionConfirmation(form.getUser().getEmail(), form);
        
        // Démarrer la pré-validation automatique
        startAutomaticPreValidation(savedForm);
        
        log.info("Formulaire soumis par l'utilisateur {}", userId);
        
        return mapEntityToDto(savedForm);
    }

    public List<RegistrationFormDto> getFormsByStatus(RegistrationForm.FormStatus status) {
        return registrationFormRepository.findByStatus(status)
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<RegistrationFormDto> getSubmittedFormsByPriority() {
        return registrationFormRepository.findSubmittedOrderedByPriority()
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public RegistrationFormDto assignToAgent(Long formId, Long agentId) {
        RegistrationForm form = registrationFormRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Formulaire non trouvé"));

        form.setAssignedAgentId(agentId);
        form.setStatus(RegistrationForm.FormStatus.UNDER_REVIEW);
        
        RegistrationForm savedForm = registrationFormRepository.save(form);
        
        log.info("Formulaire {} assigné à l'agent {}", formId, agentId);
        
        return mapEntityToDto(savedForm);
    }

    public RegistrationFormDto validateForm(Long formId, boolean approved, String reason) {
        RegistrationForm form = registrationFormRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Formulaire non trouvé"));

        if (approved) {
            form.setStatus(RegistrationForm.FormStatus.VALIDATED);
            form.setValidatedAt(LocalDateTime.now());
            emailService.sendValidationConfirmation(form.getUser().getEmail(), form);
        } else {
            form.setStatus(RegistrationForm.FormStatus.REJECTED);
            form.setRejectionReason(reason);
            emailService.sendRejectionNotification(form.getUser().getEmail(), form, reason);
        }
        
        RegistrationForm savedForm = registrationFormRepository.save(form);
        
        log.info("Formulaire {} {} par l'agent", formId, approved ? "validé" : "rejeté");
        
        return mapEntityToDto(savedForm);
    }

    private void startAutomaticPreValidation(RegistrationForm form) {
        // Vérification automatique des documents
        boolean documentsValid = documentService.validateDocuments(form);
        
        if (!documentsValid) {
            form.setStatus(RegistrationForm.FormStatus.REJECTED);
            form.setRejectionReason("Documents invalides ou manquants");
            registrationFormRepository.save(form);
            
            emailService.sendRejectionNotification(form.getUser().getEmail(), form, 
                "Vos documents n'ont pas passé la validation automatique");
        }
    }

    private Double calculateCompletionPercentage(RegistrationForm form) {
        int totalFields = 20; // Nombre total de champs obligatoires
        int completedFields = 0;

        // Section 1: Informations personnelles (8 champs)
        if (form.getLastName() != null && !form.getLastName().isEmpty()) completedFields++;
        if (form.getFirstName() != null && !form.getFirstName().isEmpty()) completedFields++;
        if (form.getGender() != null) completedFields++;
        if (form.getBirthDate() != null) completedFields++;
        if (form.getNationality() != null && !form.getNationality().isEmpty()) completedFields++;
        if (form.getIdDocumentType() != null) completedFields++;
        if (form.getIdDocumentNumber() != null && !form.getIdDocumentNumber().isEmpty()) completedFields++;

        // Section 2: Documents (5 champs)
        if (form.getDiplomaPath() != null) completedFields++;
        if (form.getIdCardFrontPath() != null) completedFields++;
        if (form.getIdCardBackPath() != null) completedFields++;
        if (form.getBirthCertificatePath() != null) completedFields++;
        if (form.getPhotoPath() != null) completedFields++;

        // Section 3: Parcours académique (2 champs)
        if (form.getLastInstitution() != null && !form.getLastInstitution().isEmpty()) completedFields++;
        if (form.getSpecialization() != null && !form.getSpecialization().isEmpty()) completedFields++;

        // Section 4: Coordonnées (5 champs)
        if (form.getContactEmail() != null && !form.getContactEmail().isEmpty()) completedFields++;
        if (form.getPhoneNumber() != null && !form.getPhoneNumber().isEmpty()) completedFields++;
        if (form.getAddress() != null && !form.getAddress().isEmpty()) completedFields++;
        if (form.getCity() != null && !form.getCity().isEmpty()) completedFields++;
        if (form.getCountry() != null && !form.getCountry().isEmpty()) completedFields++;

        return (double) completedFields / totalFields * 100.0;
    }

    private void mapDtoToEntity(RegistrationFormDto dto, RegistrationForm entity) {
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleNames(dto.getMiddleNames());
        entity.setGender(dto.getGender());
        entity.setBirthDate(dto.getBirthDate());
        entity.setNationality(dto.getNationality());
        entity.setIdDocumentType(dto.getIdDocumentType());
        entity.setIdDocumentNumber(dto.getIdDocumentNumber());
        
        entity.setLastInstitution(dto.getLastInstitution());
        entity.setSpecialization(dto.getSpecialization());
        entity.setStudyStartDate(dto.getStudyStartDate());
        entity.setStudyEndDate(dto.getStudyEndDate());
        
        entity.setContactEmail(dto.getContactEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setPostalCode(dto.getPostalCode());
        entity.setCountry(dto.getCountry());
        
        entity.setEmergencyContactName(dto.getEmergencyContactName());
        entity.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        entity.setEmergencyContactRelation(dto.getEmergencyContactRelation());
        
        entity.setCurrentStep(dto.getCurrentStep());
    }

    private RegistrationFormDto mapEntityToDto(RegistrationForm entity) {
        RegistrationFormDto dto = new RegistrationFormDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleNames(entity.getMiddleNames());
        dto.setGender(entity.getGender());
        dto.setBirthDate(entity.getBirthDate());
        dto.setNationality(entity.getNationality());
        dto.setIdDocumentType(entity.getIdDocumentType());
        dto.setIdDocumentNumber(entity.getIdDocumentNumber());
        
        dto.setDiplomaPath(entity.getDiplomaPath());
        dto.setIdCardFrontPath(entity.getIdCardFrontPath());
        dto.setIdCardBackPath(entity.getIdCardBackPath());
        dto.setBirthCertificatePath(entity.getBirthCertificatePath());
        dto.setPhotoPath(entity.getPhotoPath());
        dto.setAdditionalDiplomasPaths(entity.getAdditionalDiplomasPaths());
        
        dto.setLastInstitution(entity.getLastInstitution());
        dto.setSpecialization(entity.getSpecialization());
        dto.setStudyStartDate(entity.getStudyStartDate());
        dto.setStudyEndDate(entity.getStudyEndDate());
        
        dto.setContactEmail(entity.getContactEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setPostalCode(entity.getPostalCode());
        dto.setCountry(entity.getCountry());
        
        dto.setEmergencyContactName(entity.getEmergencyContactName());
        dto.setEmergencyContactPhone(entity.getEmergencyContactPhone());
        dto.setEmergencyContactRelation(entity.getEmergencyContactRelation());
        
        dto.setStatus(entity.getStatus());
        dto.setCurrentStep(entity.getCurrentStep());
        dto.setCompletionPercentage(entity.getCompletionPercentage());
        dto.setSubmittedAt(entity.getSubmittedAt());
        dto.setValidatedAt(entity.getValidatedAt());
        dto.setAssignedAgentId(entity.getAssignedAgentId());
        dto.setRejectionReason(entity.getRejectionReason());
        
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }
}