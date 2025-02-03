package com.example.mathchallenge;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup difficultyGroup;
    private RadioButton easyButton, mediumButton, hardButton;
    private EditText minRangeInput, maxRangeInput;
    private Button saveButton;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Views
        difficultyGroup = findViewById(R.id.difficulty_group);
        easyButton = findViewById(R.id.radio_easy);
        mediumButton = findViewById(R.id.radio_medium);
        hardButton = findViewById(R.id.radio_hard);
        minRangeInput = findViewById(R.id.min_range_input);
        maxRangeInput = findViewById(R.id.max_range_input);
        saveButton = findViewById(R.id.save_button);
        TextView settingsTitle = findViewById(R.id.settings_title);

        // Load the drawable images
        Drawable leftDash = ContextCompat.getDrawable(this, R.drawable.ic_dash);
        Drawable rightDash = ContextCompat.getDrawable(this, R.drawable.ic_dash);


        // Set new bounds for the drawable (adjust width & height as needed)
        int width = 90;  // Adjust width
        int height = 5;  // Adjust height
        leftDash.setBounds(0, 0, width, height);
        rightDash.setBounds(0, 0, width, height);

        // Apply drawables to TextView
        settingsTitle.setCompoundDrawables(leftDash, null, rightDash, null);

        // Load saved settings
        preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        loadSettings();

        // Save Button Action
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    // Load previous settings
    private void loadSettings() {
        String difficulty = preferences.getString("Difficulty", "Easy");
        int minRange = preferences.getInt("MinRange", 1);
        int maxRange = preferences.getInt("MaxRange", 50);

        // Set saved difficulty
        if (difficulty.equals("Easy")) {
            easyButton.setChecked(true);
        } else if (difficulty.equals("Medium")) {
            mediumButton.setChecked(true);
        } else {
            hardButton.setChecked(true);
        }

        // Set saved number range
        minRangeInput.setText(String.valueOf(minRange));
        maxRangeInput.setText(String.valueOf(maxRange));
    }

    // Save settings to SharedPreferences
    private void saveSettings() {
        String selectedDifficulty = "Easy";
        int selectedId = difficultyGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radio_easy) {
            selectedDifficulty = "Easy";
        } else if (selectedId == R.id.radio_medium) {
            selectedDifficulty = "Medium";
        } else if (selectedId == R.id.radio_hard) {
            selectedDifficulty = "Hard";
        }

        String minInput = minRangeInput.getText().toString();
        String maxInput = maxRangeInput.getText().toString();

        if (minInput.isEmpty() || maxInput.isEmpty()) {
            Toast.makeText(this, "Please enter both Min and Max range!", Toast.LENGTH_SHORT).show();
            return;
        }

        int minRange = Integer.parseInt(minInput);
        int maxRange = Integer.parseInt(maxInput);

        if (minRange >= maxRange) {
            Toast.makeText(this, "Min value must be less than Max value!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Difficulty", selectedDifficulty);
        editor.putInt("MinRange", minRange);
        editor.putInt("MaxRange", maxRange);
        editor.apply();

        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
        finish();  // Close the settings screen
    }


}
