# Plateforme d'Inscription en Ligne avec Workflow Automatisé

## 🎯 Description
Système complet de gestion des inscriptions pour établissements scolaires/universitaires avec processus d'inscription en 5 étapes, validation automatisée des documents, génération d'emails transactionnels et tableau de bord analytique.

## 🏗️ Architecture

### Frontend (Angular 17 + Tailwind CSS)
**Espace Candidat:**
- 🗂 Formulaire intelligent avec progress stepper (5 étapes)
- 📤 Upload de documents (PDF/IMG) avec preview
- 💾 Sauvegarde automatique
- 📧 Notifications intégrées et alertes de progression

**Espace Administrateur:**
- 📊 Dashboard avec statistiques temps réel
- 🗺️ Carte thermique des inscriptions
- 📑 Gestion des dossiers avec validation documentaire
- 📊 Export massif (Excel/CSV)

**Pages Publiques:**
- 🌐 Landing page avec animations
- 🔐 Portail de connexion multi-auth (email, Google, Microsoft)
- 🤖 CAPTCHA intelligent

### Backend (Spring Boot 3)
**Modules Clés:**
- ✉️ Email Service avec templates personnalisés
- 📄 Document Manager avec vérification des formats
- 🔐 Auth Service (JWT + OAuth2)
- 🕵️ Audit des connexions
- ⏰ Planification (Quartz Scheduler)

## 📋 Structure du Formulaire d'Inscription

### Section 1: Informations Personnelles
- 🔤 Nom/Prénom(s) avec validation alphabet
- ⚧️ Sexe (M/F/Non-binaire)
- 🎂 Date de naissance (validation ≥16 ans)
- 🌍 Nationalité avec recherche dynamique
- 🆔 Type de pièce d'identité

### Section 2: Documents Officiels
- 📄 Diplômes (PDF, max 5Mo, preview)
- 🖼️ CNI recto/verso (JPG/PNG avec OCR)
- 🏛️ Acte de naissance (PDF avec détection)
- 📸 Photo d'identité (ratio 3.5x4.5cm)

### Section 3: Parcours Académique
- 🎓 Dernier établissement (autocomplete)
- 📚 Spécialisation (nested select)
- 📅 Période de formation

### Section 4: Coordonnées
- 📧 Email (double vérification)
- 📱 Téléphone (validation internationale)
- 🏠 Adresse (API géolocalisation)
- 🚨 Personne à contacter (urgence)

## 🔄 Workflow d'Inscription Renforcé

1. **Pré-validation automatique (2 min)**
   - ✅ Vérification format des documents
   - ✅ Détection de fraude élémentaire

2. **Contrôle manuel (24-48h)**
   - 👩💼 Attribution aléatoire à un agent
   - 🚦 Système de priorisation

3. **Notification multicanal**
   - ✉️ Email de confirmation
   - 📱 SMS de rappel
   - 🔔 Notification in-app

4. **Recours en ligne (si rejet)**
   - 📅 Prise de rendez-vous virtuel
   - 💬 Chat avec l'administration

## 🔒 Sécurités Implémentées

**Protection des Données:**
- 🔒 Chiffrement PII au repos
- 🕵️ Détection des copies de documents
- 🚨 Alerte pour uploads suspects

**Expérience Utilisateur:**
- 🌀 Progress bar dynamique
- 📱 Design mobile-first
- ♿ Accessibilité WCAG AA

## 🚀 Installation et Démarrage

### Prérequis
- Java 17+
- Node.js 18+
- PostgreSQL 13+
- Maven 3.6+

### Base de données
```bash
# Créer la base PostgreSQL
createdb inscription_db

# Importer les données de test
psql -d inscription_db -f database/inscription_export.sql
```

### Backend Spring Boot
```bash
cd backend-spring
mvn spring-boot:run
```
API disponible sur http://localhost:8080

### Frontend Angular
```bash
cd frontend
npm install
ng serve
```
Application disponible sur http://localhost:4200

## 👥 Comptes de Test

**Mot de passe unique:** `password123`

| Rôle | Username | Email | Statut Formulaire |
|------|----------|-------|------------------|
| **Admin** | `admin` | admin@inscription.com | - |
| **Agent** | `agent1` | agent1@inscription.com | - |
| **Agent** | `agent2` | agent2@inscription.com | - |
| **Candidat** | `candidat1` | pierre.dupont@email.com | Soumis |
| **Candidat** | `candidat2` | sophie.martin@email.com | En cours (65%) |
| **Candidat** | `candidat3` | lucas.bernard@email.com | Validé |
| **Candidat** | `candidat4` | emma.rousseau@email.com | Rejeté |
| **Candidat** | `candidat5` | thomas.leroy@email.com | En révision |

## 🌐 URLs de Test
- **Application:** http://localhost:4200
- **API Backend:** http://localhost:8080
- **Documentation API:** http://localhost:8080/swagger-ui.html

## 📊 Tableau de Bord Admin

| Section | Fonctionnalités |
|---------|----------------|
| **Suivi des Dossiers** | Carte thermique des validations |
| **Analytique** | Taux de complétion par étape |
| **Alertes** | Dossiers bloqués >48h |
| **Outils** | Recherche cross-documents |

## 📁 Structure du Projet

```
plateforme-inscription-en-ligne/
├── backend-spring/           # API Spring Boot 3
│   ├── src/main/java/com/inscription/
│   │   ├── entity/          # Entités JPA
│   │   ├── repository/      # Repositories
│   │   ├── service/         # Services métier
│   │   ├── controller/      # Contrôleurs REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── config/         # Configuration
│   │   └── security/       # Sécurité JWT/OAuth2
│   └── src/main/resources/
│       ├── application.yml  # Configuration
│       └── data.sql        # Données de test
├── frontend/                # Application Angular 17
│   └── src/app/
│       ├── form/           # Formulaire 5 étapes
│       ├── admin/          # Interface admin
│       ├── pages/          # Pages publiques
│       └── services/       # Services Angular
├── database/               # Scripts SQL
│   └── inscription_export.sql
└── docs/                   # Documentation
```

## 🔧 Configuration

### Variables d'environnement
```yaml
# Base de données
DB_USERNAME=postgres
DB_PASSWORD=password

# Email
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# OAuth2
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
MICROSOFT_CLIENT_ID=your-microsoft-client-id
MICROSOFT_CLIENT_SECRET=your-microsoft-client-secret

# JWT
JWT_SECRET=your-secret-key

# Upload
FILE_UPLOAD_DIR=./uploads
```

## 📈 Fonctionnalités Avancées

- **Validation automatique** des documents en 2 minutes
- **Système de priorité** basé sur la complétude du dossier
- **Attribution automatique** des agents selon la charge de travail
- **Notifications multicanal** (email, SMS, in-app)
- **Détection de fraude** élémentaire sur les documents
- **Sauvegarde automatique** du formulaire
- **Export massif** des données en Excel/CSV
- **Tableau de bord analytique** temps réel
- **Système de recours** en ligne
- **Chat intégré** avec l'administration

## 🛠️ Stack Technique

| Composant | Technologies |
|-----------|-------------|
| **Frontend** | Angular 17, Tailwind CSS, TypeScript |
| **Backend** | Spring Boot 3, Spring Security, JWT |
| **Base de données** | PostgreSQL, Hibernate/JPA |
| **Upload** | Apache PDFBox, Multipart |
| **Email** | Spring Mail, Thymeleaf |
| **Scheduler** | Quartz Scheduler |
| **Documentation** | Swagger/OpenAPI |
| **OAuth2** | Google, Microsoft |

Le système est conçu pour gérer efficacement le processus d'inscription avec une expérience utilisateur optimale et des outils d'administration puissants.