package com.esiee.openclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esiee.openclassroom.model.User;

public class Menu extends AppCompatActivity {

    public static final String INTENT_TOKEN = "token";
    public static final String INTENT_USER = "user";

    private User user;
    private String token;

    private Button mQuizButton;
    private Button mLeaderboardButton;
    private Button mQuestionCreationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Intent intent = getIntent();
        user = (User) intent.getExtras().get(Connection.INTENT_USER);
        token = intent.getExtras().getString(Connection.INTENT_TOKEN);

        mQuizButton = findViewById(R.id.menu_button_quiz);
        mLeaderboardButton = findViewById(R.id.menu_button_leaderboard);
        mQuestionCreationButton = findViewById(R.id.menu_button_questioncreation);

        mQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent connectionIntent = new Intent(v.getContext(), Questions.class);
                startActivityForResult(connectionIntent, 0);
            }
        });

        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderboardIntent = new Intent(v.getContext(), Leaderboard.class);
                leaderboardIntent.putExtra(INTENT_TOKEN, token);
                leaderboardIntent.putExtra(INTENT_USER, user);
                startActivityForResult(leaderboardIntent, 0);
            }
        });

        mQuestionCreationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creationquestionIntent = new Intent(v.getContext(), QuestionCreation.class);
                creationquestionIntent.putExtra(INTENT_TOKEN, token);
                creationquestionIntent.putExtra(INTENT_USER, user);
                startActivityForResult(creationquestionIntent, 0);
            }
        });
    }
}