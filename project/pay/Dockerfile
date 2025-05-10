FROM openjdk:21-jdk-slim

WORKDIR /app
COPY project/pay/build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]