# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .

# Télécharger les dépendances
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Build l'application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Créer un utilisateur non-root
RUN addgroup -S flexwiz && adduser -S flexwiz -G flexwiz

# Copier le JAR depuis le stage de build
COPY --from=builder /app/target/kafka-streams-uppercase.jar kafka-streams-uppercase.jar

# Changer l'ownership
RUN chown -R flexwiz:flexwiz /app

# Utiliser l'utilisateur non-root
USER flexwiz

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD pgrep -f "kafka-streams-uppercase.jar" || exit 1

# Point d'entrée
ENTRYPOINT ["java", "-jar", "kafka-streams-uppercase.jar"]
