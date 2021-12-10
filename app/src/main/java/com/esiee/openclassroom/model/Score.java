package com.esiee.openclassroom.model;

import com.esiee.openclassroom.model.serializer.ScoreSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(using = ScoreSerializer.class)
public class Score implements Serializable {
    private int id;
    private int score;
    private User user;

    public Score() {
    }

    public Score(int score, User user) {
        this.score = score;
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                ", user=" + user +
                '}';
    }
}

