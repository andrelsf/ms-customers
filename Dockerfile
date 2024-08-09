FROM maven:3.9.8-eclipse-temurin-21-alpine as builder
WORKDIR /build
ADD pom.xml .
RUN mvn dependency:go-offline
ADD src/ ./src/
RUN mvn clean package

FROM eclipse-temurin:21.0.4_7-jdk-alpine as dev

ENV TZ=America/Sao_Paulo
RUN mkdir /api
WORKDIR /api

RUN addgroup -S spring \
    && adduser -S spring -G spring

RUN apk add --upgrade apk-tools \
    && apk upgrade --available

RUN apk --update add tzdata \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

USER spring:spring
COPY --from=builder /build/target/*.jar /api/ms-api-customers.jar

EXPOSE 8091
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/api/ms-api-customers.jar"]