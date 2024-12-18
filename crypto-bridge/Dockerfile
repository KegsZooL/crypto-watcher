FROM maven:3.9.9-eclipse-temurin-23 as build
LABEL authors="KegsZooL"

WORKDIR /home/app

COPY ./pom.xml ./
COPY ./crypto-bridge/pom.xml ./crypto-bridge/
COPY ./crypto-bridge/src/ ./crypto-bridge/src
COPY ./dispatcher/pom.xml ./dispatcher/
COPY ./dispatcher/src/ ./dispatcher/src

RUN mvn clean package -DskipTest

FROM openjdk:23

ENV HOME=/home/app
WORKDIR $HOME
COPY --from=build $HOME/crypto-bridge/target/crypto-bridge-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "crypto-bridge-0.0.1-SNAPSHOT.jar"]