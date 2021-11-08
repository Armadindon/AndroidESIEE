package com.esiee.openclassroom.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestionBank implements Serializable {
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public QuestionBank(List<Question> questionList) {
        // Shuffle the question list before storing it
        Collections.shuffle(questionList);
        this.mQuestionList = questionList;

        mNextQuestionIndex = 0;
    }

    public Question getNextQuestion() {
        // Loop over the questions and return a new one at each call
        Logger.getAnonymousLogger().log(Level.INFO, "On chope la prochaine question");
        return mQuestionList.get(mNextQuestionIndex++);
    }

    public Question getCurrentQuestion(){
        return mQuestionList.get(mNextQuestionIndex-1);
    }

    @Override
    public String toString() {
        return "QuestionBank{" +
                "mQuestionList=" + mQuestionList +
                ", mNextQuestionIndex=" + mNextQuestionIndex +
                '}';
    }
}
