package com.example.vanir.sensorhacks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.ui.frags.BarChartFragment;
import com.example.vanir.sensorhacks.ui.frags.GraphsFrag;
import com.example.vanir.sensorhacks.ui.frags.LineChartFragment;

import java.util.Objects;


/**
 * Created by Γιώργος on 18/10/2017.
 */
public class Graphs extends AppCompatActivity {

    private static final String TAG = "Graphs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        if (intent.getStringExtra("destinationChart") != null) {
            Bundle bd = intent.getExtras();
            switch (intent.getStringExtra("destinationChart")) {
                case "LineChartFragment":
                    LineChartFragment lineChartFragment = new LineChartFragment();
                    Bundle args = new Bundle();
                    if (bd != null) {
                        args.putInt("id", bd.getInt("id"));
                        args.putString("name", bd.getString("name"));
                        args.putString("destinationChart", bd.getString("destinationChart"));
                        lineChartFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().addToBackStack("lf").replace(R.id.chart_container, lineChartFragment, null).commit();
                    } else {
                        Log.i(TAG, "onCreate: no id or name provided");
                    }
                    break;
                case "BarChartFragment":
                    BarChartFragment barChartFragment = new BarChartFragment();
                    Bundle args2 = new Bundle();
                    if (bd != null) {
                        args2.putInt("id", bd.getInt("id"));
                        args2.putString("name", bd.getString("name"));
                        barChartFragment.setArguments(args2);
                    } else {
                        Log.i(TAG, "onCreate: no id or name provided");
                    }
                    break;
            }
        } else {
            if (savedInstanceState == null) {
                GraphsFrag fragment = new GraphsFrag();
                getSupportFragmentManager().beginTransaction().replace(R.id.chart_container, fragment, null).commit();
            }
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

}
