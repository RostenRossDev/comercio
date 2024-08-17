FROM openjdk:21-jdk-slim

MAINTAINER RostenRoss

COPY target/app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=dev"]