package com.parmu.pushupcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        bottomNavigation();
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
    @SuppressLint("NonConstantResourceId")
    private void bottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.pushup_item:
                    Intent iPushup = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(iPushup);
                    return true;
                case R.id.squat_item:
                    Intent iSquat = new Intent(getApplicationContext(), SquatActivity.class);
                    startActivity(iSquat);
                case R.id.more_item:
                    Toast.makeText(getApplicationContext(),"more settings",Toast.LENGTH_SHORT).show();

            }
            return false;
        });
    }
}