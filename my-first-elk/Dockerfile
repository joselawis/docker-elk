FROM openjdk:11-jre
MAINTAINER Jose Luis Contreras <joseluis.conteras@altia.es>

VOLUME /tmp

COPY target/elk-1.0.0.jar elk-image-1.0.0.jar
ENTRYPOINT ["java","-jar","/elk-image-1.0.0.jar"]