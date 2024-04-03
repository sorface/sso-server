FROM maven:3.8.7-eclipse-temurin-19 as build

COPY . .

ARG ACTIVE_PROFILE=production

RUN mvn clean package -P run.context.$ACTIVE_PROFILE

FROM eclipse-temurin:19

ARG JAR_FILE=/web/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]