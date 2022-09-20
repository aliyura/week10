FROM openjdk:11
RUN touch /env.txt
RUN printenv > /env.txt
MAINTAINER week10
COPY target/oxygen-0.0.1-SNAPSHOT.jar oxygen-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/oxygen-0.0.1-SNAPSHOT.jar"]