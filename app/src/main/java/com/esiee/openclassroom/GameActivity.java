package com.esiee.openclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.esiee.openclassroom.model.Score;
import com.esiee.openclassroom.BuildConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esiee.openclassroom.model.Question;
import com.esiee.openclassroom.model.QuestionBank;
import com.esiee.openclassroom.model.User;

import java.io.IOException;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    //public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    private static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    private static final String BUNDLE_STATE_REMAINING_QUESTION = "BUNDLE_STATE_REMAINING_QUESTION";
    private static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";
    private static final String BUNDLE_USER = "BUNDLE_USER";
    private static final String BUNDLE_SCORE = "BUNDLE_SCORE";
    private static final String BUNDLE_STATE_USER = "BUNDLE_STATE_USER";


    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;
    private int mRemainingQuestionCount;
    private Score mScore;
    private User mUser;
    private boolean freezeScreen;

    private TextView mQuestionTitle;
    private Button mAnswer1;
    private Button mAnswer2;
    private Button mAnswer3;
    private Button mAnswer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                User[] users = ApiTools.getAllUsers();
                String token = ApiTools.getToken();
            }
        });

        thread.start();

        mQuestionTitle = findViewById(R.id.game_activity_textview_question);
        mAnswer1 = findViewById(R.id.game_activity_button_1);
        mAnswer2 = findViewById(R.id.game_activity_button_2);
        mAnswer3 = findViewById(R.id.game_activity_button_3);
        mAnswer4 = findViewById(R.id.game_activity_button_4);

        mAnswer1.setOnClickListener(this);
        mAnswer2.setOnClickListener(this);
        mAnswer3.setOnClickListener(this);
        mAnswer4.setOnClickListener(this);

        if (savedInstanceState != null) {
            mUser = (User) savedInstanceState.getSerializable(BUNDLE_STATE_USER);
            mScore = (Score) savedInstanceState.getSerializable(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_REMAINING_QUESTION);
            mQuestionBank = (QuestionBank) savedInstanceState.getSerializable(BUNDLE_STATE_QUESTION);
            mCurrentQuestion = mQuestionBank.getCurrentQuestion();
        } else {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            mUser = (User) bundle.getSerializable(BUNDLE_USER);
            mScore = new Score(0, mUser);
            mRemainingQuestionCount = 4;
            mQuestionBank = generateQuestionBank();
            mCurrentQuestion = mQuestionBank.getNextQuestion();
        }
        System.out.println(mQuestionBank);
        displayQuestion(mCurrentQuestion);
        freezeScreen = false;
    }

    private QuestionBank generateQuestionBank() {
        //On initialise le modèle
        Question question1 = new Question(
                getString(R.string.question_1),
                getString(R.string.question_1_answer_1),
                getString(R.string.question_1_answer_2),
                getString(R.string.question_1_answer_3),
                getString(R.string.question_1_answer_4),
                0
        );

        Question question2 = new Question(
                getString(R.string.question_2),
                getString(R.string.question_2_answer_1),
                getString(R.string.question_2_answer_2),
                getString(R.string.question_2_answer_3),
                getString(R.string.question_2_answer_4),
                3
        );

        Question question3 = new Question(
                getString(R.string.question_3),
                getString(R.string.question_3_answer_1),
                getString(R.string.question_3_answer_2),
                getString(R.string.question_3_answer_3),
                getString(R.string.question_3_answer_4),
                3
        );

        Question question4 = new Question(
                getString(R.string.question_4),
                getString(R.string.question_4_answer_1),
                getString(R.string.question_4_answer_2),
                getString(R.string.question_4_answer_3),
                getString(R.string.question_4_answer_4),
                0
        );

        Question question5 = new Question(
                getString(R.string.question_5),
                getString(R.string.question_5_answer_1),
                getString(R.string.question_5_answer_2),
                getString(R.string.question_5_answer_3),
                getString(R.string.question_5_answer_4),
                1
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5));
    }

    private void displayQuestion(final Question question) {
        // Set the text for the question text view and the four buttons
        mCurrentQuestion = question;

        mQuestionTitle.setText(question.getContent());
        mAnswer1.setText(question.getAnswer1());
        mAnswer2.setText(question.getAnswer2());
        mAnswer3.setText(question.getAnswer3());
        mAnswer4.setText(question.getAnswer4());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !freezeScreen && super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        int index;

        if (v == mAnswer1) {
            index = 0;
        } else if (v == mAnswer2) {
            index = 1;
        } else if (v == mAnswer3) {
            index = 2;
        } else if (v == mAnswer4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }

        if (index == mCurrentQuestion.getAnswerIndex()) {
            Toast.makeText(this, getString(R.string.correct_answer), Toast.LENGTH_SHORT).show();
            mScore.setScore(mScore.getScore() + 1);
        } else {
            Toast.makeText(this, getString(R.string.wrong_answer), Toast.LENGTH_SHORT).show();
        }

        mRemainingQuestionCount--;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        freezeScreen = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                freezeScreen = false;
                if (mRemainingQuestionCount > 0) {
                    mCurrentQuestion = mQuestionBank.getNextQuestion();
                    displayQuestion(mCurrentQuestion);
                } else {
                    // Plus de questions; le jeu est finito
                    // Upload du score en bdd
                    builder.setTitle(getString(R.string.finish_alert_title))
                            .setMessage(getString(R.string.finish_final_score) + mScore.getScore())
                            .setPositiveButton(getString(R.string.finish_final_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra(BUNDLE_USER, mUser);
                                    intent.putExtra(BUNDLE_SCORE, mScore);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        }, 2_000); // LENGTH_SHORT is usually 2 second long
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_REMAINING_QUESTION, mRemainingQuestionCount);
        outState.putSerializable(BUNDLE_STATE_QUESTION, mQuestionBank);
        outState.putSerializable(BUNDLE_STATE_SCORE, mScore);
        outState.putSerializable(BUNDLE_STATE_USER, mUser);
    }

}