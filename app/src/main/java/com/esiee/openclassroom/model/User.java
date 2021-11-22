package com.esiee.openclassroom.model;

import java.io.Serializable;

public class User implements Serializable {

    private String mFirstName;

    private int mScore;

    public String getFirstName() { return mFirstName; }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public int getScore() { return mScore; }

    public void setScore(int score) { mScore = score; }

}
