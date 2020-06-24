FROM openjdk:8-jdk-alpine
RUN addgroup -S runner && adduser -S runner -G runner
USER runner:runner
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
