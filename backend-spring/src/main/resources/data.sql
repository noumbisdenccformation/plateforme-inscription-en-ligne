-- Insertion des utilisateurs de test (mot de passe: password123)
INSERT INTO users (username, email, password, first_name, last_name, role, provider, enabled, email_verified, created_at, updated_at) VALUES
('admin', 'admin@inscription.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Admin', 'Système', 'ADMIN', 'LOCAL', true, true, NOW(), NOW()),
('agent1', 'agent1@inscription.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Marie', 'Dubois', 'AGENT', 'LOCAL', true, true, NOW(), NOW()),
('agent2', 'agent2@inscription.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Pierre', 'Martin', 'AGENT', 'LOCAL', true, true, NOW(), NOW()),
('candidat1', 'pierre.dupont@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Pierre', 'Dupont', 'CANDIDATE', 'LOCAL', true, true, NOW(), NOW()),
('candidat2', 'sophie.martin@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Sophie', 'Martin', 'CANDIDATE', 'LOCAL', true, true, NOW(), NOW()),
('candidat3', 'lucas.bernard@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Lucas', 'Bernard', 'CANDIDATE', 'LOCAL', true, true, NOW(), NOW()),
('candidat4', 'emma.rousseau@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Emma', 'Rousseau', 'CANDIDATE', 'LOCAL', true, true, NOW(), NOW());

-- Insertion des formulaires d'inscription
INSERT INTO registration_forms (
    user_id, last_name, first_name, gender, birth_date, nationality, 
    id_document_type, id_document_number, contact_email, phone_number, 
    address, city, postal_code, country, last_institution, specialization,
    study_start_date, study_end_date, emergency_contact_name, emergency_contact_phone,
    emergency_contact_relation, status, current_step, completion_percentage,
    submitted_at, assigned_agent_id, created_at, updated_at
) VALUES
-- Formulaire complet et soumis (Pierre Dupont)
(4, 'Dupont', 'Pierre', 'MALE', '1995-03-15', 'Française', 'CNI', 'AB123456789', 
 'pierre.dupont@email.com', '+33123456789', '123 Rue de la Paix', 'Paris', '75001', 'France',
 'Université Paris-Sorbonne', 'Informatique', '2018-09-01', '2021-06-30',
 'Marie Dupont', '+33987654321', 'Mère', 'SUBMITTED', 5, 100.0, NOW(), 2, NOW(), NOW()),

-- Formulaire en cours (Sophie Martin)
(5, 'Martin', 'Sophie', 'FEMALE', '1997-07-22', 'Française', 'PASSPORT', 'FR987654321',
 'sophie.martin@email.com', '+33234567890', '456 Avenue des Champs', 'Lyon', '69001', 'France',
 'INSA Lyon', 'Génie Civil', '2019-09-01', '2022-06-30',
 'Jean Martin', '+33876543210', 'Père', 'DRAFT', 3, 65.0, NULL, NULL, NOW(), NOW()),

-- Formulaire validé (Lucas Bernard)
(6, 'Bernard', 'Lucas', 'MALE', '1996-11-08', 'Française', 'CNI', 'CD456789123',
 'lucas.bernard@email.com', '+33345678901', '789 Boulevard Saint-Michel', 'Marseille', '13001', 'France',
 'Université Aix-Marseille', 'Économie', '2017-09-01', '2020-06-30',
 'Claire Bernard', '+33765432109', 'Mère', 'VALIDATED', 5, 100.0, NOW() - INTERVAL '2 days', 2, NOW(), NOW()),

-- Formulaire rejeté (Emma Rousseau)
(7, 'Rousseau', 'Emma', 'FEMALE', '1998-01-12', 'Française', 'CNI', 'EF789123456',
 'emma.rousseau@email.com', '+33456789012', '321 Rue Victor Hugo', 'Toulouse', '31000', 'France',
 'Université Toulouse III', 'Biologie', '2020-09-01', '2023-06-30',
 'Paul Rousseau', '+33654321098', 'Père', 'REJECTED', 5, 100.0, NOW() - INTERVAL '1 day', 3, NOW(), NOW());

-- Mise à jour des formulaires avec des raisons de rejet
UPDATE registration_forms SET rejection_reason = 'Documents manquants ou illisibles' WHERE user_id = 7;
UPDATE registration_forms SET validated_at = NOW() WHERE user_id = 6;