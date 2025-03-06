FROM openjdk:23-jdk-slim
ARG JAR_FILE=target/*.jar
COPY cambio-service/cambio-service/target/cambio-service-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]