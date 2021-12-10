package com.esiee.openclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.esiee.openclassroom.model.Score;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    private List<TextView> ranks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

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


        Thread thread = new Thread(() -> {
            Score[] scores = ApiTools.getScores(ApiTools.token);
            int index = 0;
            for (Score score: scores) {
                int current = index++;
                ranks.get(current).setText(score.getByUser().getUsername() + " - " + score.getScore());
            }
        });
        thread.start();

    }
}