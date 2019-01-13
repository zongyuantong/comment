FROM java:8

ADD target/*.jar /app.jar

VOLUME /tmp

EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]