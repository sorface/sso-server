FROM maven:3.8.5-openjdk-17 as build

COPY . .

RUN mvn clean package -P run.context.production -P module.frontend

FROM openjdk:17.0.2-jdk-slim-buster

ARG JAR_FILE=/web/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]