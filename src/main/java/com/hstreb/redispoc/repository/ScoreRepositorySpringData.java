package com.hstreb.redispoc.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import com.hstreb.redispoc.model.Score;
import com.hstreb.redispoc.model.ScoreKey;
import com.hstreb.redispoc.parser.ScoreKeyParser;

@Repository
public class ScoreRepositorySpringData implements ScoreRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreRepositorySpringData.class);

    public static final String KEY = "score";

    @Autowired
    private RedisTemplate<String, String> template;

    @Autowired
    private ScoreKeyParser scoreKeyParser;
    
    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = template.opsForZSet();
    }

    @Override
    public List<Score> list(Long limit, Long offset) {
        return zSetOperations.reverseRangeWithScores(KEY, offset, limit)
                .stream()
                .map(t -> new Score(scoreKeyParser.parse(t.getValue()), t.getScore()))
                .collect(Collectors.toList());
    }

    @Override
    public void add(Score score) {
        zSetOperations.add(KEY, scoreKeyParser.parse(score.getKey()), score.getScore());
    }

    @Override
    public void updateKey(ScoreKey oldKey, ScoreKey newKey) {
        Double score = zSetOperations.score(KEY, scoreKeyParser.parse(oldKey));
        updateKey(oldKey, newKey, score);
    }

    @Override
    public void updateKey(ScoreKey oldScoreKey, ScoreKey newScoreKey, Double value) {
        zSetOperations.remove(KEY, scoreKeyParser.parse(oldScoreKey));
        zSetOperations.add(KEY, scoreKeyParser.parse(newScoreKey), value);
    }

    @Override
    public Long rank(ScoreKey key) {
        return zSetOperations.rank(KEY, scoreKeyParser.parse(key));
    }

    @Override
    public void removeAll() {
        zSetOperations.removeRange(KEY, 0, -1);
    }

}
