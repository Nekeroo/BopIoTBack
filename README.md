# Serveur - BopIoT 

Bienvenue sur le README de l'application Serveur pour le projet "BopIoT".

## Contexte

### Description du Projet

Nous proposons une carte interactive équipée de plusieurs capteurs et contrôles :

- Deux **joysticks** :
    - L’**un pour les directions** Haut, Bas, Droite, Gauche
    - **L’autre** pour sa **fonctionnalité de “Bouton”**,
- Un **accéléromètre**
- Et un **microphone**.

Cet objet sera connecté à un smartphone. Celui-ci aura pour rôle de donner les instructions à réaliser à l’utilisateur. Le jeu possède 3 niveaux de difficultés : Facile / Moyen / Difficile. Le téléphone donne chaque action à réaliser.

**Déroulement du jeu :**

- Si l'utilisateur accomplit l'action demandée dans le temps imparti, la carte envoie un signal au téléphone indiquant le succès de l'action, en précisant le temps utilisé.
- Si l'utilisateur dépasse le temps imparti, la carte signale un échec au téléphone, en mentionnant la durée écoulée.

L’utilisateur possède un nombre de vies et la durée du temps imparti adaptées au Mode (Facile : 3 vies - 5s / Moyen : 2 vies - 3s / Difficile : 1 vie - 2s )

**Fin du jeu :**

- Si l’utilisateur possède encore des vies OU ne réalise aucune erreurs (*Cas Difficile*), alors il remporte la partie. Ces données de jeu sont envoyées au serveur.
- Si l’utilisateur perd toutes ses vies, alors la partie s’arrête et c’est une défaite. Ces données de jeu sont envoyées au serveur.

### Description Fonctionnelle

Cette application à pour but d'être l'application **Logique** au sein de l'environnement BopIoT.

Les fonctionnalités intégrées sont :
* Création de routes HTTP
    * create : Création d'une partie
    * start : Lancement d'une partie
    * settings : Permet d'activer / désactiver des actions disponibles
    * actions : retourne les actions activées disponibles
    * retrive : Récupère les informations d'une partie
* Connexion au Broker MQTT public
* Envoie / Lecture de messages MQTT aux machines concernées (Mobile, IoT, etc.)


## Stack technique 

* Kotlin (openjdk-22)
* Quarkus (3.16.3)
* Protocole MQTT (org.eclipse.paho.client.mqttv3)
* Instanciation de BDD Locale (quarkus.datasource) => Not Done

Le serveur possède 2 controlleurs :
* Un **controlleur REST (GameController)**  possédant chaque route appelable.
* Un **controlleur** en tant que **Singleton (MessageController)** responsable de l'envoie / réception des messages MQTT

Chaque models est représenté au format de **Data Class**.



## Installation & Lancement 

Pour procéder à la bonne installation du projet, nous utilisons l'outil de gestion de dépendances **Graddle**.

_Retrouvez les différentes libraires utilisées dans le fichier build.gradle._


Pour installer les dépendances, placer vous à la racine du projet

_Exemple_
```
C:\Program Files\appbopiotback
```

Exécutez la comande : 
```shell script
./gradlew quarkusDev
```

Cette commande installe les dépendances et lance l'application en mode _Dev_.


## Evolutions possibles 

* Stockage des données grâce à une base de données
  * L'idée serait de stocker les données récupérées dans une base de données afin de pouvoir proposer d'autre fonctionnalités telles que : 
    * Lire les scores 
    * Ajouter des scores
    * Supprimer certaines performances.
* Parties en multijoueur 
  * La logique de jeu est prévue pour un joueur actuellement. 
  * L'idée serait de créer une logique de partie à 2 dans laquelle chaque joueur possède un objet connecté devant lui. Le premier à réaliser la bonne action remporte un point. 




