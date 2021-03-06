package com.esiee.openclassroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.esiee.openclassroom.model.Question;
import com.esiee.openclassroom.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;

public class QuestionCreation extends AppCompatActivity {
    private final static int ENABLE_FIELDS = 1;
    private TextView mQuestionEditText;
    private EditText mAnswer1EditText;
    private EditText mAnswer2EditText;
    private EditText mAnswer3EditText;
    private EditText mAnswer4EditText;
    private RadioGroup mAnswerRadioGroup;
    private RadioButton mAnswer1RadioButton;
    private RadioButton mAnswer2RadioButton;
    private RadioButton mAnswer3RadioButton;
    private RadioButton mAnswer4RadioButton;
    private Button mReturnButton;
    private Button mSubmitButton;

    private Handler UIHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_creation);
        createUpdateUiHandler();

        mQuestionEditText = findViewById(R.id.question_creation_edittext_question);
        mAnswer1EditText = findViewById(R.id.question_creation_edittext_answer1);
        mAnswer2EditText = findViewById(R.id.question_creation_edittext_answer2);
        mAnswer3EditText = findViewById(R.id.question_creation_edittext_answer3);
        mAnswer4EditText = findViewById(R.id.question_creation_edittext_answer4);
        mAnswerRadioGroup = findViewById(R.id.question_creation_radiogroup);
        mAnswer1RadioButton = findViewById(R.id.question_creation_radio_answer1);
        mAnswer2RadioButton = findViewById(R.id.question_creation_radio_answer2);
        mAnswer3RadioButton = findViewById(R.id.question_creation_radio_answer3);
        mAnswer4RadioButton = findViewById(R.id.question_creation_radio_answer4);
        mReturnButton = findViewById(R.id.question_creation_button_return);
        mSubmitButton = findViewById(R.id.question_creation_button_submit);

        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(QuestionCreation.this)
                        .setTitle("Retour ?? l'accueil")
                        .setMessage("Voulez-vous vraiment retourner ?? l'accueil? Toute progression sera perdue.")
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

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On v??rifie que toutes les donn??es sont renseign??es
                boolean fullInformations = true;
                TextView[] fieldsToCheck = {mQuestionEditText, mAnswer1EditText, mAnswer2EditText, mAnswer3EditText, mAnswer4EditText};
                for (TextView textView : fieldsToCheck) {
                    if (TextUtils.isEmpty(textView.getText())) {
                        fullInformations = false;
                        textView.setError(getString(R.string.field_required_error));
                    }
                }
                if (mAnswerRadioGroup.getCheckedRadioButtonId() == -1)
                {
                    fullInformations = false;
                    mAnswer1RadioButton.setError(getString(R.string.question_creation_toast_missingradio));
                    mAnswer2RadioButton.setError(getString(R.string.question_creation_toast_missingradio));
                    mAnswer3RadioButton.setError(getString(R.string.question_creation_toast_missingradio));
                    mAnswer4RadioButton.setError(getString(R.string.question_creation_toast_missingradio));
                }
                if (!fullInformations) return;

                //On bloque les champs le temps d'??xecution
                switchEnabledFields(false);

                int selectedRadio = mAnswerRadioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(selectedRadio);

                //On cr??e le user avec les donn??es
                Question q = new Question();
                q.setContent(mQuestionEditText.getText().toString());
                q.setAnswer1(mAnswer1EditText.getText().toString());
                q.setAnswer2(mAnswer2EditText.getText().toString());
                q.setAnswer3(mAnswer3EditText.getText().toString());
                q.setAnswer4(mAnswer4EditText.getText().toString());
                q.setAnswerIndex(Integer.parseInt( rb.getText().toString() ));
                q.setCreator(DataManager.getInstance().getUser());

                Thread thread = new Thread(() -> {
                    Question newQuestion = createQuestion(q, DataManager.getInstance().getToken());

                    //On envoie un message au handler pour qu'il r??active les champs
                    Message m = new Message();
                    m.what = ENABLE_FIELDS;
                    UIHandler.sendMessage(m);

                    //On agit en fonction de la r??ponse
                    Looper.prepare();
                    if (newQuestion != null) {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.question_creation_toast_created), Toast.LENGTH_LONG);
                        toast.show();
                        goToMenuPanel(v.getContext());
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.question_creation_toast_notcreated), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                thread.start();
            }
        });
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

    private void goToMenuPanel(Context c) {
        Intent menuIntent = new Intent(c, Menu.class);
        startActivityForResult(menuIntent, 0);
    }

    private void switchEnabledFields(boolean enabled) {
        TextView[] fieldsToCheck = {mQuestionEditText, mAnswer1EditText, mAnswer2EditText, mAnswer3EditText, mAnswer4EditText};
        for (TextView textView : fieldsToCheck) textView.setEnabled(enabled);
        mSubmitButton.setEnabled(enabled);
    }

    public static Question createQuestion(Question q, String token){
        String baseUrl = BuildConfig.API_URL;
        Question newQuestion = null;
        try {
            ObjectMapper o = new ObjectMapper();
            String questionJson = o.writeValueAsString(q);
            //System.out.println(questionJson);
            String createdQuestion = ApiTools.postJSONObjectToURL(baseUrl + "questions",token, questionJson);
            //On map la question
            //System.out.println(createdQuestion);
            newQuestion = o.readValue(createdQuestion, Question.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newQuestion;
    }
}