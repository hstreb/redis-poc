package com.hstreb.redispoc;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class App {
    
    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder(AppConfig.class)
                .run(args)
                .getBean(ScoreService.class)
                .run();
    }
}
