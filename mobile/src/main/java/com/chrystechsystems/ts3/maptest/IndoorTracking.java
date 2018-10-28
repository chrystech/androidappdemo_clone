package com.chrystechsystems.ts3.maptest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

public class IndoorTracking extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private ImageView directionImage;
    private float lastHeading = 0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_tracking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        directionImage = findViewById(R.id.directionImage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_STATUS_ACCURACY_HIGH);/*.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR,//Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);*/

    }
    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.err.println("Trying to get to this heading: ");
        Beacon target = Beacon.getBeacon(InitialDisplay.building, InitialDisplay.room);
        LatLng coordinates = target.getCoordinates();
        int currentBearing = Math.round(event.values[0]);
        System.err.println(coordinates);
        //System.err.println("event.values: " + event.values);
        System.err.print("event.values: ");
        for(int i = 0; i < event.values.length; i++){
            System.err.print(event.values[i] + " | ");
        }
        System.err.println();
        System.err.println("Bearing: " + Math.round((event.values[0])));
        System.out.println("LAST LOCATION KNOWN: " + MapsActivity.lastRecordedLocation);
        System.out.println("TARGET LOCATION -->: " + coordinates);
        RotateAnimation ra = new RotateAnimation(lastHeading,-currentBearing,Animation.RELATIVE_TO_SELF, 0.5f,

                Animation.RELATIVE_TO_SELF,

                0.5f);
        ra.setDuration(210);
        ra.setFillAfter(true);
        directionImage.startAnimation(ra);
        lastHeading = -Math.round((event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
