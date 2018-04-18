package com.example.vanir.sensorhacks.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.model.Sensor;


/**
 * Created by Γιώργος on 12/5/2016.
 */
public class Sensors extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns);

        // Add sensor list fragment if this is first creation
        if (savedInstanceState == null) {
            SensorListFragment fragment = new SensorListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, SensorListFragment.TAG).commit();
        }
    }

    /**
     * Shows the sensor detail fragment
     */
    public void show(Sensor sensor) {

        SensorFragment sensorFragment = SensorFragment.forSensor(sensor.getId());
        getSupportFragmentManager().beginTransaction().addToBackStack("sensor").replace(R.id.fragment_container, sensorFragment, null).commit();
    }
}


