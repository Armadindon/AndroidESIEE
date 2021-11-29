package com.esiee.openclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    private EditText mLoginEditText;
    private EditText mFirstnameEditText;
    private EditText mLastnameEditText;
    private EditText mPasswordEditText;
    private Button mSignUpButton;
    private TextView mSignUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        mLoginEditText = findViewById(R.id.signup_edittext_login);
        mFirstnameEditText = findViewById(R.id.signup_edittext_firstname);
        mLastnameEditText = findViewById(R.id.signup_edittext_lastname);
        mPasswordEditText = findViewById(R.id.signup_edittext_password);
        mSignUpButton = findViewById(R.id.signup_button_signup);
        mSignUpLink = findViewById(R.id.signup_link_connection);

        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent connectionIntent = new Intent(v.getContext(), Connection.class);
                startActivityForResult(connectionIntent, 0);
            }
        });
    }
}