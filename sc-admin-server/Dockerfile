FROM java:alpine
VOLUME /tmp
ADD sc-eureka-server-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]