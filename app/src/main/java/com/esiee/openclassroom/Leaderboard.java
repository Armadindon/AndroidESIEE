package com.esiee.openclassroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    private List<TextView> ranks;
    private TextView rank;
    private TextView score;
    private Button mReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        rank = findViewById(R.id.leaderboard_textview_rank);
        score = findViewById(R.id.leaderboard_textview_score);

        ranks = new ArrayList<TextView>();
        ranks.add(findViewById(R.id.leaderboard_textview_rank1));
        ranks.add(findViewById(R.id.leaderboard_textview_rank2));
        ranks.add(findViewById(R.id.leaderboard_textview_rank3));
        ranks.add(findViewById(R.id.leaderboard_textview_rank4));
        ranks.add(findViewById(R.id.leaderboard_textview_rank5));
        ranks.add(findViewById(R.id.leaderboard_textview_rank6));
        ranks.add(findViewById(R.id.leaderboard_textview_rank7));
        ranks.add(findViewById(R.id.leaderboard_textview_rank8));
        ranks.add(findViewById(R.id.leaderboard_textview_rank9));
        ranks.add(findViewById(R.id.leaderboard_textview_rank10));

        String[] mockedPseudos = {"Kevindu93", "XxDark_SasukexX", "Alexdu76", "XxdarckpgmxXpro666",
                "Le-Killeur-1998", "Cronomasturbe", "X-DarKiller-X", "Fan2charlie_uwu_baka",
                "ProOtaku2010_gamer", "ShatonMls"};
        int index = 0;
        for (TextView text: ranks) {
            rank.setText(mockedPseudos[index++]);
        }
        rank.setText("551");
        score.setText("#1");

        mReturn = findViewById(R.id.leaderboard_button_return);

        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Leaderboard.this)
                        .setTitle("Retour à l'accueil")
                        .setMessage("Voulez-vous vraiment retourner à l'accueil?")
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
    }
}