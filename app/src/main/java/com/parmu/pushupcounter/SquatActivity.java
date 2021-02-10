package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SquatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat);
        Button startSitupCounter = findViewById(R.id.start_situp_counter);
        startSitupCounter.setOnClickListener(v -> {
            Intent iStartSitupCounter = new Intent(SquatActivity.this, SquatCounter.class);
            startActivity(iStartSitupCounter);
        });


    }
}