package com.example.vanir.sensorhacks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();


        switch (intent.getStringExtra("destinationChart")) {
            case "LineChartFragment":
                LineChartFragment lineChartFragment = new LineChartFragment();
                Bundle args = new Bundle();
                args.putInt("id", getIntent().getExtras().getInt("id"));
                args.putString("name", getIntent().getExtras().getString("name"));
                lineChartFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().addToBackStack("lf").replace(R.id.chart_container, lineChartFragment, null).commit();
                break;
            case "BarChartFragment":
                BarChartFragment barChartFragment = new BarChartFragment();
                Bundle args2 = new Bundle();
                args2.putInt("id", getIntent().getExtras().getInt("id"));
                args2.putString("name", getIntent().getExtras().getString("name"));
                barChartFragment.setArguments(args2);
                break;
            default:
                if (savedInstanceState == null) {
                    GraphsFrag fragment = new GraphsFrag();
                    getSupportFragmentManager().beginTransaction().add(R.id.chart_container, fragment, null).commit();
                }
        }

        if (savedInstanceState == null) {
            GraphsFrag fragment = new GraphsFrag();
            getSupportFragmentManager().beginTransaction().add(R.id.chart_container, fragment, null).commit();
        }  //else if (Objects.equals(getIntent().getStringExtra("EXTRA"), "here goes info of id and name")) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.chart_container, new LineChartFragment()).commit();
//        }
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
