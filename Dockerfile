FROM daocloud.io/java:8

RUN echo "Asia/Shanghai" > /etc/timezone
# 复制jar包
RUN  mv target/*.jar /app.jar

VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]