package com.esiee.openclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esiee.openclassroom.model.User;

public class Menu extends AppCompatActivity {

    private Button mQuizButton;
    private Button mLeaderboardButton;
    private Button mQuestionCreationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        mQuizButton = findViewById(R.id.menu_button_quiz);
        mLeaderboardButton = findViewById(R.id.menu_button_leaderboard);
        mQuestionCreationButton = findViewById(R.id.menu_button_questioncreation);

        mQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent questionIntent = new Intent(v.getContext(), Questions.class);
                startActivityForResult(questionIntent, 0);
            }
        });

        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderboardIntent = new Intent(v.getContext(), Leaderboard.class);
                startActivityForResult(leaderboardIntent, 0);
            }
        });

        mQuestionCreationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creationquestionIntent = new Intent(v.getContext(), QuestionCreation.class);
                startActivityForResult(creationquestionIntent, 0);
            }
        });
    }
}