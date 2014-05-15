# jaxrs-unit

A library for unit testing of JAX-RS application.
 
The goal is to write unit tests without a real JavaEE server. 
But tests use real JAX-RS implementation (Jersey or RestEasy) with in-memory containers. 

JaxRS Unit provides an API to encapsulate the specific im-memory containers.

# Build status

[![Build Status](https://buildhive.cloudbees.com/job/binout/job/jaxrs-unit/badge/icon)](https://buildhive.cloudbees.com/job/binout/job/jaxrs-unit)

# Environment

- `java-1.7`

# Build

```bash
mvn clean verify
```

# Usage


## Maven

Release versions are deployed on Maven Central:

```xml
<dependency>
  <groupId>net.code-story</groupId>
  <artifactId>http</artifactId>
  <version>1.51</version>
</dependency>
```

## Hello World

Starting a web server that responds `Hello World` on `/` uri is as simple as that:

```java
import net.codestory.http.*;

public class HelloWorld {
  public static void main(String[] args) {
    new WebServer(routes -> routes.get("/", "Hello World")).start();
  }
}
```

Adding more routes is not hard either:

```java
new WebServer(routes -> routes.
    get("/", "Hello World").
    get("/Test", "Test").
    get("/OtherTest", "Other Test")
).start();
```