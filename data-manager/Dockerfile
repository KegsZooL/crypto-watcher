FROM maven:3.9.9-eclipse-temurin-23 as build
LABEL authors="KegsZooL"

WORKDIR /home/app
COPY ../.. .

RUN mvn clean package -pl data-manager -am -DskipTests

FROM openjdk:23

WORKDIR /app
COPY --from=build /home/app/data-manager/target/data-manager-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "data-manager-0.0.1-SNAPSHOT.jar"]
