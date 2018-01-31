package com.example.vanir.sensorhacks.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.vanir.sensorhacks.AppDatabase;
import com.example.vanir.sensorhacks.CustomSensorsAdapter;
import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.SensorDB;
import com.example.vanir.sensorhacks.model.Sensor;

import java.util.ArrayList;


/**
 * Created by Γιώργος on 12/5/2016.
 */
public class Sensors extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            SensorListFragment fragment = new SensorListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, SensorListFragment.TAG).commit();
        }
    }

    /**
     * Shows the product detail fragment
     */
    public void show(Sensor sensor) {

        SensorFragment sensorFragment = SensorFragment.forSensor(sensor.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("sensor")
                .replace(R.id.fragment_container,
                        sensorFragment, null).commit();
    }
}


