FROM openjdk:21-jdk
LABEL MAINTAINER="adrian@gilbert.ca"
VOLUME /tmp

EXPOSE 8888

COPY build/libs/photo-server-0.0.1-SNAPSHOT.jar /

ENTRYPOINT [ "java", "-jar", "/photo-server-0.0.1-SNAPSHOT.jar" ]
