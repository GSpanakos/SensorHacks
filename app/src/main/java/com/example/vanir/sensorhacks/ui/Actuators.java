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
import com.example.vanir.sensorhacks.model.Actuator;
import com.example.vanir.sensorhacks.ui.frags.ActuatorFragment;
import com.example.vanir.sensorhacks.ui.frags.ActuatorListFragment;
import com.example.vanir.sensorhacks.ui.frags.AddActuatorFragment;


/**
 * Created by Γιώργος on 16/11/2016.
 */
public class Actuators extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public FloatingActionButton fab;
    private static final String TAG = "Actuators";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);
        configureNavigationDrawer();
        configureToolbar();

        fab = (findViewById(R.id.fabActAdd));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: pressed!");
                AddActuatorFragment addActuatorFragment = new AddActuatorFragment();
                getFragmentManager().beginTransaction().addToBackStack("addactuator").replace(R.id.fragment_actuator_container, addActuatorFragment, null).commit();
            }
        });

        // Add actuator list fragment if this is first creation
        if (savedInstanceState == null) {
            ActuatorListFragment fragment = new ActuatorListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_actuator_container, fragment, ActuatorListFragment.TAG).commit();
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_act);
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
                    startsns(findViewById(itemId));
                    drawerLayout.closeDrawers();
                    return true;
                } else if (itemId == R.id.start_act) {
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
     * Shows the actuator detail fragment
     */
    public void show(Actuator actuator) {
        ActuatorFragment actuatorFragment = ActuatorFragment.forActuator(actuator.getId());
        getSupportFragmentManager().beginTransaction().addToBackStack("actuator").replace(R.id.fragment_actuator_container, actuatorFragment, null).commit();
    }

    public void startHome(View view) {
        Intent intent = new Intent(Actuators.this, MainActivity.class);
        startActivity(intent);
    }

    public void startnds(View view) {
        Intent intent = new Intent(Actuators.this, Nodes.class);
        startActivity(intent);

    }

    public void startsns(View view) {
        Intent intent = new Intent(Actuators.this, Sensors.class);
        startActivity(intent);
    }

    public void startgrp(View view) {
        Intent intent = new Intent(Actuators.this, Graphs.class);
        startActivity(intent);
    }

}
