FROM eclipse-temurin:17-jdk-alpine
ADD target/*.jar deal.jar
ENTRYPOINT ["java", "-jar", "/deal.jar"]
