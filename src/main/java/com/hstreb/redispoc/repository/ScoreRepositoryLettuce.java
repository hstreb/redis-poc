package com.hstreb.redispoc.repository;


import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.hstreb.redispoc.model.Score;
import com.hstreb.redispoc.model.ScoreKey;
import com.hstreb.redispoc.parser.ScoreKeyParser;
import com.lambdaworks.redis.ScoredValue;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;

@Repository
@Qualifier("lettuce")
public class ScoreRepositoryLettuce implements ScoreRepository {

    public static final String KEY = "score";

    @Autowired
    private StatefulRedisClusterConnection statefulRedisClusterConnection;

    @Autowired
    private ScoreKeyParser scoreKeyParser;

    private RedisAdvancedClusterCommands redisAdvancedClusterCommands;

    @PostConstruct
    private void init() {
        redisAdvancedClusterCommands = statefulRedisClusterConnection.sync();
    }

    @Override
    public List<Score> list(Long limit, Long offset) {
        List<ScoredValue> list = redisAdvancedClusterCommands.zrevrangeWithScores(KEY, offset, limit);
        return list
                .stream()
                .map(s -> new Score(scoreKeyParser.parse((String) s.value), s.score))
                .collect(Collectors.toList());
    }

    @Override
    public Long rank(ScoreKey key) {
        return redisAdvancedClusterCommands.zrank(KEY, scoreKeyParser.parse(key));
    }

    @Override
    public void add(Score score) {
        redisAdvancedClusterCommands.zadd(KEY, score.getScore(), scoreKeyParser.parse(score.getKey()));
    }

    @Override
    public void updateKey(ScoreKey oldKey, ScoreKey newKey) {
        Double score = redisAdvancedClusterCommands.zscore(KEY, scoreKeyParser.parse(oldKey));
        updateKey(oldKey, newKey, score);
    }

    @Override
    public void updateKey(ScoreKey oldScoreKey, ScoreKey newScoreKey, Double value) {
        redisAdvancedClusterCommands.zrem(KEY, scoreKeyParser.parse(oldScoreKey));
        redisAdvancedClusterCommands.zadd(KEY, value, scoreKeyParser.parse(newScoreKey));
    }

    @Override
    public void removeAll() {
        redisAdvancedClusterCommands.zremrangebyrank(KEY, 0, -1);
    }
}