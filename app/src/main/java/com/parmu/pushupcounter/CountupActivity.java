package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;

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

public class CountupActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private TextView liveCountTextView;
    private Button finishCountingButton;
    int numberOfPushups=0;
    Intent iFinishCounting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countup);

        liveCountTextView = findViewById(R.id.text_view_live_count);
        mSensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) {
            Toast.makeText(getApplicationContext(), "Proximity sensor not available on device", Toast.LENGTH_LONG).show();
        }
        mProximitySensor= mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        finishCountingButton = findViewById(R.id.button_finish_counting);
        finishCountingButton.setOnClickListener( v-> {
            iFinishCounting = new Intent(CountupActivity.this, MainActivity.class);
            iFinishCounting.putExtra("numberofpushups",numberOfPushups);
            startActivity(iFinishCounting);
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Do something with sensor data

        if(mSensorManager != null && event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            for(int i=0; i<1; i++){
                if(event.values[0] >= mProximitySensor.getMaximumRange()){
                    //Far
                    Toast.makeText(getApplicationContext(),"Far", Toast.LENGTH_SHORT).show();
                    numberOfPushups++;
                    liveCountTextView.setText(String.valueOf(numberOfPushups));
                }
                else{
                    //Near
                    Toast.makeText(getApplicationContext(),"Near",Toast.LENGTH_SHORT).show();
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
        mSensorManager.registerListener(this,mProximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //unregister the listener when activity pauses
        mProximitySensor = null;
        mSensorManager.unregisterListener(this);

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        //if paused then back to mainactivity with the result
        iFinishCounting = new Intent(CountupActivity.this, MainActivity.class);
        iFinishCounting.putExtra("numberofpushups",numberOfPushups);
        startActivity(iFinishCounting);

    }

}