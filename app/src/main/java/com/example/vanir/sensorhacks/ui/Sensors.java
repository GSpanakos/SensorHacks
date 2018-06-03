package com.example.vanir.sensorhacks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.model.Sensor;
import com.example.vanir.sensorhacks.ui.frags.AddSensorFragment;
import com.example.vanir.sensorhacks.ui.frags.SensorFragment;
import com.example.vanir.sensorhacks.ui.frags.SensorListFragment;


/**
 * Created by Γιώργος on 12/5/2016.
 */
public class Sensors extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public FloatingActionButton fab;
    private static final String TAG = "Sensors";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns);
        configureNavigationDrawer();
        configureToolbar();

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

    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.baseline2_menu_white_18dp);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_sns);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.home) {
                    startHome(findViewById(itemId));
                    return true;
                } else if (itemId == R.id.start_nds) {
                    startnds(findViewById(itemId));
                    drawerLayout.closeDrawers();
                    return true;
                } else if (itemId == R.id.start_sns) {
                    drawerLayout.closeDrawers();
                    return true;
                } else if (itemId == R.id.start_act) {
                    startact(findViewById(itemId));
                    drawerLayout.closeDrawers();
                    return true;
                } else if (itemId == R.id.start_grp) {
                    startgrp(findViewById(itemId));
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });

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

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
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

    public void startHome(View view) {
        Intent intent = new Intent(Sensors.this, MainActivity.class);
        startActivity(intent);
    }

    public void startnds(View view) {
        Intent intent = new Intent(Sensors.this, Nodes.class);
        startActivity(intent);

    }

    public void startact(View view) {
        Intent intent = new Intent(Sensors.this, Actuators.class);
        startActivity(intent);
    }

    public void startgrp(View view) {
        Intent intent = new Intent(Sensors.this, Graphs.class);
        startActivity(intent);
    }

}


