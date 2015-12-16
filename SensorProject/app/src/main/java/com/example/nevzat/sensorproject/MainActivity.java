package com.example.nevzat.sensorproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    static final String ACTIVE_TIME="Active Time";
    static final String PASSIVE_TIME="Passive Time";
    TextView textx,texty,textz,textActive,textPassive;
    long activeTime,passiveTime;
    double sensorValue;
    double x,y,z,beforeX=0,beforeY=9.8,beforeZ=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        if (savedInstanceState!=null){
            activeTime= savedInstanceState.getLong(ACTIVE_TIME);
            passiveTime = savedInstanceState.getLong(PASSIVE_TIME);
        }

        textx = (TextView) findViewById(R.id.TextX);
        texty = (TextView)findViewById(R.id.TextY);
        textz = (TextView)findViewById(R.id.TextZ);
        textActive = (TextView) findViewById(R.id.textActive);
        textPassive = (TextView) findViewById(R.id.textPassive);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null){
            activeTime= savedInstanceState.getLong(ACTIVE_TIME);
            passiveTime = savedInstanceState.getLong(PASSIVE_TIME);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong(ACTIVE_TIME, activeTime);
        savedInstanceState.putLong(PASSIVE_TIME, passiveTime);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        Date date =new Date();
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float [] values = event.values;
            x = values[0];
            y = values[1];
            z = values[2];
            int hour,minute,second,temp;
            textx.setText(Float.toString(event.values[0]));
            texty.setText(Float.toString(event.values[1]));
            textz.setText(Float.toString(event.values[2]));
            sensorValue=Math.sqrt(Math.pow(x-beforeX,2)+ Math.pow(y-beforeY,2)+ Math.pow(z-beforeZ,2));
            if(sensorValue>6){
                activeTime+=0.23;
                date.setTime(activeTime);
                /*
                temp = (int)activeTime;
                hour = temp/3600;
                temp%=3600;
                minute=temp/60;
                temp%=60;
                second = temp;
                textActive.setText("Active Time:  "+hour+":"+minute+":"+second);
                */
                textActive.setText("Active Time:  "+ date.getHours()+":"+ date.getMinutes()+":"+ date.getSeconds());
            }
            else{
                passiveTime+=0.23;
                date.setTime(passiveTime);
                /*
                temp = (int)passiveTime;
                hour = temp/3600;
                temp%=3600;
                minute=temp/60;
                temp%=60;
                second = temp;

                textPassive.setText("Passive Time:  "+hour+":"+minute+":"+second);*/
                textPassive.setText("Passive Time:  "+ date.getHours()+":"+ date.getMinutes()+":"+ date.getSeconds());
            }
            beforeX=x;beforeY=y;beforeZ=z;
            Log.d("MainActivity", String.format("x : %f y : %f z : %f", x, y, z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
