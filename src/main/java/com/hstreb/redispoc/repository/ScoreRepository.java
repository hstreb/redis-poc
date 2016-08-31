package com.hstreb.redispoc.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hstreb.redispoc.model.Score;
import com.hstreb.redispoc.model.ScoreKey;
import com.hstreb.redispoc.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ScoreRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreRepository.class);

    public static final String KEY = "score";

    @Autowired
    private RedisTemplate<String, String> template;

    private ObjectMapper mapper = new ObjectMapper();
    
    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = template.opsForZSet();
    }

    public List<Score> list(Long limit, Long offset) {
        return zSetOperations.reverseRangeWithScores(KEY, offset, limit)
                .stream()
                .map(this::getScore)
                .collect(Collectors.toList());
    }

    private Score getScore(ZSetOperations.TypedTuple<String> t) {
        return new Score(getScoreKey(t),  t.getScore());
    }

    public void add(Score score) {
        zSetOperations.add(KEY, getStringKey(score.getKey()), score.getScore());
    }

    public void updateKey(ScoreKey oldKey, ScoreKey newKey) {
        Double score = zSetOperations.score(KEY, getStringKey(oldKey));
        updateKey(oldKey, newKey, score);
    }

    public void updateKey(ScoreKey oldScoreKey, ScoreKey newScoreKey, Double value) {
        zSetOperations.remove(KEY, getStringKey(oldScoreKey));
        zSetOperations.add(KEY, getStringKey(newScoreKey), value);
    }

    public Long rank(ScoreKey key) {
        return zSetOperations.rank(KEY, getStringKey(key));
    }

    public void removeAll() {
        zSetOperations.removeRange(KEY, 0, -1);
    }

    private ScoreKey getScoreKey(ZSetOperations.TypedTuple<String> t) {
        try {
            return mapper.readValue(t.getValue(), ScoreKey.class);
        } catch (IOException e) {
            LOGGER.error("Error on get score from json. {}", t.getValue(), e);
        }
        return null;
    }

    private String getStringKey(ScoreKey oldScoreKey) {
        try {
            return mapper.writeValueAsString(oldScoreKey);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error on write score to json. {}", oldScoreKey, e);
        }
        return null;
    }

}
