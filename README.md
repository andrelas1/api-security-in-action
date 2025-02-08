# Api Security In Action - Theory Application 

This repository contains the code that is implemented in the Api Security in Action book. However, the book uses Java Spark, but I don't want to use it. I am implementing the code examples in Spring instead of Spark.

## Project Setup

### Hot reload
> `gradle build --continuous` -> every time code changes in the classpath, gradle will rerun the build command.
> `gradle bootRun` with `spring-boot-devtools` -> every time the build artifact changes, the Spring Boot DevTools will restart the application. Thus, that's why the Gradle build with that `continuous` flag is important, because it reruns the build and then the devtools can restart the application.

In `application.properties`, these are useful options:
```
spring.devtools.restart.enabled=true
spring.devtools.restart.exclude=static/**,public/**
```

### Bean configuration
This project uses explicit component scanning. This is in place so that I have a bit more control of this application.