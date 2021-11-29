package com.esiee.openclassroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

public class QuestionCreation extends AppCompatActivity {

    private TextView mQuestionTitle;
    private EditText mAnswer1;
    private EditText mAnswer2;
    private EditText mAnswer3;
    private EditText mAnswer4;
    private Button mReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_creation);

        mQuestionTitle = findViewById(R.id.question_creation_edittext_question);
        mAnswer1 = findViewById(R.id.question_creation_edittext_answer1);
        mAnswer2 = findViewById(R.id.question_creation_edittext_answer2);
        mAnswer3 = findViewById(R.id.question_creation_edittext_answer3);
        mAnswer4 = findViewById(R.id.question_creation_edittext_answer4);
        mReturn = findViewById(R.id.question_creation_button_return);

        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(QuestionCreation.this)
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
}