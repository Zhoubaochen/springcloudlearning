# springcloudlearning
for spring cloud microservices learning[2018-02-05,shenzhen]<br>
do with the [cloud.spring.io](cloud.spring.io),include CI/CD with Jenkins,deploy as image with Docker.

### 前提：
本项目工程使用java1.8,maven3.3
## sc-eureka-server
这是微服务注册中心，只需要加入Eureka-Server的依赖，在Spring Boot主程序添加一个`@EnableEurekaServer`即可。</br>
主要是配置项，包括pom.xml,application.yml,Jenkinsfile,Dockerfile.
```java
@EnableEurekaServer
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

```
在maven的pom.xml文件添加相应依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.example</groupId>
	<artifactId>sc-eureka-server</artifactId>
	<version>0.1.0</version>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.3.RELEASE</version>
	</parent>

	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-eureka-server</artifactId>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>Edgware.RELEASE</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
	<properties>
		<java.version>1.8</java.version>
	</properties>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>

```
若需要打包成docker镜像，则可以引入一个[maven插件](https://github.com/spotify/docker-maven-plugin)

> **注意:** 对于使用win7环境下的[Docker Toolbox](https://docs.docker.com/toolbox/)的用户要有以下\<dockerHost>和\<dockerCertPath>的配置，否则会报错`com.spotify.docker.client.shaded.javax.ws.rs.ProcessingException`.
---
使用`mvn clean package docker:build`打包镜像到Docker仓库。成功后可以`docker run -p 8761:8761 sc-eureka-server`启动运行。
```xml
<plugin>
	<!--详见：https://github.com/spotify/docker-maven-plugin-->
       <groupId>com.spotify</groupId>
       <artifactId>docker-maven-plugin</artifactId>
       <version>0.4.13</version>
       <configuration>
           <!-- win7toolbox需要配置以下两行 -->
           <!-- 插件默认使用localhost:2375去连接Docker如果Docker端口不是2375需要配置环境变量DOCKER_HOST=tcp://<host>:2375 -->
           <dockerHost>https://192.168.99.100:2376</dockerHost>
           <dockerCertPath>C:\Users\Administrator\.docker\machine\machines\default</dockerCertPath>
           <!-- 注意imageName要是符合正则[a-z0-9-_.]的否则构建不会成功 -->
           <imageName>${project.name}:${project.version}</imageName>
           <baseImage>java:alpine</baseImage>
           <entryPoint>["java", "-jar", "/${project.build.finalName}.jar"]</entryPoint>
           <resources>
               <resource>
                   <targetPath>/</targetPath>
                   <directory>${project.build.directory}</directory>
                   <include>${project.build.finalName}.jar</include>
               </resource>
           </resources>
           <!-- 以上等价于/src/main/docker/Dockerfile:
             FROM java
             ADD /sc-eureka-server-0.1.0.jar //
             ENTRYPOINT java -jar /sc-eureka-server-0.1.0.jar
             指定Dockerfile所在的路径 
             <dockerDirectory>${project.basedir}/src/main/docker</dockerDirectory>
           -->
           <!-- 发布到镜像私库，serverId在maven配置文件settings.xml一致 
            <imageName>镜像私库地址/${project.name}:${project.version}</imageName>
            <serverId>docker-registry</serverId>
            -->
            <!-- 以下两行是为了docker push到DockerHub使用的。 
           <serverId>docker-hub</serverId>
           <registryUrl>https://index.docker.io/v1/</registryUrl>
           -->
       </configuration>
</plugin>
```
上面的片段相当于Dockerfile
```xml
FROM java:alpine
VOLUME /tmp
ADD sc-eureka-server-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]
```

