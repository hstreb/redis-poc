package com.hstreb.redispoc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        RedisRepository repository = ctx.getBean(RedisRepository.class);

        repository.removeAll();

        repository.add("Second", 9.0);
        repository.add("First", 10.0);

        System.out.println("RANKING: " + repository.list(-1L, 0L));

        repository.add("Wrong", 9.1);

        System.out.println("RANKING: " + repository.list(-1L, 0L));

        repository.add("Second", 9.5);

        System.out.println("RANKING: " + repository.list(-1L, 0L));

        repository.updateKey("Wrong", "Thrid");

        System.out.println("RANKING: " + repository.list(-1L, 0L));

        System.out.println("'Second' position: " + (repository.rank("Second") + 1));
    }
}
