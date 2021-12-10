package com.esiee.openclassroom;

import com.esiee.openclassroom.model.User;

public class DataManager {

    private static DataManager INSTANCE = null;
    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    private String token;
    private User user;

    private DataManager(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
