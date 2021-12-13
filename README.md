# Squizzy

## Description

Ce projet reprend la base du tutoriel d'OpenClassRooms et a été amélioré. 

## Ressources

- [Figma - Mock-Up](https://www.figma.com/file/gavlmDGEp0TXDZ8yTg8SeL/Untitled?node-id=0%3A1e)
- [OpenClassRoom](https://openclassrooms.com/fr/courses/4517166-developpez-votre-premiere-application-android/7298600-creez-une-seconde-activite)

Accès à la plateforme API : android.bperrin.fr

## Deploiement

Le deploiement utilise docker-compose (https://docs.docker.com/compose/), il est nécéssaire de l'avoir sur le système.

Effectuer ces commandes:
```
#On build les images
docker-compose build --pull --no-cache

#On met en place les secrets de l'application
export SERVER_NAME=VOTRE_NOM_DE_DOMAINE
export APP_SECRET=$(openssl rand -base64 32)
export POSTGRES_PASSWORD=$(openssl rand -base64 32)
export CADDY_MERCURE_JWT_SECRET=$(openssl rand -base64 32)
export MERCURE_JWT_SECRET=$(openssl rand -base64 32)
export MERCURE_PUBLISHER_JWT_KEY=$(openssl rand -base64 32)
export MERCURE_SUBSCRIBER_JWT_KEY=$(openssl rand -base64 32)

#On lance les dockers
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

#On génère les certificats pour les JWT
docker-compose exec php sh -c '
    set -e
    apk add openssl
    php bin/console lexik:jwt:generate-keypair
    setfacl -R -m u:www-data:rX -m u:"$(whoami)":rwX config/jwt
    setfacl -dR -m u:www-data:rX -m u:"$(whoami)":rwX config/jwt
'
```

Avec ces commandes, le serveur est maintenant en ligne.
