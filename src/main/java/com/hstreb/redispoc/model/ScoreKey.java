package com.hstreb.redispoc.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

public class ScoreKey implements Serializable {

    private String id;
    private String name;
    private Boolean pro;

    public ScoreKey() {
    }

    public ScoreKey(String id, String name, Boolean pro) {
        this.id = id;
        this.name = name;
        this.pro = pro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPro() {
        return pro;
    }

    public void setPro(Boolean pro) {
        this.pro = pro;
    }

    @Override
    public String toString() {
        return "ScoreKey(" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pro=" + pro +
                ')';
    }
}
