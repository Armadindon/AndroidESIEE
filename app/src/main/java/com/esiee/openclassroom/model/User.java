package com.esiee.openclassroom.model;

import java.io.Serializable;

public class User implements Serializable {

    private int mId;
    private String mUsername;
    private String mFirstName;
    private String mLastName;
    private String mPassword;
    private int mScore;

    public User() {
    }

    public User(int mId, String mUsername, String mFirstName, String mLastName, String mPassword, int mScore) {
        this.mId = mId;
        this.mUsername = mUsername;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPassword = mPassword;
        this.mScore = mScore;
    }

        public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }
}
