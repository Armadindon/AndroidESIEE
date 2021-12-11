package com.esiee.openclassroom;

import androidx.appcompat.app.AppCompatActivity;

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

import com.esiee.openclassroom.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Connection extends AppCompatActivity {

    private static final int ENABLE_FIELDS = 1;

    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private Button mConnectionButton;
    private TextView mConnectionLink;
    private Handler UIHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);

        createUpdateUiHandler();

        mLoginEditText = findViewById(R.id.connection_edittext_login);
        mPasswordEditText = findViewById(R.id.connection_edittext_password);
        mConnectionButton = findViewById(R.id.connection_button_connection);
        mConnectionLink = findViewById(R.id.connection_link_signup);


        mConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On vérifie que toutes les données sont renseignées
                boolean fullInformations = true;
                TextView[] fieldsToCheck = {mLoginEditText, mPasswordEditText};
                for (TextView textView : fieldsToCheck) {
                    if (TextUtils.isEmpty(textView.getText())) {
                        fullInformations = false;
                        textView.setError(getString(R.string.field_required_error));
                    }
                }
                if (!fullInformations) return;

                //On bloque les champs le temps que le login se fait
                switchEnabledFields(false);


                Thread thread = new Thread(() -> {
                    String token = authenticate(mLoginEditText.getText().toString(), mPasswordEditText.getText().toString());

                    //On envoie un message au handler pour qu'il réactive les champs
                    Message m = new Message();
                    m.what = ENABLE_FIELDS;
                    UIHandler.sendMessage(m);

                    //On agit en fonction de la réponse
                    Looper.prepare();
                    if (!token.isEmpty()) {
                        User user = getUserByUsername(token, mLoginEditText.getText().toString());
                        DataManager.getInstance().setUser(user);
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.connection_success), Toast.LENGTH_LONG);
                        toast.show();
                        goToNextPanel(v.getContext(), token, user);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.connection_error), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                thread.start();
            }
        });

        //passage layout connection au layout signup, sans passation de paramètres spécifiques
        mConnectionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(v.getContext(), SignUp.class);
                startActivityForResult(signUpIntent, 0);
            }
        });
    }

    private void goToNextPanel(Context c, String token, User u) {
        Intent menuIntent = new Intent(c, Menu.class);
        startActivityForResult(menuIntent, 0);
    }

    private void switchEnabledFields(boolean enabled) {
        TextView[] fieldsToCheck = {mLoginEditText, mPasswordEditText};
        for (TextView textView : fieldsToCheck) textView.setEnabled(enabled);
        mConnectionButton.setEnabled(enabled);
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

    public static String authenticate(String user, String password){
        String baseUrl = BuildConfig.API_URL;
        String token = "";

        try {
            JSONObject json = new JSONObject();
            json.put("username", user);
            json.put("password", password);

            String userToken = ApiTools.postJSONObjectToURL(baseUrl + "authentication_token", json.toString());
            JSONObject tokenObject = new JSONObject(userToken);
            token = tokenObject.getString("token");
            DataManager.getInstance().setToken(token); //Degueu, mais pas de solution autre pour le moment
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static User getUserByUsername(String token, String username){
        String baseUrl = BuildConfig.API_URL;
        String usersString = "";
        try {
            usersString = ApiTools.getJSONObjectFromURL(baseUrl + "users?username="+username, token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        User[] users = null;
        try{
            users = mapper.readValue(usersString, User[].class);
            if(users.length == 0) return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return users[0];
    }
}