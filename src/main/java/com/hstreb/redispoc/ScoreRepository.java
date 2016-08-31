package com.hstreb.redispoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ScoreRepository {

    public static final String KEY = "score";
    
    @Autowired
    private RedisTemplate<String, String> template;
    
    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = template.opsForZSet();
    }

    public List<String> list(Long limit, Long offset) {
        Set<ZSetOperations.TypedTuple<String>> store = zSetOperations.reverseRangeWithScores(KEY, offset, limit);
        return store.stream().map(t -> t.getValue() + " - " + t.getScore()).collect(Collectors.toList());
    }

    public Long rank(String key) {
        return zSetOperations.rank(KEY, key);
    }

    public void add(String key, Double value) {
        zSetOperations.add(KEY, key, value);
    }

    public void updateKey(String oldKey, String newKey) {
        Double score = zSetOperations.score(KEY, oldKey);
        updateKey(oldKey, newKey, score);
    }

    public void updateKey(String oldKey, String newKey, Double value) {
        zSetOperations.remove(KEY, oldKey);
        zSetOperations.add(KEY, newKey, value);
    }

    public void removeAll() {
        zSetOperations.removeRange(KEY, 0 , -1);
    }

}
