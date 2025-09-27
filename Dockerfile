# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar application.jar

# Railway exposes a dynamic $PORT, so we just EXPOSE 8080 (default for Spring Boot)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
