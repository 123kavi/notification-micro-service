FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-alpine-slim
ENV PORT 8080
EXPOSE 8080
#COPY target/*.jar /opt/springboot-docker-demo.jar
ADD target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]