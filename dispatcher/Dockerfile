FROM maven:3.9.9-eclipse-temurin-23 as build
LABEL authors="KegsZooL"

WORKDIR /home/app

COPY ./pom.xml ./
COPY ./dispatcher/pom.xml ./dispatcher/
COPY ./dispatcher/src/ ./dispatcher/src
COPY ./crypto-bridge/pom.xml ./crypto-bridge/
COPY ./crypto-bridge/src/ ./crypto-bridge/src

RUN mvn clean package -DskipTest

FROM openjdk:23

ENV HOME=/home/app
WORKDIR $HOME
COPY --from=build $HOME/dispatcher/target/dispatcher-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "dispatcher-0.0.1-SNAPSHOT.jar"]