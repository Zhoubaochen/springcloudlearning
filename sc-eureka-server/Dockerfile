FROM java:openjdk-8u40
MAINTAINER dflemstr <dflemstr@spotify.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/myservice/myservice.jar"]

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
ADD target/lib           /usr/share/myservice/lib
# Add the service itself
ARG JAR_FILE
ADD target/sc-eureka-server-0.1.0.jar /usr/share/myservice/myservice.jar
