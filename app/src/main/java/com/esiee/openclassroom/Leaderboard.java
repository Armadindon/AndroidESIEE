package com.esiee.openclassroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.esiee.openclassroom.model.Score;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    private List<TextView> ranks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        ranks = new ArrayList<>();
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
            Score[] scores = getScores(DataManager.getInstance().getToken());
            int index = 0;
            for (Score score: scores) {
                int current = index++;
                if(current == 10) break;
                ranks.get(current).setText(score.getByUser().getUsername() + " - " + score.getScore());
            }
        });
        thread.start();

    }

    public static Score[] getScores(String token){
        String baseUrl = BuildConfig.API_URL;
        String scoreString = "";
        try {
            scoreString = ApiTools.getJSONObjectFromURL(baseUrl + "scores", token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        Score[] scores = null;
        try{
            scores = mapper.readValue(scoreString, Score[].class);
            if(scores.length == 0) return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println(Arrays.toString(scores));
        return scores;
    }
}