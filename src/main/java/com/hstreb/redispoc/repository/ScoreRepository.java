package com.hstreb.redispoc.repository;

import com.hstreb.redispoc.model.Score;
import com.hstreb.redispoc.model.ScoreKey;

import java.util.List;

public interface ScoreRepository {
    List<Score> list(Long limit, Long offset);
    void add(Score score);
    void updateKey(ScoreKey oldKey, ScoreKey newKey);
    void updateKey(ScoreKey oldScoreKey, ScoreKey newScoreKey, Double value);
    Long rank(ScoreKey key);
    void removeAll();
}
