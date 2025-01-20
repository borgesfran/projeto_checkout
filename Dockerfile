FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/projeto_checkout-0.0.1-SNAPSHOT.jar projeto_checkout.jar

ENTRYPOINT ["java", "-jar", "projeto_checkout.jar"]

EXPOSE 8080
