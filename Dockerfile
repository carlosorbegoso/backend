FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
docker build --build-arg JAR_FILE=build/libs/*.jar -t myorg/myapp .
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]