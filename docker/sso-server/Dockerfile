FROM maven:3.8.5-openjdk-17 as build

ARG REACT_APP_BACKEND_URL
ARG REACT_APP_VERSION

ENV REACT_APP_BACKEND_URL=http://localhot:8080
ENV REACT_APP_VERSION=1.0.0

COPY . .

RUN mvn clean package

FROM openjdk:17.0.2-jdk-slim-buster

ARG JAR_FILE=/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]