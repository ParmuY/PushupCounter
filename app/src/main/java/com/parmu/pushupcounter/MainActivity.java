package com.parmu.pushupcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private TextView counterTextView;
    private Button startCounterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterTextView = findViewById(R.id.text_view_counter);
        startCounterButton = findViewById(R.id.button_start_counter);

        mSensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximitySensor= mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Do something with sensor data
        if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if(event.values[0]< mProximitySensor.getMaximumRange()){
                //Near
                Toast.makeText(getApplicationContext(),"Near", Toast.LENGTH_SHORT).show();
            }
            else{
                //Far
                Toast.makeText(getApplicationContext(),"Far",Toast.LENGTH_SHORT).show();
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
        mSensorManager.unregisterListener(this);
    }
}