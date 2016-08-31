package com.hstreb.redispoc;

import com.hstreb.redispoc.config.AppConfig;
import com.hstreb.redispoc.service.ScoreService;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class App {
    
    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder(AppConfig.class)
                .run(args)
                .getBean(ScoreService.class)
                .run();
    }
}
