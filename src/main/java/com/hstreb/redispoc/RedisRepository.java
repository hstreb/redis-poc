package com.hstreb.redispoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RedisRepository {

    public static final String COLLECTION_NAME = "store";
    @Autowired
    private RedisTemplate<String, String> template;

    public List<String> list(Long limit, Long offset) {
        Set<ZSetOperations.TypedTuple<String>> store = template.opsForZSet().reverseRangeWithScores(COLLECTION_NAME, offset, limit);
        return store.stream().map(t -> t.getValue() + " - " + t.getScore()).collect(Collectors.toList());
    }

    public Long rank(String key) {
        return template.opsForZSet().rank(COLLECTION_NAME, key);
    }

    public void add(String key, Double value) {
        template.opsForZSet().add(COLLECTION_NAME, key, value);
    }

    public void updateKey(String oldKey, String newKey) {
        Double score = template.opsForZSet().score(COLLECTION_NAME, oldKey);
        updateKey(oldKey, newKey, score);
    }

    public void updateKey(String oldKey, String newKey, Double value) {
        template.opsForZSet().remove(COLLECTION_NAME, oldKey);
        template.opsForZSet().add(COLLECTION_NAME, newKey, value);
    }

    public void removeAll() {
        template.opsForZSet().removeRange(COLLECTION_NAME, 0 , -1);
    }

}
