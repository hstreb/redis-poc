package com.hstreb.redispoc.model;

import java.io.Serializable;

public class Score implements Serializable {

    private ScoreKey key;
    private Double score;

    public Score(ScoreKey key, Double score) {
        this.key = key;
        this.score = score;
    }

    public Score(ScoreKey key) {
        this.key = key;
    }

    public ScoreKey getKey() {
        return key;
    }

    public void setKey(ScoreKey key) {
        this.key = key;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "key=" + key +
                ", score=" + score +
                '}';
    }
}
