# TCH057 Projet de session : Application Mobile Agence Touristique

Développé dans le cadre du cours TCH057 "Applications mobiles et expérience usager", ce projet est une application mobile intuitive conçue pour une Agence Touristique basée à Montréal.

---

## Contributions

Ce projet a été développé par :

*   Alexis Fecteau
*   Jimmy Allard
*   Yasmine Beddouch

---

## Aperçu du Projet

https://github.com/user-attachments/assets/7aba1b44-2fe4-4c9a-aca9-c630b94f76e3

https://github.com/user-attachments/assets/742d2a41-29c4-42d6-84b3-9ec997b1624c

L'objectif principal de cette application mobile est de faciliter le processus de planification, de recherche, de consultation et de réservation de voyages pour les utilisateurs. Elle fournit des informations détaillées sur les destinations, optimise le parcours de réservation et permet de consulter l'historique des réservations.

Fonctionnalités clés :

*   **Connexion et Inscription:** Authentification des utilisateurs et création de comptes.
*   **Accueil et Recherche de Voyages:** Interface pour explorer les voyages avec des critères de recherche (destination, budget, type, date de départ). Affichage d'une liste dynamique des résultats.
*   **Détails du Voyage et Réservation:** Consultation d'informations complètes sur un voyage sélectionné (description, programme, activités, prix). Sélection de dates de départ et réservation avec gestion de la disponibilité en temps réel.
*   **Historique des Réservations:** Consultation de l'ensemble des voyages réservés, y compris les détails et la possibilité d'annuler.

Gestion des données :

*   Les comptes clients et les données des voyages sont stockés sur un **serveur JSON local** (`voyages.json`).
*   L'historique des réservations est stocké dans une base de données **SQLite locale**, permettant un accès et une consultation **même en mode hors ligne**.

---

## Installation de l'Application Mobile

Pour installer et exécuter l'application mobile Agence Touristique, vous aurez besoin d'Android Studio et du code source de ce dépôt. Vous devrez également mettre en place un serveur JSON local utilisant le fichier `voyages.json`.

1.  **Cloner le dépôt :**
    ```bash
    https://github.com/Spoodern0t/TCH057_AgenceTouristique.git
    ```

2.  **Ouvrir dans Android Studio :**
    *   Lancez Android Studio.
    *   Sélectionnez "Open an Existing Project" (Ouvrir un projet existant) et naviguez jusqu'au répertoire du dépôt que vous avez cloné.

3.  **Mettre en place le serveur JSON local :**
    *   L'application utilise un serveur JSON local pour les données des voyages et des clients. Vous devrez installer et configurer un serveur JSON (par exemple, `json-server` via npm : `npm install -g json-server`).
    *   Exécutez le serveur JSON en pointant vers le fichier `voyages.json` (ex: `json-server --watch assets/voyages.json` si le fichier est dans le dossier `assets/`). L'URL par défaut est généralement `http://localhost:3000/`.
    *   Assurez-vous que votre application Android est configurée pour se connecter à cette URL locale.

4.  **Compiler et Exécuter :**
    *   Connectez un appareil Android à votre ordinateur ou lancez un émulateur Android via Android Studio.
    *   Sélectionnez l'appareil ou l'émulateur cible dans la barre d'outils d'Android Studio.
    *   Cliquez sur le bouton "Run" (Exécuter) (le triangle vert) pour compiler et installer l'application sur l'appareil/émulateur sélectionné.

---

## Technologies

*   **Développement Mobile :** Java (Android Studio), XML (Layouts)
*   **Gestion des Données :** Serveur JSON local (`json-server`), SQLite
*   **Design & Prototypage :** Figma (pour les maquettes d'interface)
*   **Architecture :** Respect de l'architecture par packages enseignée (activités, modèles, adaptateurs, dao, etc.)
*   **Outils :** Git, GitHub

## Link Figma de l'Application Mobile

https://www.figma.com/design/qwjoJRvwe32phCkAzhGR6f/PLANED?node-id=17-2&t=c1Tm3MkrLkeEDjk1-1
