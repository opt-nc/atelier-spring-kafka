[![Project Website](https://img.shields.io/badge/Project%20Website-atelier--spring--kafka-informational)](https://opt-nc.github.io/atelier-spring-kafka/)

# atelier spring kafka 

Comment développer un producer/consumer sans la lib opt-kafka

## Description

Projet d'exemple d'utilisation de la lib `org.springframework.kafka:spring-kafka`

## Pré-requis

* Instance `kafka`
* `docker` & `docker-compose` (si utilisation de kafka.yml)

## Scripts

### Initialiser le topic `demo.user`

Entrer dans le container `kafka`

```
docker exec -ti kafka bash
```

Créer le producer

```
kafka-console-producer.sh --broker-list kafka:9092 --topic demo.user --property "parse.key=true" --property "key.separator=|"
```

Copier les lignes *(inclure la dernière ligne vide)*
```
112233|{"phoneNumber":"112233", "firstName":"Hubert", "lastName":"Bonisseur de la Bath"}
998877|{"phoneNumber":"998877", "firstName":"Jean", "lastName":"Soudajman"}
446655|{"phoneNumber":"446655", "firstName":"Henri", "lastName":"Tathan"}

```

### Ecriture automatique dans le topic `demo.sms`

**Initialiser le topic `demo.user` par les scripts ci-dessus au préalable**

Le script `sendSMSDaemon.sh` *(ressources/pre-init)* envoie toutes les 2 secondes un `curl` de type *POST* à l'endpoint `kafka/sms/send`

**Important : Nécessite la commande `jq`**

## Liens utiles

* https://docs.spring.io/spring-kafka/reference/html/
* https://docs.docker.com/get-docker/
* https://docs.docker.com/compose/
* https://stedolan.github.io/jq/
