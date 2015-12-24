package com.example.nevzat.sensorproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


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
        sensorManager.registerListener
                (this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 100);

        if (savedInstanceState!=null){
            activeTime= savedInstanceState.getLong(ACTIVE_TIME);
            passiveTime = savedInstanceState.getLong(PASSIVE_TIME);
        }

        textx = (TextView) findViewById(R.id.TextX);
        texty = (TextView)findViewById(R.id.TextY);
        textz = (TextView)findViewById(R.id.TextZ);
        textActive = (TextView) findViewById(R.id.textActive);
        textPassive = (TextView) findViewById(R.id.textPassive);
        textActive.setText("Active Time:  00:00:00");
        textPassive.setText("Passive Time:  00:00:00");
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
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 100);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float [] values = event.values;
            x = values[0];
            y = values[1];
            z = values[2];
            //int hour,minute,second,temp;
            textx.setText("X: "+x);
            texty.setText("Y: "+y);
            textz.setText("Z: "+z);
            //sensorValue=Math.sqrt(Math.pow(x,2)+ Math.pow(y,2)+ Math.pow(z,2))-Math.sqrt(Math.pow(beforeX,2)+ Math.pow(beforeY,2)+ Math.pow(beforeZ,2));

            sensorValue=Math.sqrt(Math.pow(x-beforeX,2)+ Math.pow(y-beforeY,2)+ Math.pow(z-beforeZ,2));
            if(sensorValue>0.2){
                activeTime+=10;
                //date.setTime(activeTime);
                Date date = new Date(activeTime);
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
                String dateFormatted = formatter.format(date);

                long hour = TimeUnit.MILLISECONDS.toHours(activeTime);
                activeTime -= TimeUnit.HOURS.toMillis(hour);
                long minute = TimeUnit.MILLISECONDS.toMinutes(activeTime);
                activeTime -= TimeUnit.MINUTES.toMillis(minute);
                long second = TimeUnit.MILLISECONDS.toSeconds(activeTime);
/*
                temp = (int)activeTime;
                hour = temp/3600;
                temp%=3600;
                minute=temp/60;
                temp%=60;
                second = temp;
                */
//                textActive.setText("Active Time:  "+ hour+":"+minute+":"+second);
                textActive.setText("Active Time:  "+ dateFormatted);

                //textActive.setText("Active Time:  "+ date.getHours()+":"+ date.getMinutes()+":"+ date.getSeconds());
            }
            else{
                passiveTime+=10;
                Date date = new Date(passiveTime);
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
                String dateFormatted = formatter.format(date);
                //date.setTime(passiveTime);
                long hour = TimeUnit.MILLISECONDS.toHours(passiveTime);
                passiveTime -= TimeUnit.HOURS.toMillis(hour);
                long minute = TimeUnit.MILLISECONDS.toMinutes(passiveTime);
                passiveTime -= TimeUnit.MINUTES.toMillis(minute);
                long second = TimeUnit.MILLISECONDS.toSeconds(passiveTime);
                /*
                temp = (int)passiveTime;
                hour = temp/3600;
                temp%=3600;
                minute=temp/60;
                temp%=60;
                second = temp;
                */
                //textPassive.setText("Passive Time:  "+ hour + ":" + minute + ":" + second);
                textPassive.setText("Passive Time:  "+ dateFormatted);
            }
            beforeX=x;beforeY=y;beforeZ=z;
            Log.d("MainActivity", String.format("x : %f y : %f z : %f", x, y, z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
