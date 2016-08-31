package com.hstreb.redispoc.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hstreb.redispoc.model.Score;
import com.hstreb.redispoc.model.ScoreKey;
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
        try {
            ScoreKey scoreKey = mapper.readValue(t.getValue(), ScoreKey.class);
            return new Score(scoreKey,  t.getScore());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(Score score) {
        try {
            zSetOperations.add(KEY, mapper.writeValueAsString(score.getKey()), score.getScore());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void updateKey(ScoreKey oldKey, ScoreKey newKey) {
        try {
            Double score = zSetOperations.score(KEY, mapper.writeValueAsString(oldKey));
            updateKey(oldKey, newKey, score);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void updateKey(ScoreKey oldKey, ScoreKey newKey, Double value) {
        try {
            zSetOperations.remove(KEY, mapper.writeValueAsString(oldKey));
            zSetOperations.add(KEY, mapper.writeValueAsString(newKey), value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Long rank(ScoreKey key) {
        try {
            return zSetOperations.rank(KEY, mapper.writeValueAsString(key));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeAll() {
        zSetOperations.removeRange(KEY, 0, -1);
    }

}
