package com.example.mathchallenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class GameOverActivity extends AppCompatActivity {

    private TextView scoreText, highScoreText, messageText;
    private EditText nameInput;
    private Button tryAgainButton, exitButton, saveNameButton;
    private ImageView settingsButton;
    private int finalScore;
    private boolean isNewHighScore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        scoreText = findViewById(R.id.score_text);
        highScoreText = findViewById(R.id.high_score_text);
        messageText = findViewById(R.id.message_text);
        nameInput = findViewById(R.id.name_input);
        saveNameButton = findViewById(R.id.btn_save_name);
        tryAgainButton = findViewById(R.id.btn_try_again);
        settingsButton = findViewById(R.id.btn_settings);
        exitButton = findViewById(R.id.btn_exit);
        TextView gameOverText = findViewById(R.id.game_over_title);

        // Load the animation
        Animation scaleBounce = AnimationUtils.loadAnimation(this, R.anim.game_over_anim);

        // Apply the animation
        gameOverText.startAnimation(scaleBounce);

        // Get final score from intent
        finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        scoreText.setText("Your Score: " + finalScore);

        // Retrieve the latest high score
        SharedPreferences preferences = getSharedPreferences("Leaderboard", MODE_PRIVATE);
        int savedHighScore = preferences.getInt("HighScore1", 0); // Fetch Rank 1 Score

        highScoreText.setText("High Score: " + savedHighScore);

        // Check if player achieved a new high score and save high score
        isNewHighScore = saveHighScore(finalScore);

        if (isNewHighScore) {
            messageText.setText("Congratulations! New High Score!");
            nameInput.setVisibility(View.VISIBLE);
            saveNameButton.setVisibility(View.VISIBLE);

            // Hide retry and exit buttons until name is entered
            tryAgainButton.setVisibility(View.GONE);
            settingsButton.setVisibility(View.GONE);
            exitButton.setVisibility(View.GONE);
        } else {
            messageText.setText("Try Again to Beat Your High Score!");
            nameInput.setVisibility(View.GONE);
            saveNameButton.setVisibility(View.GONE);

            // Show retry and exit buttons immediately
            tryAgainButton.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.VISIBLE);
        }

        // Make sure the message is visible
        messageText.setVisibility(View.VISIBLE);

        saveNameButton.setOnClickListener(v -> {
            String playerName = nameInput.getText().toString().trim();
            if (!playerName.isEmpty()) {
                saveHighScoreName(playerName);

                // Hide input and submit button
                nameInput.setVisibility(View.GONE);
                saveNameButton.setVisibility(View.GONE);

                // Show retry and exit buttons after saving name
                tryAgainButton.setVisibility(View.VISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
                exitButton.setVisibility(View.VISIBLE);
            }
        });

        tryAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean saveHighScore(int newScore) {
        SharedPreferences preferences = getSharedPreferences("Leaderboard", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        ArrayList<ScoreEntry> scoreEntries = new ArrayList<>();

        // Load existing scores and names
        for (int i = 1; i <= 10; i++) {
            int score = preferences.getInt("HighScore" + i, 0);
            String name = preferences.getString("HighScoreName" + i, "AAA");
            scoreEntries.add(new ScoreEntry(score, name));
        }

        // Check if the score already exists to prevent duplication
        boolean isNewScore = true;
        for (ScoreEntry entry : scoreEntries) {
            if (entry.score == newScore) {
                isNewScore = false; // Score already exists
                break;
            }
        }

        // Only add the new score if it doesn't exist already
        if (isNewScore) {
            scoreEntries.add(new ScoreEntry(newScore, "---")); // Add new score with a placeholder name
        }

        // Sort scores in descending order
        Collections.sort(scoreEntries);

        // Keep only top 10 scores
        if (scoreEntries.size() > 10) {
            scoreEntries = new ArrayList<>(scoreEntries.subList(0, 10));
        }

        // Save the sorted scores back to SharedPreferences
        for (int i = 0; i < scoreEntries.size(); i++) {
            editor.putInt("HighScore" + (i + 1), scoreEntries.get(i).score);
            editor.putString("HighScoreName" + (i + 1), scoreEntries.get(i).name);
        }
        editor.apply();

        return newScore == scoreEntries.get(0).score;
    }


    private void saveHighScoreName(String playerName) {
        if (playerName.isEmpty()) {
            nameInput.setError("Please enter your name");
            return;
        }

        SharedPreferences preferences = getSharedPreferences("Leaderboard", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        ArrayList<ScoreEntry> scoreEntries = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            int score = preferences.getInt("HighScore" + i, 0);
            String name = preferences.getString("HighScoreName" + i, "AAA");
            scoreEntries.add(new ScoreEntry(score, name));
        }

        // Find the placeholder "---" and replace it with the actual name
        for (ScoreEntry entry : scoreEntries) {
            if (entry.name.equals("---")) {
                entry.name = playerName; // Replace "---" with player's actual name
                break;
            }
        }

        // Save the updated leaderboard
        for (int i = 0; i < scoreEntries.size(); i++) {
            editor.putInt("HighScore" + (i + 1), scoreEntries.get(i).score);
            editor.putString("HighScoreName" + (i + 1), scoreEntries.get(i).name);
        }
        editor.apply();

        messageText.setText("Name Saved: " + playerName);
        nameInput.setVisibility(View.GONE);
        saveNameButton.setVisibility(View.GONE);
    }
}



