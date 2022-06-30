[![Project Website](https://img.shields.io/badge/Project%20Website-atelier--spring--kafka-informational)](https://opt-nc.github.io/atelier-spring-kafka/) [![Confluent tutorial](https://img.shields.io/badge/Confluent%20tutorial-Best%20Tutorials%20for%20Getting%20Started%20with%20Apache%20Kafka-blue)](https://www.confluent.io/blog/best-tutorials-for-getting-started-with-apache-kafka/)


# ❔ A propos

Ce projet explique comment développer un producer/consumer sans la lib interne historique, 
au profit de celle [développée et maintenue par Spring](https://spring.io/projects/spring-kafka).

## 📝 Description

Projet d'exemple d'utilisation de la lib [`org.springframework.kafka:spring-kafka`](https://spring.io/projects/spring-kafka)

## ✅ Pré-requis

- Instance `kafka`
- `docker` & `docker-compose` (si utilisation de `kafka.yml`)

## 📜 Scripts

### 1️⃣ Initialiser le topic `demo.user`

Entrer dans le container `kafka` :

```
docker exec -ti kafka bash
```

Créer le producer :

```
kafka-console-producer.sh --broker-list kafka:9092 --topic demo.user --property "parse.key=true" --property "key.separator=|"
```

Copier les lignes **(inclure la dernière ligne vide)** :

```
112233|{"phoneNumber":"112233", "firstName":"Hubert", "lastName":"Bonisseur de la Bath"}
998877|{"phoneNumber":"998877", "firstName":"Jean", "lastName":"Soudajman"}
446655|{"phoneNumber":"446655", "firstName":"Henri", "lastName":"Tathan"}

```

### 2️⃣ Ecriture automatique dans le topic `demo.sms`

**Initialiser le topic `demo.user` par les scripts ci-dessus au préalable**

Le script `sendSMSDaemon.sh` *(ressources/pre-init)* envoie toutes les 2 secondes un `curl` de type *POST* à l'endpoint `kafka/sms/send`

**Important : Nécessite la commande `jq`**

## 🔖 Liens utiles

- [Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/html/)
- [`docker`](https://docs.docker.com/get-docker/)
- [`docker-compose`](https://docs.docker.com/compose/)
- [`jq`](https://stedolan.github.io/jq/)
- [Migraton vers l'image `Bitnami`](https://dev.to/optnc/kafka-image-wurstmeister-vs-bitnami-efg)
- [DZONE Article : What Is Kafka? Everything You Need to Know](https://dzone.com/articles/what-is-kafka?edition=738693)
