FROM gradle:8-jdk21 AS builder
WORKDIR /workspace
COPY --chown=gradle:gradle . /workspace
RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jre-jammy
ARG JAR_FILE=/workspace/build/libs/*.jar
COPY --from=builder ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]