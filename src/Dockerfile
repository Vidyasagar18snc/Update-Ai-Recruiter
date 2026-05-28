
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPdocker build -t vendor-app .OINT ["java", "-jar", "app.jar"]