FROM openjdk:22-jdk
COPY target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]