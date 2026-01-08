# Build stage
FROM gradle:8.13-jdk21-jammy AS builder
WORKDIR /app

COPY settings.gradle ./

COPY gradle ./gradle

RUN gradle :backend:dependencies --no-daemon || true

COPY backend ./backend

RUN gradle :backend:bootJar -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
EXPOSE 8080

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser

COPY --from=builder /app/backend/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
