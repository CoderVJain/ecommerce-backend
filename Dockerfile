# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copy only pom.xml first (for caching)
COPY pom.xml .
# Copy source code
COPY src ./src
# Build the jar
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app
# Copy the built jar
COPY --from=build /app/target/E-CommerceSpring-0.0.1-SNAPSHOT.jar E-CommerceSpring.jar

# Railway dynamic port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "E-CommerceSpring.jar"]
