[![Project Website](https://img.shields.io/badge/Project%20Website-atelier--spring--kafka-informational)](https://opt-nc.github.io/atelier-spring-kafka/) [![Confluent tutorial](https://img.shields.io/badge/Confluent%20tutorial-Best%20Tutorials%20for%20Getting%20Started%20with%20Apache%20Kafka-blue)](https://www.confluent.io/blog/best-tutorials-for-getting-started-with-apache-kafka/)


# ❔ A propos

Ce projet explique comment développer un producer/consumer sans la lib interne historique, 
au profit de celle [développée et maintenue par Spring](https://spring.io/projects/spring-kafka).

# ✋Pourquoi Kafka

![image](https://user-images.githubusercontent.com/5235127/220445922-ec89e56c-6880-4b79-b8f3-5221142d1dee.png)


## 📝 Description

Projet d'exemple d'utilisation de la lib [`org.springframework.kafka:spring-kafka`](https://spring.io/projects/spring-kafka)

## ✅ Pré-requis

- Instance `kafka`
- `docker` & `docker-compose` (si utilisation de `kafka.yml`)

## 👶 Kafka ?! WTF ❔

Avant de passer à la suite, prendre soin de consulter les ressources ci-dessous:

- 🎥 [Comprendre ce qu'est Kafka en 6 minutes](https://youtu.be/Ch5VhJzaoaI)
- 📝 [The Apache Kafka Handbook (freecodecamp)](https://www.freecodecamp.org/news/apache-kafka-handbook/)

## :student: Supports custome

- [⌨️ Pipe xlsx files into/from Kafka... From cli with (k)cat 🙀](https://dev.to/optnc/pipe-xlsx-files-intofrom-kafka-from-cli-with-kcat-plp)

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

Copier les lignes :

```
112233|{"phoneNumber":"112233", "firstName":"Hubert", "lastName":"Bonisseur de la Bath"}
998877|{"phoneNumber":"998877", "firstName":"Jean", "lastName":"Soudajman"}
446655|{"phoneNumber":"446655", "firstName":"Henri", "lastName":"Tathan"}
```

### 2️⃣ Ecriture automatique dans le topic `demo.sms`

**Initialiser le topic `demo.user` par les scripts ci-dessus au préalable**

Le script `sendSMSDaemon.sh` *(ressources/pre-init)* envoie toutes les 2 secondes un `curl` de type *POST* à l'endpoint `kafka/sms/send`

**Important : Nécessite la commande `jq`**


## Recommandations

Dernièrement une passe d'alignement des conf des producers et consumers a été opérée par le GLIA, car beaucoup de 
projets ont été développés par copier/coller par peur de mal faire. 

Voici quelques recommendations :
- `acks=all` : c'est maintenant la valeur par défaut, donc inutile de positionner
- `retries=1` : la valeur par défaut est maintenant énorme pour faire des retries quasi indéfiniment, donc inutile de positionner
- `max.in.flight.requests.per.connection=1` : il s'agissait de garantir l'ordre des messages en cas de retry. Je pense que ce [besoin](https://www.confluent.io/blog/exactly-once-semantics-are-possible-heres-how-apache-kafka-does-it/) est rare et que cette conf ne doit pas être généralisée car on n'a pas la valeur par défaut (5 aujourd'hui)
- `max.request.size`>valeur par défaut : ce besoin est réel sur de très rares cas comme le 'producer-otrs' étant donné que l'on a mis les pièces jointes dans le message kafka, mais c'est très rare et doit être supprimé quand ce n'est pas nécessaire car comme toute limite configurable, c'est un garde fou et le fait de le configurer alors que ce n'est pas nécessaire peut induire en erreur
- `activer les logs Kafka en INFO` : par défaut elles sont actives mais on les a désactivés dans pas mal de projets. Aujourd'hui elles manquent cruellement et elles ne sont pas si fréquentes
- utiliser le plus possible la configuration via le fichier de conf yaml.

## 🔖 Liens utiles

### 🧰 Tools

- [Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/html/)
- [`docker`](https://docs.docker.com/get-docker/)
- [`docker-compose`](https://docs.docker.com/compose/)
- [`jq`](https://stedolan.github.io/jq/)

### 📝 Articles recommandés

- [Migraton vers l'image `Bitnami`](https://dev.to/optnc/kafka-image-wurstmeister-vs-bitnami-efg)
- [DZONE Article : What Is Kafka? Everything You Need to Know](https://dzone.com/articles/what-is-kafka?edition=738693)
- [A comparison of ActiveMQ and Kafka](https://developers.redhat.com/articles/2023/02/16/comparison-activemq-and-kafka)
- [Google Cloud Community :: Application Integration: Integration over Kafka](https://www.googlecloudcommunity.com/gc/Integration-Services/Application-Integration-Integration-over-Kafka/m-p/523518/thread-id/105)
