# 📡 Vehicle GPS Tracker & Geofencing Backend

Backend de géolocalisation et de sécurité automobile développé avec **Spring Boot 3** et **PostGIS**.

Ce système permet de recevoir en temps réel les trames GPS d'un véhicule (via l'application [Traccar Client](https://www.traccar.org/client/) ou un boîtier OBD/IoT commercial), de stocker l'historique des positions dans une base de données spatiale, et de détecter les mouvements non autorisés (*Geofencing*) pour prévenir les vols.

---

## 🛠️ Stack Technique

* **Langage & Framework :** Java 26, Spring Boot 4
* **Base de données spatiale :** PostgreSQL 18 + PostGIS 3.5 (via Docker)
* **ORM & SIG :** Spring Data JPA, Hibernate Spatial, JTS (Java Topology Suite)
* **Protocole d'entrée :** HTTP REST (`GET` / `POST` compatible Traccar Client / OsmAnd)
* **Containerisation :** Docker & Docker Compose

---

## 🚀 Fonctionnalités

- 📍 **Réception des données GPS :** Prise en charge des trames HTTP transmises par Traccar Client (Latitude, Longitude, Vitesse, Batterie, Timestamp).
- 🧭 **Stockage Spatial PostGIS :** Indexation des positions géographiques sous forme de points `POINT(SRID 4326)` optimisés pour les requêtes spatiales.
- 🚨 **Détection de vol (Geofencing) :** Calcul de distance en mètres sur la sphère terrestre (`ST_DWithin` sur type `geography`) pour vérifier si le véhicule quitte son périmètre de sécurité.
- 🔐 **Sécurité & Confidentialité :** Externalisation complète des coordonnées GPS sensibles et secrets via des variables d'environnement.

---

## 📂 Architecture du Projet

```text
src/main/java/com/yanndub/gpstracker/
├── controller/
│   └── GpsController.java         # Endpoint HTTP d'acquisition des données
├── dto/
│   └── TraccarPayload.java        # DTO d'entrée pour le mapping des trames
├── entities/
│   └── GpsPosition.java           # Entité JPA avec mapping spatial JTS/PostGIS
├── repositories/
│   └── GpsPositionRepository.java # Requêtes SQL natives PostGIS (ST_DWithin)
└── services/
    └── GpsService.java            # Traitement métier et vérification du périmètre
```

# 📱 Configuration du Client GPS (Traccar Client)

Pour envoyer les positions depuis votre smartphone vers le serveur :

- Téléchargez Traccar Client (Android / iOS).
- Configurez les paramètres suivants :
  - URL du serveur : http://<IP_SERVEUR_OU_TUNNEL>:8080/api/gps
  - Identifiant du terminal : votre_device_id (ex: car-01)
  - Fréquence d'envoi : 10 (secondes)
- Activez le bouton Service status.

# 🗺️ Roadmap & Évolutions à venir

- [x] **Notification Push** : Intégration d'un Bot Telegram pour l'envoi d'alertes instantanées avec lien Google Maps.
- [ ] **Dashboard Web** : Interface de suivi en temps réel basée sur Leaflet / OpenStreetMap.
- [x] **Rétention de données** : Tâche planifiée (@Scheduled) pour la purge automatique des anciennes trajectoires.
- [ ] **Protocole TCP/OBD** : Support natif des trames binaires Teltonika / Concox via Netty.