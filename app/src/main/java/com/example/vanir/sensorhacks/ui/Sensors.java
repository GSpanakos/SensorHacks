package com.example.vanir.sensorhacks.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.model.Sensor;


/**
 * Created by Γιώργος on 12/5/2016.
 */
public class Sensors extends AppCompatActivity {

    public FloatingActionButton fab;
    private static final String TAG = "Sensors";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (findViewById(R.id.fabadd));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: pressed!");
                AddSensorFragment addSensorFragment = new AddSensorFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack("addsensor").replace(R.id.fragment_container, addSensorFragment, null).commit();
            }
        });

        // Add sensor list fragment if this is first creation
        if (savedInstanceState == null) {
            SensorListFragment fragment = new SensorListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, SensorListFragment.TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the sensor detail fragment
     */
    public void show(Sensor sensor) {
        SensorFragment sensorFragment = SensorFragment.forSensor(sensor.getId());
        getSupportFragmentManager().beginTransaction().addToBackStack("sensor").replace(R.id.fragment_container, sensorFragment, null).commit();
    }
}


