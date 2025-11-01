# Kafka Streams Uppercase Transformer

## Démarrage Rapide

### Prérequis
- Docker & Docker Compose
- Java 17+ (pour développement local)
- Maven 3.6+ (pour développement local)

### Lancer avec Docker

```bash
# Cloner le projet
git clone https://github.com/nntran/kafka-samples
cd kafka-samples/kafka-streams-uppercase

# Démarrer tous les services
docker compose up -d

# Voir les logs de l'application
docker compose logs -f kafka-streams-uppercase

# Accéder à Kafka UI
open http://kafka.localhost
```

### Test de l'application

```bash
# Produire un message
docker exec -it kafka kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic input-topic

# Consommer les résultats
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic output-topic \
  --from-beginning
```

### Développement Local

```bash
# Build
mvn clean package

# Run
java -jar target/kafka-streams-uppercase.jar

# Tests
mvn test
```

## Monitoring

- **Kafka UI**: http://localhost:8080
- **JMX**: Port 9101

## Commandes utiles

```bash
# Arrêter les services
docker compose down

# Nettoyer volumes
docker compose down -v

# Rebuild l'application
docker compose up -d --build kafka-streams-uppercase

# Voir les topics
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092
```
