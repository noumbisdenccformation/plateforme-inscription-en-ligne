-- =============================================
-- Script SQL pour la Plateforme d'Inscription en Ligne
-- Base de données: inscription_db
-- Version: 1.0
-- Date: 18/09/2024
-- =============================================

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS inscription_db;
USE inscription_db;

-- =============================================
-- CRÉATION DES TABLES
-- =============================================

-- Table des utilisateurs
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('CANDIDATE', 'ADMIN', 'AGENT') DEFAULT 'CANDIDATE',
    provider ENUM('LOCAL', 'GOOGLE', 'MICROSOFT') DEFAULT 'LOCAL',
    provider_id VARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table des formulaires d'inscription
CREATE TABLE registration_forms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    
    -- Section 1: Informations Personnelles
    last_name VARCHAR(100),
    first_name VARCHAR(100),
    gender ENUM('MALE', 'FEMALE', 'NON_BINARY'),
    birth_date DATE,
    nationality VARCHAR(50),
    id_document_type ENUM('CNI', 'PASSPORT', 'BIRTH_CERTIFICATE'),
    id_document_number VARCHAR(50),
    
    -- Section 2: Documents (chemins)
    diploma_path VARCHAR(500),
    id_card_front_path VARCHAR(500),
    id_card_back_path VARCHAR(500),
    birth_certificate_path VARCHAR(500),
    photo_path VARCHAR(500),
    
    -- Section 3: Parcours Académique
    last_institution VARCHAR(200),
    specialization VARCHAR(100),
    study_start_date DATE,
    study_end_date DATE,
    
    -- Section 4: Coordonnées
    contact_email VARCHAR(100),
    phone_number VARCHAR(20),
    address TEXT,
    city VARCHAR(100),
    postal_code VARCHAR(10),
    country VARCHAR(50),
    
    -- Contact d'urgence
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relation VARCHAR(50),
    
    -- Workflow
    status ENUM('DRAFT', 'SUBMITTED', 'UNDER_REVIEW', 'VALIDATED', 'REJECTED', 'APPEAL') DEFAULT 'DRAFT',
    current_step INT DEFAULT 1,
    completion_percentage DECIMAL(5,2) DEFAULT 0.0,
    submitted_at TIMESTAMP NULL,
    validated_at TIMESTAMP NULL,
    assigned_agent_id BIGINT,
    rejection_reason TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_agent_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Table des documents
CREATE TABLE documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_form_id BIGINT NOT NULL,
    type ENUM('DIPLOMA', 'ID_CARD_FRONT', 'ID_CARD_BACK', 'BIRTH_CERTIFICATE', 'PHOTO', 'ADDITIONAL_DIPLOMA'),
    original_name VARCHAR(255),
    file_name VARCHAR(255),
    file_path VARCHAR(500),
    mime_type VARCHAR(100),
    file_size BIGINT,
    validation_status ENUM('PENDING', 'VALID', 'INVALID', 'SUSPICIOUS') DEFAULT 'PENDING',
    validation_notes TEXT,
    checksum VARCHAR(64),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    validated_at TIMESTAMP NULL,
    validated_by BIGINT,
    
    FOREIGN KEY (registration_form_id) REFERENCES registration_forms(id) ON DELETE CASCADE,
    FOREIGN KEY (validated_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Table des prénoms multiples
CREATE TABLE middle_names (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_form_id BIGINT NOT NULL,
    middle_name VARCHAR(50) NOT NULL,
    position_order INT DEFAULT 1,
    
    FOREIGN KEY (registration_form_id) REFERENCES registration_forms(id) ON DELETE CASCADE
);

-- Table des diplômes additionnels
CREATE TABLE additional_diplomas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_form_id BIGINT NOT NULL,
    diploma_path VARCHAR(500),
    diploma_name VARCHAR(200),
    institution VARCHAR(200),
    graduation_year YEAR,
    
    FOREIGN KEY (registration_form_id) REFERENCES registration_forms(id) ON DELETE CASCADE
);

-- =============================================
-- INDEX POUR OPTIMISATION
-- =============================================

CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_registration_forms_status ON registration_forms(status);
CREATE INDEX idx_registration_forms_user ON registration_forms(user_id);
CREATE INDEX idx_registration_forms_agent ON registration_forms(assigned_agent_id);
CREATE INDEX idx_documents_form ON documents(registration_form_id);
CREATE INDEX idx_documents_type ON documents(type);

-- =============================================
-- INSERTION DES DONNÉES DE TEST
-- =============================================

-- Utilisateurs (mot de passe: password123)
INSERT INTO users (username, email, password, first_name, last_name, role, provider, enabled, email_verified) VALUES
('admin', 'admin@inscription.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Admin', 'Système', 'ADMIN', 'LOCAL', true, true),
('agent1', 'agent1@inscription.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Marie', 'Dubois', 'AGENT', 'LOCAL', true, true),
('agent2', 'agent2@inscription.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Pierre', 'Martin', 'AGENT', 'LOCAL', true, true),
('candidat1', 'pierre.dupont@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Pierre', 'Dupont', 'CANDIDATE', 'LOCAL', true, true),
('candidat2', 'sophie.martin@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Sophie', 'Martin', 'CANDIDATE', 'LOCAL', true, true),
('candidat3', 'lucas.bernard@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Lucas', 'Bernard', 'CANDIDATE', 'LOCAL', true, true),
('candidat4', 'emma.rousseau@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Emma', 'Rousseau', 'CANDIDATE', 'LOCAL', true, true),
('candidat5', 'thomas.leroy@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Thomas', 'Leroy', 'CANDIDATE', 'LOCAL', true, true);

-- Formulaires d'inscription avec différents statuts
INSERT INTO registration_forms (
    user_id, last_name, first_name, gender, birth_date, nationality, 
    id_document_type, id_document_number, contact_email, phone_number, 
    address, city, postal_code, country, last_institution, specialization,
    study_start_date, study_end_date, emergency_contact_name, emergency_contact_phone,
    emergency_contact_relation, status, current_step, completion_percentage,
    submitted_at, assigned_agent_id
) VALUES
-- Formulaire complet et soumis (Pierre Dupont)
(4, 'Dupont', 'Pierre', 'MALE', '1995-03-15', 'Française', 'CNI', 'AB123456789', 
 'pierre.dupont@email.com', '+33123456789', '123 Rue de la Paix', 'Paris', '75001', 'France',
 'Université Paris-Sorbonne', 'Informatique', '2018-09-01', '2021-06-30',
 'Marie Dupont', '+33987654321', 'Mère', 'SUBMITTED', 5, 100.0, NOW(), 2),

-- Formulaire en cours (Sophie Martin)
(5, 'Martin', 'Sophie', 'FEMALE', '1997-07-22', 'Française', 'PASSPORT', 'FR987654321',
 'sophie.martin@email.com', '+33234567890', '456 Avenue des Champs', 'Lyon', '69001', 'France',
 'INSA Lyon', 'Génie Civil', '2019-09-01', '2022-06-30',
 'Jean Martin', '+33876543210', 'Père', 'DRAFT', 3, 65.0, NULL, NULL),

-- Formulaire validé (Lucas Bernard)
(6, 'Bernard', 'Lucas', 'MALE', '1996-11-08', 'Française', 'CNI', 'CD456789123',
 'lucas.bernard@email.com', '+33345678901', '789 Boulevard Saint-Michel', 'Marseille', '13001', 'France',
 'Université Aix-Marseille', 'Économie', '2017-09-01', '2020-06-30',
 'Claire Bernard', '+33765432109', 'Mère', 'VALIDATED', 5, 100.0, DATE_SUB(NOW(), INTERVAL 2 DAY), 2),

-- Formulaire rejeté (Emma Rousseau)
(7, 'Rousseau', 'Emma', 'FEMALE', '1998-01-12', 'Française', 'CNI', 'EF789123456',
 'emma.rousseau@email.com', '+33456789012', '321 Rue Victor Hugo', 'Toulouse', '31000', 'France',
 'Université Toulouse III', 'Biologie', '2020-09-01', '2023-06-30',
 'Paul Rousseau', '+33654321098', 'Père', 'REJECTED', 5, 100.0, DATE_SUB(NOW(), INTERVAL 1 DAY), 3),

-- Formulaire en révision (Thomas Leroy)
(8, 'Leroy', 'Thomas', 'MALE', '1994-09-25', 'Française', 'PASSPORT', 'GH123789456',
 'thomas.leroy@email.com', '+33567890123', '654 Place de la République', 'Bordeaux', '33000', 'France',
 'Université de Bordeaux', 'Droit', '2016-09-01', '2019-06-30',
 'Anne Leroy', '+33543210987', 'Mère', 'UNDER_REVIEW', 5, 100.0, DATE_SUB(NOW(), INTERVAL 1 DAY), 2);

-- Mise à jour des formulaires avec des informations supplémentaires
UPDATE registration_forms SET rejection_reason = 'Documents manquants ou illisibles' WHERE user_id = 7;
UPDATE registration_forms SET validated_at = DATE_SUB(NOW(), INTERVAL 1 DAY) WHERE user_id = 6;

-- Insertion de prénoms multiples
INSERT INTO middle_names (registration_form_id, middle_name, position_order) VALUES
(1, 'Alexandre', 1),
(1, 'Jean', 2),
(2, 'Marie', 1),
(3, 'Paul', 1);

-- Insertion de documents simulés
INSERT INTO documents (registration_form_id, type, original_name, file_name, file_path, mime_type, file_size, validation_status) VALUES
(1, 'DIPLOMA', 'diplome_licence.pdf', 'doc_1_diploma.pdf', '/uploads/user_4/doc_1_diploma.pdf', 'application/pdf', 2048576, 'VALID'),
(1, 'ID_CARD_FRONT', 'cni_recto.jpg', 'doc_1_id_front.jpg', '/uploads/user_4/doc_1_id_front.jpg', 'image/jpeg', 1024768, 'VALID'),
(1, 'ID_CARD_BACK', 'cni_verso.jpg', 'doc_1_id_back.jpg', '/uploads/user_4/doc_1_id_back.jpg', 'image/jpeg', 1024768, 'VALID'),
(1, 'PHOTO', 'photo_identite.jpg', 'doc_1_photo.jpg', '/uploads/user_4/doc_1_photo.jpg', 'image/jpeg', 512384, 'VALID'),
(1, 'BIRTH_CERTIFICATE', 'acte_naissance.pdf', 'doc_1_birth.pdf', '/uploads/user_4/doc_1_birth.pdf', 'application/pdf', 1536512, 'VALID'),

(3, 'DIPLOMA', 'master_economie.pdf', 'doc_3_diploma.pdf', '/uploads/user_6/doc_3_diploma.pdf', 'application/pdf', 2560000, 'VALID'),
(3, 'ID_CARD_FRONT', 'cni_recto.jpg', 'doc_3_id_front.jpg', '/uploads/user_6/doc_3_id_front.jpg', 'image/jpeg', 1024768, 'VALID'),
(3, 'ID_CARD_BACK', 'cni_verso.jpg', 'doc_3_id_back.jpg', '/uploads/user_6/doc_3_id_back.jpg', 'image/jpeg', 1024768, 'VALID'),
(3, 'PHOTO', 'photo_identite.jpg', 'doc_3_photo.jpg', '/uploads/user_6/doc_3_photo.jpg', 'image/jpeg', 512384, 'VALID'),
(3, 'BIRTH_CERTIFICATE', 'acte_naissance.pdf', 'doc_3_birth.pdf', '/uploads/user_6/doc_3_birth.pdf', 'application/pdf', 1536512, 'VALID');

-- =============================================
-- VUES UTILES POUR LES STATISTIQUES
-- =============================================

-- Vue pour les statistiques par statut
CREATE VIEW form_statistics AS
SELECT 
    status,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM registration_forms), 2) as percentage
FROM registration_forms
GROUP BY status;

-- Vue pour les formulaires en retard
CREATE VIEW overdue_forms AS
SELECT 
    rf.id,
    rf.user_id,
    CONCAT(rf.first_name, ' ', rf.last_name) as candidate_name,
    rf.submitted_at,
    DATEDIFF(NOW(), rf.submitted_at) as days_pending,
    u.first_name as agent_name
FROM registration_forms rf
LEFT JOIN users u ON rf.assigned_agent_id = u.id
WHERE rf.status = 'UNDER_REVIEW' 
AND rf.submitted_at < DATE_SUB(NOW(), INTERVAL 2 DAY);

-- =============================================
-- PROCÉDURES STOCKÉES
-- =============================================

DELIMITER //

-- Procédure pour obtenir les statistiques du tableau de bord
CREATE PROCEDURE GetDashboardStats()
BEGIN
    SELECT 
        (SELECT COUNT(*) FROM registration_forms WHERE status = 'SUBMITTED') as pending_forms,
        (SELECT COUNT(*) FROM registration_forms WHERE status = 'UNDER_REVIEW') as under_review,
        (SELECT COUNT(*) FROM registration_forms WHERE status = 'VALIDATED') as validated,
        (SELECT COUNT(*) FROM registration_forms WHERE status = 'REJECTED') as rejected,
        (SELECT AVG(completion_percentage) FROM registration_forms WHERE status = 'DRAFT') as avg_completion,
        (SELECT COUNT(*) FROM registration_forms WHERE submitted_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)) as weekly_submissions;
END //

-- Procédure pour assigner automatiquement les formulaires
CREATE PROCEDURE AutoAssignForms()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE form_id BIGINT;
    DECLARE agent_id BIGINT;
    
    DECLARE form_cursor CURSOR FOR 
        SELECT id FROM registration_forms 
        WHERE status = 'SUBMITTED' AND assigned_agent_id IS NULL
        ORDER BY completion_percentage DESC, submitted_at ASC;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN form_cursor;
    
    read_loop: LOOP
        FETCH form_cursor INTO form_id;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- Sélectionner l'agent avec le moins de dossiers assignés
        SELECT u.id INTO agent_id
        FROM users u
        LEFT JOIN registration_forms rf ON u.id = rf.assigned_agent_id AND rf.status = 'UNDER_REVIEW'
        WHERE u.role = 'AGENT' AND u.enabled = true
        GROUP BY u.id
        ORDER BY COUNT(rf.id) ASC
        LIMIT 1;
        
        -- Assigner le formulaire à l'agent
        UPDATE registration_forms 
        SET assigned_agent_id = agent_id, status = 'UNDER_REVIEW'
        WHERE id = form_id;
        
    END LOOP;
    
    CLOSE form_cursor;
END //

DELIMITER ;

-- =============================================
-- INFORMATIONS DE CONNEXION POUR LES TESTS
-- =============================================

/*
COMPTES DE TEST (mot de passe: password123):

ADMINISTRATEUR:
- Username: admin
- Email: admin@inscription.com

AGENTS:
- Username: agent1 (Marie Dubois)
- Username: agent2 (Pierre Martin)

CANDIDATS:
- Username: candidat1 (Pierre Dupont) - Formulaire soumis
- Username: candidat2 (Sophie Martin) - Formulaire en cours (65%)
- Username: candidat3 (Lucas Bernard) - Formulaire validé
- Username: candidat4 (Emma Rousseau) - Formulaire rejeté
- Username: candidat5 (Thomas Leroy) - Formulaire en révision

STATUTS DES FORMULAIRES:
- 1 DRAFT (en cours)
- 1 SUBMITTED (soumis)
- 1 UNDER_REVIEW (en révision)
- 1 VALIDATED (validé)
- 1 REJECTED (rejeté)
*/