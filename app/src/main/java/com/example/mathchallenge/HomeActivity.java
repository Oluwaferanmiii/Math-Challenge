package com.example.mathchallenge;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {

    private Button startGameButton, leaderboardButton;
    private ImageView settingsButton, appLogo;

    private TextView homeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Buttons
        startGameButton = findViewById(R.id.btn_start_game);
        settingsButton = findViewById(R.id.btn_settings);
        leaderboardButton = findViewById(R.id.btn_leaderboard);
        homeTitle = findViewById(R.id.home_title);
        appLogo = findViewById(R.id.logo_image);
        TextView title = findViewById(R.id.home_title);

        // Paint App Title
        Paint paint = title.getPaint();
        Shader textShader = new LinearGradient(
                0, 0, 0, title.getTextSize(),
                new int[]{Color.parseColor("#FFD700"), Color.parseColor("#FFA500")},
                null, Shader.TileMode.CLAMP);
        paint.setShader(textShader);


        // Animations
        Animation fadeBounce = AnimationUtils.loadAnimation(this, R.anim.fade_bounce);
        Animation scaleUpPop = AnimationUtils.loadAnimation(this, R.anim.scale_up_pop);
        Animation wobble = AnimationUtils.loadAnimation(this, R.anim.wobble);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);


        homeTitle.startAnimation(wobble);
        appLogo.startAnimation(scaleUpPop);
        appLogo.startAnimation(rotateAnimation);

        // Navigate to GameActivity
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(HomeActivity.this, GameActivity.class);
                startActivity(gameIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Navigate to SettingsActivity
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Navigate to LeaderboardActivity
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderboardIntent = new Intent(HomeActivity.this, LeaderboardActivity.class);
                startActivity(leaderboardIntent);
            }
        });
    }
}
