# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
COPY --from=build target/E-CommerceSpring-0.0.1-SNAPSHOT.jar E-CommerceSpring.jar

# Railway exposes a dynamic $PORT
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "E-CommerceSpring.jar"]
