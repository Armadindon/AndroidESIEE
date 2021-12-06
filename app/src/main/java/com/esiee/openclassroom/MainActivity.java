package com.esiee.openclassroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esiee.openclassroom.model.Score;
import com.esiee.openclassroom.model.User;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREF_USER = "SHARED_PREF_USER";
    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    private static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME";
    private static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";
    private static final String BUNDLE_USER = "BUNDLE_USER";
    private static final String BUNDLE_SCORE = "BUNDLE_SCORE";

    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;

    private User mUser;
    private Score mScore;
    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = new User();
        mScore = new Score();

        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mNameEditText = findViewById(R.id.main_edittext_name);
        mPlayButton = findViewById(R.id.main_button_play);

        mPlayButton.setEnabled(false);

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(!s.toString().isEmpty());
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstname(mNameEditText.getText().toString());
                mScore.setUser(mUser);
                mScore.setScore(0);
                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                gameActivityIntent.putExtra(BUNDLE_USER, mUser);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        //On vérifie si l'application à déjà été lancé
        String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
        int score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_SCORE, -1);

        if(firstName != null){
            mGreetingTextView.setText(String.format(getString(R.string.welcome_screen_previous_play),firstName, score));
            mNameEditText.setText(firstName);
            mNameEditText.setSelection(mNameEditText.getText().length());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            //mUser.setScore(data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0));
            Bundle bundle = data.getExtras();
            mUser = (User) bundle.getSerializable(BUNDLE_USER);
            mScore = (Score) bundle.getSerializable(BUNDLE_SCORE);
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putString(SHARED_PREF_USER_INFO_NAME, mUser.getFirstname())
                    .putInt(SHARED_PREF_USER_INFO_SCORE, mScore.getScore())
                    .apply();

            mGreetingTextView.setText(String.format(getString(R.string.welcome_screen_previous_play), mUser.getFirstname(), mScore.getScore()));
            mNameEditText.setSelection(mNameEditText.getText().length());
        }

    }
}