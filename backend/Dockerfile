# Build stage
FROM gradle:8.13-jdk21 AS build
WORKDIR /app
# Копируем файлы конфигурации Gradle для кэширования зависимостей (если мы решим использовать кэш слоев)
# Но сейчас используем прямой метод с системным Gradle
COPY --chown=gradle:gradle . .
# Используем 'gradle', который уже есть в образе, вместо './gradlew', который качает дистрибутив
RUN gradle :backend:build -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/backend/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]