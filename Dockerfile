FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
RUN apk add --no-cache ca-certificates && update-ca-certificates
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]