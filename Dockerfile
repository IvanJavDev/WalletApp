FROM openjdk:17.0.2-jdk-slim-buster
WORKDIR /app
COPY target/WalletApp-0.0.1-SNAPSHOT.jar /app/myapp.jar
CMD ["java", "-jar", "myapp.jar"]