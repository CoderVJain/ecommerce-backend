# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-jammy AS build
COPY . .
RUN mvm clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/E-CommerceSpring-0.0.1-SNAPSHOT.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]