package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    int numberOfPushups;
    TextView recentPushupsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recentPushupsTextView = findViewById(R.id.text_view_count_display);
        Button startCounterButton = findViewById(R.id.button_start_counter);
        startCounterButton.setOnClickListener(v -> {
            Intent iCountup = new Intent(MainActivity.this, CountupActivity.class);
            startActivity(iCountup);
        });
        //numberof pushups got from countup activity as intentextra
        numberOfPushups =  getIntent().getIntExtra("numberofpushups",0);
        recentPushupsTextView.setText(String.valueOf(numberOfPushups));
    }

}