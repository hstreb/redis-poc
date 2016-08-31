package com.hstreb.redispoc.service;

import com.hstreb.redispoc.model.Score;
import com.hstreb.redispoc.model.ScoreKey;
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
        ScoreKey first = new ScoreKey("1", "First", false);
        ScoreKey second = new ScoreKey("2", "Second", false);
        ScoreKey thrid = new ScoreKey("3", "Thrird", false);
        repository.add(new Score(second, 9.0));
        repository.add(new Score(first, 10.0));
        list();
        repository.add(new Score(thrid, 9.1));
        list();
        repository.add(new Score(second, 9.5));
        list();
        ScoreKey thridPro = new ScoreKey(thrid.getId(), thrid.getName(), true);
        repository.updateKey(thrid, thridPro);
        list();
        LOGGER.info("'Second' position: " + (repository.rank(second) + 1));
    }

    private void list() {
        LOGGER.info("RANKING: " + repository.list(-1L, 0L));
    }
}
