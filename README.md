http ![alt tag](https://api.travis-ci.org/phishman3579/java-algorithms-implementation.svg?branch=master)
==============================

This is Apache HttpClient project. 这是一个Spring Boot项目，调用外部接口，支持http、https请求，实现GET、POST、File、DataFlow等请求传输方式。

## Table of Contents
+ [Environment](https://github.com/loveisontheway/http#Environment)
+ [Project](https://github.com/loveisontheway/http#Project)
+ [HTTP&HTTPS](https://github.com/loveisontheway/http#HTTP&HTTPS)

## Environment
+ `JDK:` 1.8+
+ `Tomcat:` 9.0.x
+ `Spring Boot:` 2.3.x
+ `Http Client:` 4.5.x

## Project
| name | description |
| :------ | :------ |
| HttpApplicationTests.java | HttpClient 发送方 |
| HttpController | HttpClient 接收方 |
| https.p12 | 服务端证书 |
| https.crt | 客户端证书 |

## HTTP&HTTPS
在com.muxi.http包下，`HttpApplication.java`类是项目启动类；支持http请求通过代码实现，https请求通过`application.yml`直接配置。
```java
@SpringBootApplication
public class HttpApplication {
    public static void main(String[] args) {
        SpringApplication.run(HttpApplication.class, args);
    }

    @Value("${http.port}")
    private Integer httpPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createHttpConnector());
        return tomcat;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(httpPort);
        return connector;
    }
}
```
```xml
http:
  port: 8600
server:
  port: 8500
  ssl:
    key-store: classpath:https.p12
    key-alias: muxi
    key-store-password: 123456
    key-store-type: PKCS12
```
