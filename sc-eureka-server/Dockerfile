FROM java:openjdk-8u40
MAINTAINER dflemstr <dflemstr@spotify.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/myservice/sc-eureka-server-0.1.0.jar"]

ADD target/sc-eureka-server-0.1.0.jar /usr/share/myservice/sc-eureka-server-0.1.0.jar
