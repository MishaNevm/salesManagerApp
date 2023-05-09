FROM openjdk:17-alpine
ADD target/clientService-0.0.1-SNAPSHOT.jar client-backend
ENTRYPOINT ["java", "-jar", "client-backend.jar"]