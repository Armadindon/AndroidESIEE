package com.esiee.openclassroom.model;

import java.io.Serializable;

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

