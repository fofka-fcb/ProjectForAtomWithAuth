FROM maven:3.9.4-eclipse-temurin-17 as build
COPY src src
COPY pom.xml pom.xml
RUN mvn clean package
FROM bellsoft/liberica-openjdk-debian:17
RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot
WORKDIR /app
COPY --from=build target/ProjectForAtomWithAuth-0.0.1-SNAPSHOT.jar /app/myProject.jar
ENTRYPOINT ["java", "-jar", "myProject.jar"]


#FROM bellsoft/liberica-openjdk-debian:17
#RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
#USER spring-boot
#WORKDIR /appdock
#COPY target/ProjectForAtomWithAuth-0.0.1-SNAPSHOT.jar /app/myProject.jar
#ENTRYPOINT ["java", "-jar", "myProject.jar"]

#FROM openjdk:17-jdk-slim-buster
#WORKDIR /app
#COPY target/ProjectForAtomWithAuth-0.0.1-SNAPSHOT.jar /app/myProject.jar
#ENTRYPOINT ["java", "-jar", "myProject.jar"]