# redis-poc
redis ranking example with spring-data-redis

### setup
```
mvn clean package assembly:single
```

### run
```
java -jar target/redis-poc-1.0-jar-with-dependencies.jar
```

### run in different environment {dev, prod}
```
java -jar target/redis-poc-1.0-jar-with-dependencies.jar --spring.profiles.active=dev
```
