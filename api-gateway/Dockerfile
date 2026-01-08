# Build stage
FROM gradle:8.13-jdk21-jammy AS builder
WORKDIR /app

COPY settings.gradle ./

COPY gradle ./gradle

RUN gradle :api-gateway:dependencies --no-daemon || true

COPY api-gateway ./api-gateway

RUN gradle :api-gateway:build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
EXPOSE 8082

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser

COPY --from=builder /app/api-gateway/build/libs/api-gateway-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
