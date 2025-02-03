package com.example.mathchallenge;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreManager {

    private static final String PREFS_NAME = "GameSettings";
    private static final String SCORE_KEY_PREFIX = "HighScore";

    private SharedPreferences sharedPreferences;

    public ScoreManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Save a new score to the leaderboard
    public void saveScore(int newScore) {
        ArrayList<Integer> scores = getHighScores();

        // Add the new score and sort in descending order
        scores.add(newScore);
        Collections.sort(scores, Collections.reverseOrder());

        // Keep only the top 10 scores
        if (scores.size() > 10) {
            scores = new ArrayList<>(scores.subList(0, 10));
        }

        // Save updated scores
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < scores.size(); i++) {
            editor.putInt(SCORE_KEY_PREFIX + (i + 1), scores.get(i));
        }
        editor.apply();
    }

    // Retrieve the top 10 high scores
    public ArrayList<Integer> getHighScores() {
        ArrayList<Integer> scores = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            scores.add(sharedPreferences.getInt(SCORE_KEY_PREFIX + i, 0));
        }
        return scores;
    }
}
