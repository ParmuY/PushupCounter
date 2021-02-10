package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Integer.valueOf;

public class CountupActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private TextView liveCountTextView;
    private Button finishCountingButton;
    int numberOfPushups = -1;
    Intent iFinishCounting;
    final static String PREF_HIGH_SCORE_FILE_NAME_2 = "com.parmu.pushupcounter.HighScore";
    SharedPreferences prefHighScore2;
    SharedPreferences.Editor editorHighScore2;
    int highScore2;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countup);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        liveCountTextView = findViewById(R.id.text_view_live_count);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) {
            Toast.makeText(getApplicationContext(), "Proximity sensor not available on device", Toast.LENGTH_LONG).show();
        }
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        finishCountingButton = findViewById(R.id.button_finish_counting);
        finishCountingButton.setOnClickListener(v -> {
            iFinishCounting = new Intent(CountupActivity.this, MainActivity.class);
            iFinishCounting.putExtra("numberofpushups", numberOfPushups);
            highScoreSetting();
            startActivity(iFinishCounting);
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Do something with sensor data

        if (mSensorManager != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            for (int i = 0; i < 1; i++) {
                if (event.values[0] >= mProximitySensor.getMaximumRange()) {
                    //Far
                    Toast.makeText(getApplicationContext(), "Far", Toast.LENGTH_SHORT).show();
                    numberOfPushups++;
                    liveCountTextView.setText(String.valueOf(numberOfPushups));
                }

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do here something if accuracy changes
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register a listener for sensor
        mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister the listener when activity pauses
        mProximitySensor = null;
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mSensorManager.unregisterListener(this);
        //if paused then back to mainactivity with the result
        iFinishCounting = new Intent(CountupActivity.this, MainActivity.class);
        iFinishCounting.putExtra("numberofpushups", numberOfPushups);
        highScoreSetting();
        startActivity(iFinishCounting);
    }

    private void highScoreSetting(){
        prefHighScore2 = getSharedPreferences(PREF_HIGH_SCORE_FILE_NAME_2,MODE_PRIVATE);
        highScore2 = prefHighScore2.getInt("highscore",0);
        editorHighScore2 = prefHighScore2.edit();
        if(numberOfPushups > highScore2){
            highScore2= numberOfPushups;
            editorHighScore2.putInt("highscore",highScore2);
            editorHighScore2.apply();
        }
    }

}
