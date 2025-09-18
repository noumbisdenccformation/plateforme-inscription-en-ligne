package com.inscription.service;

import com.inscription.entity.RegistrationForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendSubmissionConfirmation(String email, RegistrationForm form) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Confirmation de soumission - Dossier d'inscription");
            message.setText(buildSubmissionConfirmationText(form));
            
            mailSender.send(message);
            log.info("Email de confirmation envoyé à {}", email);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de confirmation à {}: {}", email, e.getMessage());
        }
    }

    @Async
    public void sendValidationConfirmation(String email, RegistrationForm form) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Validation de votre dossier d'inscription");
            message.setText(buildValidationConfirmationText(form));
            
            mailSender.send(message);
            log.info("Email de validation envoyé à {}", email);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de validation à {}: {}", email, e.getMessage());
        }
    }

    @Async
    public void sendRejectionNotification(String email, RegistrationForm form, String reason) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Notification concernant votre dossier d'inscription");
            message.setText(buildRejectionNotificationText(form, reason));
            
            mailSender.send(message);
            log.info("Email de rejet envoyé à {}", email);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de rejet à {}: {}", email, e.getMessage());
        }
    }

    @Async
    public void sendReminderNotification(String email, RegistrationForm form) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Rappel - Complétez votre dossier d'inscription");
            message.setText(buildReminderNotificationText(form));
            
            mailSender.send(message);
            log.info("Email de rappel envoyé à {}", email);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de rappel à {}: {}", email, e.getMessage());
        }
    }

    private String buildSubmissionConfirmationText(RegistrationForm form) {
        return String.format("""
            Bonjour %s %s,
            
            Nous avons bien reçu votre dossier d'inscription.
            
            Détails de votre dossier :
            - Numéro de dossier : %d
            - Date de soumission : %s
            - Statut : En cours de traitement
            
            Votre dossier sera examiné dans un délai de 24 à 48 heures.
            Vous recevrez une notification par email dès que le traitement sera terminé.
            
            Cordialement,
            L'équipe d'admission
            """, 
            form.getFirstName(), 
            form.getLastName(), 
            form.getId(),
            form.getSubmittedAt().toString()
        );
    }

    private String buildValidationConfirmationText(RegistrationForm form) {
        return String.format("""
            Bonjour %s %s,
            
            Félicitations ! Votre dossier d'inscription a été validé avec succès.
            
            Détails de votre dossier :
            - Numéro de dossier : %d
            - Date de validation : %s
            
            Vous recevrez prochainement les informations concernant la suite du processus d'inscription.
            
            Cordialement,
            L'équipe d'admission
            """, 
            form.getFirstName(), 
            form.getLastName(), 
            form.getId(),
            form.getValidatedAt().toString()
        );
    }

    private String buildRejectionNotificationText(RegistrationForm form, String reason) {
        return String.format("""
            Bonjour %s %s,
            
            Nous vous informons que votre dossier d'inscription n'a pas pu être validé.
            
            Détails de votre dossier :
            - Numéro de dossier : %d
            - Motif : %s
            
            Vous pouvez faire appel de cette décision en vous connectant à votre espace candidat
            et en utilisant la fonction "Recours en ligne".
            
            Cordialement,
            L'équipe d'admission
            """, 
            form.getFirstName(), 
            form.getLastName(), 
            form.getId(),
            reason
        );
    }

    private String buildReminderNotificationText(RegistrationForm form) {
        return String.format("""
            Bonjour %s %s,
            
            Votre dossier d'inscription est en cours de completion (%,.1f%%).
            
            Pour finaliser votre inscription, veuillez vous connecter à votre espace candidat
            et compléter les informations manquantes.
            
            Étape actuelle : %d/5
            
            Cordialement,
            L'équipe d'admission
            """, 
            form.getFirstName(), 
            form.getLastName(), 
            form.getCompletionPercentage(),
            form.getCurrentStep()
        );
    }
}