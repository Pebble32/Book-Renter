FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/book-renter-0.0.1-SNAPSHOT.jar /app/book-renter.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/book-renter.jar"]
