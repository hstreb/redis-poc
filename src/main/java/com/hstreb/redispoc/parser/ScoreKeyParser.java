package com.hstreb.redispoc.parser;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hstreb.redispoc.model.ScoreKey;

@Component
public class ScoreKeyParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreKeyParser.class);

    private ObjectMapper mapper = new ObjectMapper();

    public ScoreKey parse(String t) {
        try {
            return mapper.readValue(t, ScoreKey.class);
        } catch (IOException e) {
            LOGGER.error("Error on get score from json. {}", t, e);
        }
        return null;
    }

    public String parse(ScoreKey oldScoreKey) {
        try {
            return mapper.writeValueAsString(oldScoreKey);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error on write score to json. {}", oldScoreKey, e);
        }
        return null;
    }
}
