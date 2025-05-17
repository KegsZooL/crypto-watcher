FROM maven:3.9.9-eclipse-temurin-23 as build
LABEL authors="KegsZooL"

WORKDIR /home/app
COPY . .

RUN mvn clean package -pl dispatcher -am -DskipTests

FROM openjdk:23

WORKDIR /app
COPY --from=build /home/app/dispatcher/target/dispatcher-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "dispatcher-0.0.1-SNAPSHOT.jar"]