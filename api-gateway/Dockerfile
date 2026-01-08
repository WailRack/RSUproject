# Build stage
FROM gradle:8.13-jdk21 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle :api-gateway:build -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/api-gateway/build/libs/api-gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]