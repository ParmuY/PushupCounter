package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SquatActivity extends AppCompatActivity {
    final static String PREF_HIGH_SCORE_FILE_NAME = "com.parmu.pushupcounter.HighScore";
    private  SharedPreferences prefHighScoreOfSquat;
    private int highScoreSquat;
    private TextView recentSquatTextView;
     int numberOfSquat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat);

        recentSquatTextView = findViewById(R.id.squat_count_display);
        ImageButton refreshButton = findViewById(R.id.squat_refresh);
        refreshButton.setOnClickListener(v -> {numberOfSquat=0; recentSquatTextView.setText(String.valueOf(numberOfSquat));  });
        Button startSitupCounter = findViewById(R.id.start_situp_counter);
        startSitupCounter.setOnClickListener(v -> {
            Intent iStartSitupCounter = new Intent(SquatActivity.this, SquatCounter.class);
            startActivity(iStartSitupCounter);
        });
        numberOfSquat =  getIntent().getIntExtra("numberofsquat",0);
        recentSquatTextView.setText(String.valueOf(numberOfSquat));

    }
}