# Stage 1: Build the application
FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app

# Copy pom.xml first for caching
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the built jar and rename for simplicity
COPY --from=build /app/target/E-CommerceSpring-0.0.1-SNAPSHOT.jar application.jar

# Railway dynamic port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
