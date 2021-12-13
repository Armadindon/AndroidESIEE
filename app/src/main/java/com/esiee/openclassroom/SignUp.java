package com.esiee.openclassroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esiee.openclassroom.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUp extends AppCompatActivity {

    // Message type code.
    private final static int ENABLE_FIELDS = 1;
    private EditText mLoginEditText;
    private EditText mFirstnameEditText;
    private EditText mLastnameEditText;
    private EditText mPasswordEditText;
    private Button mSignUpButton;
    private TextView mSignUpLink;
    private Handler UIHandler; //Permet de changer les vues depuis un thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        createUpdateUiHandler();

        mLoginEditText = findViewById(R.id.signup_edittext_login);
        mFirstnameEditText = findViewById(R.id.signup_edittext_firstname);
        mLastnameEditText = findViewById(R.id.signup_edittext_lastname);
        mPasswordEditText = findViewById(R.id.signup_edittext_password);
        mSignUpButton = findViewById(R.id.signup_button_signup);
        mSignUpLink = findViewById(R.id.signup_link_connection);


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On vérifie que toutes les données sont renseignées
                boolean fullInformations = true;
                TextView[] fieldsToCheck = {mLoginEditText, mFirstnameEditText, mLastnameEditText, mPasswordEditText};
                for (TextView textView : fieldsToCheck) {
                    if (TextUtils.isEmpty(textView.getText())) {
                        fullInformations = false;
                        textView.setError(getString(R.string.field_required_error));
                    }
                }
                if (!fullInformations) return;

                //On bloque les champs le temps que l'utilisateur soit enregistrés
                switchEnabledFields(false);

                //On crée le user avec les données
                User u = new User();
                u.setFirstname(mFirstnameEditText.getText().toString());
                u.setLastname(mLastnameEditText.getText().toString());
                u.setUsername(mLoginEditText.getText().toString());
                u.setPassword(mPasswordEditText.getText().toString());

                Thread thread = new Thread(() -> {
                    User newUser = registerUser(u);

                    //On envoie un message au handler pour qu'il réactive les champs
                    Message m = new Message();
                    m.what = ENABLE_FIELDS;
                    UIHandler.sendMessage(m);

                    //On agit en fonction de la réponse
                    Looper.prepare();
                    if (newUser != null) {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.user_created), Toast.LENGTH_LONG);
                        toast.show();
                        goToLoginPanel(v.getContext());
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.user_created_error), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                thread.start();
            }
        });

        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginPanel(v.getContext());
            }
        });
    }

    private void goToLoginPanel(Context c) {
        Intent connectionIntent = new Intent(c, Connection.class);
        startActivityForResult(connectionIntent, 0);
    }

    private void switchEnabledFields(boolean enabled) {
        TextView[] fieldsToCheck = {mLoginEditText, mFirstnameEditText, mLastnameEditText, mPasswordEditText};
        for (TextView textView : fieldsToCheck) textView.setEnabled(enabled);
        mSignUpButton.setEnabled(enabled);
    }

    private void createUpdateUiHandler() {
        //Permet de creer un handler
        if (UIHandler == null) {
            UIHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    // Means the message is sent from child thread.
                    if (msg.what == ENABLE_FIELDS) {
                        switchEnabledFields(true);
                    }
                    return true;
                }
            });
        }
    }

    public static User registerUser(User u){
        String baseUrl = BuildConfig.API_URL;
        User newUser = null;
        try {
            ObjectMapper o = new ObjectMapper();
            String userJson = o.writeValueAsString(u);
            Logger.getAnonymousLogger().log(Level.INFO, userJson);
            String createdUser = ApiTools.postJSONObjectToURL(baseUrl + "users", userJson);
            //On map l'user
            Logger.getAnonymousLogger().log(Level.INFO, createdUser);
            newUser = o.readValue(createdUser, User.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newUser;
    }
}