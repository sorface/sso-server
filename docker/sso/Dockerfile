FROM maven:3.8.5-openjdk-17 as build

COPY . .

RUN mvn clean package -P production

FROM openjdk:17.0.2-jdk-slim-buster

ARG JAR_FILE=/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]