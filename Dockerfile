FROM openjdk:11

WORKDIR /application

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]