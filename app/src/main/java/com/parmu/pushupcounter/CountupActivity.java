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
    private Sensor mAccelerometerSensor;
    private Sensor mMagnetometerSensor;
    private TextView liveCountTextView;
    private Button finishCountingButton;
    int numberOfPushups = -1;
    Intent iFinishCounting;
    private float pitch;
    private float recentValueOfPitch = (float) 0.60;
    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];
    private float mProximityData;

    final static String PREF_HIGH_SCORE_FILE_NAME_2 = "com.parmu.pushupcounter.HighScore";
    SharedPreferences prefHighScore2;
    SharedPreferences.Editor editorHighScore2;
    int highScore2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        liveCountTextView = findViewById(R.id.text_view_live_count);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) {
            Toast.makeText(getApplicationContext(), "Proximity sensor not available on device", Toast.LENGTH_LONG).show();
        }
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
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
        int sensorType = event.sensor.getType();
        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = event.values.clone();
                break;
            case Sensor.TYPE_PROXIMITY:
                mProximityData = event.values[0];
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
//        float azimuth = orientationValues[0];
        pitch = Math.abs(orientationValues[1]);   //pitch
//        float roll = orientationValues[2];

        if(pitch<0.20 && recentValueOfPitch>0.40 && mProximityData < mProximitySensor.getMaximumRange()){

            numberOfPushups++;
            liveCountTextView.setText(String.valueOf(numberOfPushups));
            recentValueOfPitch = pitch;

        }
        if(pitch > 0.40 && recentValueOfPitch < 0.20 && mProximityData < mProximitySensor.getMaximumRange()){
            recentValueOfPitch = pitch;
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
        if (mAccelerometerSensor != null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mMagnetometerSensor != null) {
            mSensorManager.registerListener(this, mMagnetometerSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mProximitySensor != null) {
            mSensorManager.registerListener(this, mProximitySensor,
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
        highScore2 = prefHighScore2.getInt("highscore",1);
        editorHighScore2 = prefHighScore2.edit();
        if(numberOfPushups > highScore2){
            highScore2= numberOfPushups;
            editorHighScore2.putInt("highscore",highScore2);
            editorHighScore2.apply();
        }
    }

}
