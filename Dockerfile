FROM openjdk:17-jdk-slim-buster
ARG JAR_FILE=target/vending-machine.jar
WORKDIR /opt/app
COPY target/vending-machine-1.0.0.jar /vending-machine-docker.jar
ENTRYPOINT ["java","-jar","/vending-machine-docker.jar"]