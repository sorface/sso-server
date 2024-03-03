FROM maven:3.8.5-openjdk-17 as build

ARG MAVEN_OPTS="-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn"
ENV MAVEN_OPTS="${MAVEN_OPTS}"

COPY . .

RUN mvn clean package -P production

FROM openjdk:17.0.2-jdk-slim-buster

ARG JAR_FILE=/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]