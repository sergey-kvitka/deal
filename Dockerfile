FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/*.jar
ADD JAR_FILE deal.jar
ENTRYPOINT ["java", "-jar", "/deal.jar"]