FROM maven:3.8.7-eclipse-temurin-19 as build

COPY . .

ARG ACTIVE_PROFILE=production
ARG REACT_APP_BACKEND_URL=$REACT_APP_BACKEND_URL
ARG REACT_APP_VERSION=$REACT_APP_VERSION

RUN mvn clean package \
     -P run.context.$ACTIVE_PROFILE  \
     -P module.frontend  \
     -D REACT_APP_BACKEND_URL=$REACT_APP_BACKEND_URL  \
     -D REACT_APP_VERSION=$REACT_APP_VERSION

FROM eclipse-temurin:19

ARG JAR_FILE=/web/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]