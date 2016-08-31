package com.hstreb.redispoc.service;

import com.hstreb.redispoc.repository.ScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreService.class);

    @Value("${config.salute}")
    private String salute;

    @Autowired
    private ScoreRepository repository;

    public void run() {
        LOGGER.info(salute);
        repository.removeAll();
        add("Second", 9.0);
        add("First", 10.0);
        list();
        add("Wrong", 9.1);
        list();
        add("Second", 9.5);
        list();
        repository.updateKey("Wrong", "Thrid");
        list();
        LOGGER.info("'Second' position: " + (repository.rank("Second") + 1));
    }

    private void add(String second, double value) {
        repository.add(second, value);
    }

    private void list() {
        LOGGER.info("RANKING: " + repository.list(-1L, 0L));
    }
}
