package com.esiee.openclassroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.esiee.openclassroom.model.Question;
import com.esiee.openclassroom.model.QuestionBank;
import com.esiee.openclassroom.model.Score;
import com.esiee.openclassroom.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;

public class Questions extends AppCompatActivity implements View.OnClickListener {
    //public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    private static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    private static final String BUNDLE_STATE_REMAINING_QUESTION = "BUNDLE_STATE_REMAINING_QUESTION";
    private static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";
    private static final String BUNDLE_USER = "BUNDLE_USER";
    private static final String BUNDLE_SCORE = "BUNDLE_SCORE";
    private static final String BUNDLE_STATE_USER = "BUNDLE_STATE_USER";
    private static final int FINISH_ACTIVITY = 1;

    private QuestionBank mQuestionBank;
    private com.esiee.openclassroom.model.Question mCurrentQuestion;
    private int mRemainingQuestionCount;
    private Score mScore;
    private User mUser;
    private boolean freezeScreen;
    private Handler handler;

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

        createUpdateHandler();

        if (savedInstanceState != null) {
            mUser = (User) savedInstanceState.getSerializable(BUNDLE_STATE_USER);
            mScore = (Score) savedInstanceState.getSerializable(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_REMAINING_QUESTION);
            mQuestionBank = (QuestionBank) savedInstanceState.getSerializable(BUNDLE_STATE_QUESTION);
            mCurrentQuestion = mQuestionBank.getCurrentQuestion();
            displayQuestion(mCurrentQuestion);
        } else {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            mUser = (User) DataManager.getInstance().getUser();
            mScore = new Score(0, mUser);
            mRemainingQuestionCount = 4;
            Thread t = new Thread(() -> {
                mQuestionBank = generateQuestionBank();
                mCurrentQuestion = mQuestionBank.getNextQuestion();
                displayQuestion(mCurrentQuestion);
            });
            t.start();

        }
        freezeScreen = false;

        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Questions.this)
                        .setTitle("Retour à l'accueil")
                        .setMessage("Voulez-vous vraiment retourner à l'accueil? Toute progression sera perdue.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent menuIntent = new Intent(v.getContext(), Menu.class);
                                startActivityForResult(menuIntent, 0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }

    private QuestionBank generateQuestionBank() {
        //On récupère de l'API
        Thread t;
        Question[] q = getQuestions(DataManager.getInstance().getToken());


        return new QuestionBank(Arrays.asList(q));
    }

    private void displayQuestion(Question question) {
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
                    //On enregistre le score en bdd via l'api
                    Thread t = new Thread(() -> {
                        Score score = postScore(mScore, DataManager.getInstance().getToken());
                        Message m = new Message();
                        m.what = FINISH_ACTIVITY;
                        handler.sendMessage(m);
                    });

                    t.start();

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

    private void createUpdateHandler() {
        //Permet de creer un handler
        if (handler == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    // Means the message is sent from child thread.
                    if (msg.what == FINISH_ACTIVITY) {
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
                                    }
                                })
                                .create()
                                .show();

                    }
                    return true;
                }
            });
        }

    }

    public static Score postScore(Score s, String token){
        String baseUrl = BuildConfig.API_URL;
        Score newScore = null;
        try {
            ObjectMapper o = new ObjectMapper();
            String scoreJson = o.writeValueAsString(s);
            //System.out.println(scoreJson);
            String createdScore = ApiTools.postJSONObjectToURL(baseUrl + "scores",token, scoreJson);

            //On map la question
            //System.out.println(createdScore);
            newScore = o.readValue(createdScore, Score.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newScore;
    }

    public static Question[] getQuestions(String token){
        String baseUrl = BuildConfig.API_URL;
        String scoreString = "";
        try {
            scoreString = ApiTools.getJSONObjectFromURL(baseUrl + "questions", token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        Question[] questions = null;
        try{
            questions = mapper.readValue(scoreString, Question[].class);
            if(questions.length == 0) return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return questions;
    }
}
