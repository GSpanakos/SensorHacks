package com.example.vanir.sensorhacks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.vanir.sensorhacks.R;


/**
 * Created by Γιώργος on 18/10/2017.
 */
public class Graphs extends AppCompatActivity {
    public static final String TAGB = "Start Bar Chart";
    public static final String TAGL = "Start Line Chart";
    public LinearLayout barLayout, linelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp);
        barLayout = (LinearLayout) (findViewById(R.id.bar_chart));
        linelayout = (LinearLayout) (findViewById(R.id.line_chart));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        barLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().addToBackStack(TAGB).add(R.id.chart_container, new BarChartFragment(), TAGB).commit();
                Log.d(null, "onClick: gamw to spiti sou");
            }
        });

        linelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().addToBackStack(TAGL).replace(R.id.chart_container, new LineChartFragment(), TAGL).commit();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
