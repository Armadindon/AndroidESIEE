package com.esiee.openclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Connection extends AppCompatActivity {

    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private Button mConnectionButton;
    private TextView mConnectionLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);

        mLoginEditText = findViewById(R.id.connection_edittext_login);
        mPasswordEditText = findViewById(R.id.connection_edittext_password);
        mConnectionButton = findViewById(R.id.connection_button_connection);
        mConnectionLink = findViewById(R.id.connection_link_signup);

        mConnectionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(v.getContext(), SignUp.class);
                startActivityForResult(signUpIntent, 0);
            }
        });
    }
}