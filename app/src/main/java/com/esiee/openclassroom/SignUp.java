package com.esiee.openclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esiee.openclassroom.model.User;

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

        //TEST
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String token = ApiTools.authenticate("vinc", "changeme");
                System.out.println(token);
                User[] users = ApiTools.getAllUsers(token);
                for (User user : users) System.out.println(user.getUsername());
            }
        });
        thread.start();

        mLoginEditText = findViewById(R.id.signup_edittext_login);
        mFirstnameEditText = findViewById(R.id.signup_edittext_firstname);
        mLastnameEditText = findViewById(R.id.signup_edittext_lastname);
        mPasswordEditText = findViewById(R.id.signup_edittext_password);
        mSignUpButton = findViewById(R.id.signup_button_signup);
        mSignUpLink = findViewById(R.id.signup_link_connection);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On crée le user avec les données
                User u = new User();
                u.setFirstname(mFirstnameEditText.getText().toString());
                u.setLastname(mLastnameEditText.getText().toString());
                u.setUsername(mLoginEditText.getText().toString());
                u.setPassword(mPasswordEditText.getText().toString());

                Thread thread = new Thread(() -> {
                    User newUser = ApiTools.registerUser(u);
                    System.out.println("Nouveau Utilisateur");
                    System.out.println(newUser);
                    if(newUser != null){
                        Intent connectionIntent = new Intent(v.getContext(), Connection.class);
                        startActivityForResult(connectionIntent, 0);
                    }
                });
                thread.start();
            }
        });

        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent connectionIntent = new Intent(v.getContext(), Connection.class);
                startActivityForResult(connectionIntent, 0);
            }
        });
    }
}