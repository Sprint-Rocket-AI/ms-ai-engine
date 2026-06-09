# ── Stage 1: Build ──────────────────────────────────────────
# Usamos eclipse-temurin:25-jdk para coincidir con java.version=25 del pom.xml
# y el Maven Wrapper (./mvnw) del propio proyecto en lugar de la imagen maven:3.9
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

# Copiar Maven Wrapper primero para cachear la descarga de Maven
COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw

# Descargar dependencias (capa cacheada separada del código fuente)
COPY pom.xml .
RUN ./mvnw dependency:go-offline -q

# Compilar y empaquetar
COPY src ./src
RUN ./mvnw package -DskipTests -q

# ── Stage 2: Runtime ─────────────────────────────────────────
FROM eclipse-temurin:25-jre
WORKDIR /app

LABEL maintainer="SpringRocket IA"
LABEL service="ms-ai-engine"

RUN groupadd -r spring && useradd -r -g spring spring
USER spring

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
