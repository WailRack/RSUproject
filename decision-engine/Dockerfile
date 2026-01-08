# Build stage
FROM gradle:8.13-jdk21-jammy AS builder
WORKDIR /app

COPY settings.gradle ./

COPY gradle ./gradle

RUN gradle :decision-engine:dependencies --no-daemon || true

COPY decision-engine ./decision-engine

RUN gradle :decision-engine:build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
EXPOSE 8081

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser

COPY --from=builder /app/decision-engine/build/libs/decision-engine-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
