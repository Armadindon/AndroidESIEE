package com.esiee.openclassroom.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private int mId;
    private String mContent;
    private String mAnswer1;
    private String mAnswer2;
    private String mAnswer3;
    private String mAnswer4;
    private int mAnswerIndex;
    private User mCreator;

    public Question() {
    }

    public Question(int mId, String mContent, String mAnswer1, String mAnswer2, String mAnswer3, String mAnswer4, int mAnswerIndex, User mCreator) {
        this.mId = mId;
        this.mContent = mContent;
        this.mAnswer1 = mAnswer1;
        this.mAnswer2 = mAnswer2;
        this.mAnswer3 = mAnswer3;
        this.mAnswer4 = mAnswer4;
        this.mAnswerIndex = mAnswerIndex;
        this.mCreator = mCreator;
    }

    public Question(String mContent, String mAnswer1, String mAnswer2, String mAnswer3, String mAnswer4, int mAnswerIndex) {
        this.mContent = mContent;
        this.mAnswer1 = mAnswer1;
        this.mAnswer2 = mAnswer2;
        this.mAnswer3 = mAnswer3;
        this.mAnswer4 = mAnswer4;
        this.mAnswerIndex = mAnswerIndex;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getAnswer1() {
        return mAnswer1;
    }

    public void setAnswer1(String mAnswer1) {
        this.mAnswer1 = mAnswer1;
    }

    public String getAnswer2() {
        return mAnswer2;
    }

    public void setAnswer2(String mAnswer2) {
        this.mAnswer2 = mAnswer2;
    }

    public String getAnswer3() {
        return mAnswer3;
    }

    public void setAnswer3(String mAnswer3) {
        this.mAnswer3 = mAnswer3;
    }

    public String getAnswer4() {
        return mAnswer4;
    }

    public void setAnswer4(String mAnswer4) {
        this.mAnswer4 = mAnswer4;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }

    public void setAnswerIndex(int mAnswerIndex) {
        this.mAnswerIndex = mAnswerIndex;
    }

    public User getCreator() {
        return mCreator;
    }

    public void setCreator(User mCreator) {
        this.mCreator = mCreator;
    }
}
