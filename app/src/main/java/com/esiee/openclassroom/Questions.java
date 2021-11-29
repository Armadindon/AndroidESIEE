package com.esiee.openclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esiee.openclassroom.model.QuestionBank;

import java.util.Arrays;

public class Questions extends AppCompatActivity implements View.OnClickListener {
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    private static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    private static final String BUNDLE_STATE_REMAINING_QUESTION = "BUNDLE_STATE_REMAINING_QUESTION";
    private static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";

    private QuestionBank mQuestionBank;
    private com.esiee.openclassroom.model.Question mCurrentQuestion;
    private int mRemainingQuestionCount;
    private int mScore;
    private boolean freezeScreen;

    private TextView mQuestionTitle;
    private Button mAnswer1;
    private Button mAnswer2;
    private Button mAnswer3;
    private Button mAnswer4;
    private Button mReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);

        mQuestionTitle = findViewById(R.id.questions_textview_question);
        mAnswer1 = findViewById(R.id.questions_button_answer1);
        mAnswer2 = findViewById(R.id.questions_button_answer2);
        mAnswer3 = findViewById(R.id.questions_button_answer3);
        mAnswer4 = findViewById(R.id.questions_button_answer4);
        mReturn = findViewById(R.id.questions_button_return);

        mAnswer1.setOnClickListener(this);
        mAnswer2.setOnClickListener(this);
        mAnswer3.setOnClickListener(this);
        mAnswer4.setOnClickListener(this);

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_REMAINING_QUESTION);
            mQuestionBank = (QuestionBank) savedInstanceState.getSerializable(BUNDLE_STATE_QUESTION);
            mCurrentQuestion = mQuestionBank.getCurrentQuestion();
        } else {
            mScore = 0;
            mRemainingQuestionCount = 4;
            mQuestionBank = generateQuestionBank();
            mCurrentQuestion = mQuestionBank.getNextQuestion();
        }
        System.out.println(mQuestionBank);
        displayQuestion(mCurrentQuestion);
        freezeScreen = false;

        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Questions.this)
                    .setTitle("Retour à l'accueil")
                    .setMessage("Voulez-vous vraiment retourner à l'accueil? Toute progression sera perdue.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent signUpIntent = new Intent(v.getContext(), SignUp.class);
                            startActivityForResult(signUpIntent, 0);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            }
        });


    }

    private QuestionBank generateQuestionBank() {
        //On initialise le modèle
        com.esiee.openclassroom.model.Question question1 = new com.esiee.openclassroom.model.Question(
                getString(R.string.question_1),
                Arrays.asList(
                        getString(R.string.question_1_answer_1),
                        getString(R.string.question_1_answer_2),
                        getString(R.string.question_1_answer_3),
                        getString(R.string.question_1_answer_4)
                ),
                0
        );

        com.esiee.openclassroom.model.Question question2 = new com.esiee.openclassroom.model.Question(
                getString(R.string.question_2),
                Arrays.asList(
                        getString(R.string.question_2_answer_1),
                        getString(R.string.question_2_answer_2),
                        getString(R.string.question_2_answer_3),
                        getString(R.string.question_2_answer_4)
                ),
                3
        );

        com.esiee.openclassroom.model.Question question3 = new com.esiee.openclassroom.model.Question(
                getString(R.string.question_3),
                Arrays.asList(
                        getString(R.string.question_3_answer_1),
                        getString(R.string.question_3_answer_2),
                        getString(R.string.question_3_answer_3),
                        getString(R.string.question_3_answer_4)
                ),
                3
        );

        com.esiee.openclassroom.model.Question question4 = new com.esiee.openclassroom.model.Question(
                getString(R.string.question_4),
                Arrays.asList(
                        getString(R.string.question_4_answer_1),
                        getString(R.string.question_4_answer_2),
                        getString(R.string.question_4_answer_3),
                        getString(R.string.question_4_answer_4)
                ),
                0
        );

        com.esiee.openclassroom.model.Question question5 = new com.esiee.openclassroom.model.Question(
                getString(R.string.question_5),
                Arrays.asList(
                        getString(R.string.question_5_answer_1),
                        getString(R.string.question_5_answer_2),
                        getString(R.string.question_5_answer_3),
                        getString(R.string.question_5_answer_4)
                ),
                1
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5));
    }

    private void displayQuestion(final com.esiee.openclassroom.model.Question question) {
        // Set the text for the question text view and the four buttons
        mCurrentQuestion = question;

        mQuestionTitle.setText(question.getQuestion());
        mAnswer1.setText(question.getChoiceList().get(0));
        mAnswer2.setText(question.getChoiceList().get(1));
        mAnswer3.setText(question.getChoiceList().get(2));
        mAnswer4.setText(question.getChoiceList().get(3));
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
            mScore++;
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

                    builder.setTitle(getString(R.string.finish_alert_title))
                            .setMessage(getString(R.string.finish_final_score) + mScore)
                            .setPositiveButton(getString(R.string.finish_final_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
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

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_REMAINING_QUESTION, mRemainingQuestionCount);
        outState.putSerializable(BUNDLE_STATE_QUESTION, mQuestionBank);
    }

}