package com.example.mathchallenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.mariuszgromada.math.mxparser.*;

import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView questionText, scoreText, resultText, difficultyText;
    private EditText answerInput;
    private Button submitButton;

    private int score = 0;
    private double correctAnswer;
    private String currentDifficulty = "Easy";  // Default difficulty
    private Random random;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize Views
        questionText = findViewById(R.id.question_text);
        scoreText = findViewById(R.id.score_text);
        resultText = findViewById(R.id.result_text);
        difficultyText = findViewById(R.id.difficulty_text);
        answerInput = findViewById(R.id.answer_input);
        submitButton = findViewById(R.id.submit_button);

        // Load animations
        final Animation scaleCorrect = AnimationUtils.loadAnimation(this, R.anim.scale_correct);
        final Animation scaleWrong = AnimationUtils.loadAnimation(this, R.anim.scale_wrong);
        final Animation flashCorrect = AnimationUtils.loadAnimation(this, R.anim.flash_correct);
        final Animation flashWrong = AnimationUtils.loadAnimation(this, R.anim.flash_wrong);

        random = new Random();

        // Load user settings
        preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        currentDifficulty = preferences.getString("Difficulty", "Easy");

        // Display the selected difficulty
        difficultyText.setText("Difficulty: " + currentDifficulty);

        // Generate the first question
        generateQuestion();

        // Handle Submit Button
        submitButton.setOnClickListener(v -> {
            String userAnswerStr = answerInput.getText().toString().trim();
            if (userAnswerStr.isEmpty()) {
                Toast.makeText(GameActivity.this, "Please enter an answer!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double userAnswer = Double.parseDouble(userAnswerStr);
                if (Math.abs(userAnswer - correctAnswer) < 0.001) {
                    // Correct Answer Animation
                    answerInput.startAnimation(scaleCorrect);
                    answerInput.startAnimation(flashCorrect);

                    score++;
                    scoreText.setText("Score: " + score);
                    resultText.setText("Correct! 🎉");
                    resultText.setTextColor(getResources().getColor(R.color.green));

                    generateQuestion();  // Load new question
                    answerInput.setText("");  // Clear input
                } else {
                    // Wrong Answer Animation
                    answerInput.startAnimation(scaleWrong);
                    answerInput.startAnimation(flashWrong);

                    resultText.setText("Wrong! ❌");
                    resultText.setTextColor(getResources().getColor(R.color.red));

                    endGame();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(GameActivity.this, "Invalid input! Enter a number.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Enter Key
        answerInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                submitButton.performClick();  // Trigger the submit action
                return true;
            }
            return false;
        });
    }

    private void endGame() {
        saveHighScore(score);
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("FINAL_SCORE", score);
        startActivity(intent);
        finish();
    }

    private void saveHighScore(int finalScore) {
        SharedPreferences preferences = getSharedPreferences("Leaderboard", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String scores = preferences.getString("scores", "0,0,0,0,0,0,0,0,0,0");
        String[] scoreArray = scores.split(",");

        int[] highScores = new int[10];
        for (int i = 0; i < scoreArray.length; i++) {
            highScores[i] = Integer.parseInt(scoreArray[i]);
        }

        // Insert the new score and sort the array
        highScores[9] = finalScore;
        Arrays.sort(highScores);
        reverseArray(highScores);

        // Save sorted scores back to preferences
        StringBuilder sb = new StringBuilder();
        for (int s : highScores) {
            sb.append(s).append(",");
        }

        editor.putString("scores", sb.toString());
        editor.apply();
    }

    // Helper function to reverse array
    private void reverseArray(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }


    // Generates a new question based on difficulty
    private void generateQuestion() {
        // Load fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // Delay the fade-out effect
        resultText.postDelayed(() -> {
            resultText.startAnimation(fadeOut);
            resultText.setText("");
        }, 1200); // Keep result text visible for 1.2 seconds before fading out

        // Retrieve min and max values from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        int min = preferences.getInt("MinRange", 1);  // Default to 1
        int max = preferences.getInt("MaxRange", 10); // Default to 10

        Random random = new Random();
        int num1 = random.nextInt((max - min) + 1) + min;
        int num2 = random.nextInt((max - min) + 1) + min;
        int num3;
        String expression = ""; // Declare before switch to avoid scope issues

        switch (currentDifficulty) {
            case "Easy":
                String[] easyOps = {"+", "-", "*", "/"};
                String easyOp = easyOps[random.nextInt(easyOps.length)];
                expression = num1 + " " + easyOp + " " + num2;
                break;

            case "Medium":
                String[] mediumOps = {"+", "-", "*"};
                String op1 = mediumOps[random.nextInt(mediumOps.length)];
                String op2 = mediumOps[random.nextInt(mediumOps.length)];
                num3 = random.nextInt((max - min) + 1) + min;
                expression = num1 + " " + op1 + " " + num2 + " " + op2 + " " + num3;
                break;

            case "Hard":
                num3 = random.nextInt((max - min) + 1) + min;
                expression = "(" + num1 + " + " + num2 + ") * " + num3;
                break;
        }

        // Set the generated question text
        questionText.setText(expression);

        // Apply the fade-in animation
        questionText.startAnimation(fadeIn);

        // Calculate answer
        correctAnswer = calculateAnswer(expression);
    }



    // Evaluates the mathematical expression using the script engine
    private double calculateAnswer(String expression) {
        Expression exp = new Expression(expression);
        return exp.calculate();
    }
}
