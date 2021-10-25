package com.esiee.openclassroom.model;

import java.util.Collections;
import java.util.List;

public class QuestionBank {
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
        return mQuestionList.get(mNextQuestionIndex++);
    }
}
