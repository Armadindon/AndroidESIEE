package com.esiee.openclassroom.model;

import com.esiee.openclassroom.model.deserializer.ScoreDeserializer;
import com.esiee.openclassroom.model.serializer.ScoreSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonDeserialize(using = ScoreDeserializer.class)
@JsonSerialize(using = ScoreSerializer.class)
public class Score implements Serializable {
    private int id;
    private int score;
    private User byUser;

    public Score() {
    }

    public Score(int score, User user) {
        this.score = score;
        this.byUser = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public User getByUser() {
        return byUser;
    }

    public void setByUser(User byUser) {
        this.byUser = byUser;
    }

    @JsonIgnore
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", score=" + score +
                ", user=" + byUser +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }
}

