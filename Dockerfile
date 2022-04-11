
FROM openjdk:11-oracle
VOLUME /tmp
COPY build/libs/system_app-1.0.0-SNAPSHOT.jar app.jar
COPY Data Data/
COPY create_tables.sql .
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=Asiz/Shanghai","-Dfile.encoding=UTF-8","-jar","/app.jar"]


# FROM openjdk:8-jdk-alpine
# VOLUME /tmp
# ARG JAR_FILE
# COPY ${JAR_FILE} app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]