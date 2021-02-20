package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SquatCounter extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;
    private Sensor mSensorProximity;
    private int numberOfSquat =-1;
    private TextView liveSquatCount;
    private float pitch;
    Intent iFinishCountingOfSquat;
    private float recentValueOfPitch= (float) 0.95;
    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];
    final static String PREF_HIGH_SCORE_FILE_NAME_2 = "com.parmu.pushupcounter.HighScore";
    private SharedPreferences prefHighScoreSquat;
    private SharedPreferences.Editor editorHighScoreSquat;
    private int highScoreSquat;
    private float mProximityData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat_counter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Squat Counter");
        toolbar.setBackgroundColor(getResources().getColor(R.color.purple_shade));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        liveSquatCount = findViewById(R.id.squat_live_count);
        Button finishCountOfSquatButton = findViewById(R.id.squat_finish_counting);
        finishCountOfSquatButton.setOnClickListener(v->{
            iFinishCountingOfSquat = new Intent(SquatCounter.this, SquatActivity.class);
            iFinishCountingOfSquat.putExtra("numberofsquat", numberOfSquat);
            highScoreSetting();
            startActivity(iFinishCountingOfSquat);
        });

        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_MAGNETIC_FIELD);
        mSensorProximity = mSensorManager.getDefaultSensor(
                Sensor.TYPE_PROXIMITY);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_PROXIMITY:
                mProximityData = sensorEvent.values[0];
                break;
            default:
                return;
        }
        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);
        float[] orientationValues = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }
        float azimuth = orientationValues[0];
        pitch = Math.abs(orientationValues[1]);   //pitch
        float roll = orientationValues[2];

        //here start the main or important code
        if(pitch<0.60 && recentValueOfPitch>0.90 && mProximityData < mSensorProximity.getMaximumRange()){

            numberOfSquat++;
            liveSquatCount.setText(String.valueOf(numberOfSquat));
            recentValueOfPitch = pitch;

        }
        if(pitch > 0.90 && recentValueOfPitch < 0.60 && mProximityData < mSensorProximity.getMaximumRange()){
            recentValueOfPitch = pitch;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorAccelerometer != null) {
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorMagnetometer != null) {
            mSensorManager.registerListener(this, mSensorMagnetometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorProximity != null) {
            mSensorManager.registerListener(this, mSensorProximity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister the listener when activity pauses
        mSensorManager.unregisterListener(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unregister all sensor listeners in this callback so they don't
        // continue to use resources when the app is stopped.
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mSensorManager.unregisterListener(this);
        highScoreSetting();
//      if paused then back to squatActivity with the result
        iFinishCountingOfSquat = new Intent(SquatCounter.this, SquatActivity.class);
        iFinishCountingOfSquat.putExtra("numberofsquat", numberOfSquat);
        startActivity(iFinishCountingOfSquat);
    }

        private void highScoreSetting(){
        prefHighScoreSquat = getSharedPreferences(PREF_HIGH_SCORE_FILE_NAME_2,MODE_PRIVATE);
        highScoreSquat = prefHighScoreSquat.getInt("squathighscore",1);
        editorHighScoreSquat = prefHighScoreSquat.edit();
        if(numberOfSquat > highScoreSquat){
            highScoreSquat= numberOfSquat;
            editorHighScoreSquat.putInt("squathighscore",highScoreSquat);
            editorHighScoreSquat.apply();
        }
    }
}