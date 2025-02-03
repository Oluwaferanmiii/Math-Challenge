package com.example.mathchallenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private ArrayList<Integer> highScores;
    private ArrayList<String> playerNames;

    // UI Elements for Top 3 Players
    private TextView firstPlaceName, firstPlaceScore;
    private TextView secondPlaceName, secondPlaceScore;
    private TextView thirdPlaceName, thirdPlaceScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize RecyclerView
        leaderboardRecyclerView = findViewById(R.id.leaderboard_recycler_view);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Top 3 TextViews
        firstPlaceName = findViewById(R.id.first_place_name);
        firstPlaceScore = findViewById(R.id.first_place_score);
        secondPlaceName = findViewById(R.id.second_place_name);
        secondPlaceScore = findViewById(R.id.second_place_score);
        thirdPlaceName = findViewById(R.id.third_place_name);
        thirdPlaceScore = findViewById(R.id.third_place_score);

        loadHighScores();

        // Back button functionality
        Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderboardActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadHighScores() {
        SharedPreferences preferences = getSharedPreferences("Leaderboard", MODE_PRIVATE);
        ArrayList<ScoreEntry> scoreEntries = new ArrayList<>();

        // Retrieve stored scores and names
        for (int i = 1; i <= 10; i++) {
            int score = preferences.getInt("HighScore" + i, 0);
            String name = preferences.getString("HighScoreName" + i, "AAA");
            scoreEntries.add(new ScoreEntry(score, name));
        }

        // Sort scores in descending order
        Collections.sort(scoreEntries);

        highScores = new ArrayList<>();
        playerNames = new ArrayList<>();
        for (ScoreEntry entry : scoreEntries) {
            highScores.add(entry.score);
            playerNames.add(entry.name);
        }

        // Assign Top 3 Ranks
        if (scoreEntries.size() > 0) {
            firstPlaceName.setText(scoreEntries.get(0).name);
            firstPlaceScore.setText(String.valueOf(scoreEntries.get(0).score));
        }
        if (scoreEntries.size() > 1) {
            secondPlaceName.setText(scoreEntries.get(1).name);
            secondPlaceScore.setText(String.valueOf(scoreEntries.get(1).score));
        }
        if (scoreEntries.size() > 2) {
            thirdPlaceName.setText(scoreEntries.get(2).name);
            thirdPlaceScore.setText(String.valueOf(scoreEntries.get(2).score));
        }

        // Populate RecyclerView with ranks 4-10
        if (scoreEntries.size() > 3) {
            ArrayList<Integer> remainingScores = new ArrayList<>();
            ArrayList<String> remainingNames = new ArrayList<>();
            for (int i = 3; i < scoreEntries.size(); i++) {
                remainingScores.add(scoreEntries.get(i).score);
                remainingNames.add(scoreEntries.get(i).name);
            }
            leaderboardAdapter = new LeaderboardAdapter(remainingScores, remainingNames);
            leaderboardRecyclerView.setAdapter(leaderboardAdapter);
        }
    }
}


