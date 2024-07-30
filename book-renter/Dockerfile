# Use a Maven base image for the build stage
FROM maven:3.9.8-amazoncorretto-17 AS build

WORKDIR /app

# Copy the Maven wrapper and pom.xml files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Ensure the mvnw script has executable permissions and correct line endings
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

COPY src ./src
RUN ./mvnw clean package -DskipTests

# Use an Amazon Corretto base image for the runtime stage
FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/target/book-renter-0.0.1-SNAPSHOT.jar /app/book-renter.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/book-renter.jar"]
