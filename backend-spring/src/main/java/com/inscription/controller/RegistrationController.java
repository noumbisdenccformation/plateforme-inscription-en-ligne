package com.inscription.controller;

import com.inscription.dto.RegistrationFormDto;
import com.inscription.entity.Document;
import com.inscription.entity.RegistrationForm;
import com.inscription.service.DocumentService;
import com.inscription.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
@Tag(name = "Registration", description = "API de gestion des inscriptions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RegistrationController {

    private final RegistrationService registrationService;
    private final DocumentService documentService;

    @GetMapping("/form")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Récupérer le formulaire de l'utilisateur connecté")
    public ResponseEntity<RegistrationFormDto> getMyForm(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        RegistrationFormDto form = registrationService.getFormByUserId(userId);
        return ResponseEntity.ok(form);
    }

    @PostMapping("/form")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Créer ou mettre à jour le formulaire")
    public ResponseEntity<RegistrationFormDto> saveForm(
            @Valid @RequestBody RegistrationFormDto formDto,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        RegistrationFormDto savedForm = registrationService.createOrUpdateForm(userId, formDto);
        return ResponseEntity.ok(savedForm);
    }

    @PostMapping("/form/submit")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Soumettre le formulaire pour validation")
    public ResponseEntity<RegistrationFormDto> submitForm(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        RegistrationFormDto submittedForm = registrationService.submitForm(userId);
        return ResponseEntity.ok(submittedForm);
    }

    @PostMapping("/upload/{documentType}")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Upload d'un document")
    public ResponseEntity<String> uploadDocument(
            @PathVariable String documentType,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        
        Long userId = getUserIdFromAuth(authentication);
        Document.DocumentType type = Document.DocumentType.valueOf(documentType.toUpperCase());
        
        String filePath = documentService.uploadDocument(file, type, userId);
        
        return ResponseEntity.ok(filePath);
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Obtenir le statut du dossier")
    public ResponseEntity<RegistrationFormDto> getStatus(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        RegistrationFormDto form = registrationService.getFormByUserId(userId);
        return ResponseEntity.ok(form);
    }

    @GetMapping("/forms/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    @Operation(summary = "Obtenir les formulaires par statut")
    public ResponseEntity<List<RegistrationFormDto>> getFormsByStatus(@PathVariable String status) {
        RegistrationForm.FormStatus formStatus = RegistrationForm.FormStatus.valueOf(status.toUpperCase());
        List<RegistrationFormDto> forms = registrationService.getFormsByStatus(formStatus);
        return ResponseEntity.ok(forms);
    }

    @GetMapping("/forms/priority")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    @Operation(summary = "Obtenir les formulaires soumis par ordre de priorité")
    public ResponseEntity<List<RegistrationFormDto>> getFormsByPriority() {
        List<RegistrationFormDto> forms = registrationService.getSubmittedFormsByPriority();
        return ResponseEntity.ok(forms);
    }

    @PostMapping("/forms/{formId}/assign/{agentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assigner un formulaire à un agent")
    public ResponseEntity<RegistrationFormDto> assignToAgent(
            @PathVariable Long formId,
            @PathVariable Long agentId) {
        RegistrationFormDto form = registrationService.assignToAgent(formId, agentId);
        return ResponseEntity.ok(form);
    }

    @PostMapping("/forms/{formId}/validate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    @Operation(summary = "Valider ou rejeter un formulaire")
    public ResponseEntity<RegistrationFormDto> validateForm(
            @PathVariable Long formId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String reason) {
        RegistrationFormDto form = registrationService.validateForm(formId, approved, reason);
        return ResponseEntity.ok(form);
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        // Extraction de l'ID utilisateur depuis l'authentification
        // Cette implémentation dépend de votre système d'authentification
        return 1L; // Placeholder - à implémenter selon votre logique JWT
    }
}