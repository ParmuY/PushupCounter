package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent mIntent = new Intent(SplashActivity.this,MainActivity.class);
        Timer timerSplash = new Timer();
        timerSplash.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(mIntent);
            }
        },7000);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_splash_screen);
        floatingActionButton.setOnClickListener(view -> {
            startActivity(mIntent);
        });

    }
}