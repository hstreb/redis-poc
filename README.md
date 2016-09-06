# redis-poc
redis ranking example with spring-data-redis or lettuce in redis cluster

### setup
```
mvn clean package assembly:single
```

```
docker run -d -p 6379:6379 -p 6380:6380 -p 6381:6381 -p 6382:6382 -p 6383:6383 -p 6384:6384 --name redis-cluster vishnunair/docker-redis-cluster
```


### run
```
java -jar target/redis-poc-1.0-jar-with-dependencies.jar
```

### run in different environment {dev, prod}
```
java -jar target/redis-poc-1.0-jar-with-dependencies.jar --spring.profiles.active=dev
```
