#!/bin/bash
./mvnw clean package -DskipTests
java -jar target/E-CommerceSpring-0.0.1-SNAPSHOT.jar
