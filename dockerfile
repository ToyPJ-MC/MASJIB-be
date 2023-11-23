FROM openjdk:17

ARG JAVA_OPTS

ENV JAVA_OPTS=$JAVA_OPTS

COPY build/libs/MASJIB-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]