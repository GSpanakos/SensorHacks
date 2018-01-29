package com.example.vanir.sensorhacks.ui;

import android.database.Cursor;
import android.os.Bundle;
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

import java.util.ArrayList;


/**
 * Created by Γιώργος on 12/5/2016.
 */
public class Sensors extends AppCompatActivity {

    private SensorDB sensorDB;
    private AppDatabase database;
    private Cursor mCursor;

    private ListView mlistView;
    private Button mAddItem;
    private ArrayList<SensorDB> msensor;
    private EditText mNameTxt, mTypeTxt, mStatusTxt, mValueTxt;
    private CustomSensorsAdapter mcustomSensorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns);
//      populateSensorsList();
        initViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = AppDatabase.getDatabase(getApplicationContext());

//      cache inflated edittext views
        mNameTxt = (EditText) findViewById(R.id.edit_sns_listview_name);
        mTypeTxt = (EditText) findViewById(R.id.edit_sns_listview_type);
        mStatusTxt = (EditText) findViewById(R.id.edit_sns_listview_status);
        mValueTxt = (EditText) findViewById(R.id.edit_sns_listview_value);

        mlistView = (ListView) findViewById(R.id.lvSensors);
        msensor = new ArrayList<SensorDB>();

        msensor = database.sensorDAO().getallSensor();
        if (msensor.size() == 0) {
            int flag = 1;
            //           database.sensorDAO().addSensor(new SensorDB(mNameTxt.getText().toString(), mTypeTxt.getText().toString(), Boolean.parseBoolean(mStatusTxt.getText().toString()), Double.parseDouble(mValueTxt.getText().toString())));
        } else if (msensor.size() > 0) {

        }

//        msensor.add(new Sensor("DHT23", "Temp", false , 36.8));
//        msensor.add(new Sensor("GTY42", "Press", true, 105.32));
//        msensor.add(new Sensor("KJH79", "Humid", true, 78));

//        mcustomSensorsAdapter = new CustomSensorsAdapter(msensor, getApplicationContext());
        mcustomSensorsAdapter = new CustomSensorsAdapter(this, mCursor);
        mlistView.setAdapter(mcustomSensorsAdapter);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SensorDB another_sensor = msensor.get(position);
                Snackbar.make(view, another_sensor.getName() + "\n It measures: " + another_sensor.getType() + ", It is: " + another_sensor.getStatus() + ", It's value is: " + another_sensor.getValue(), Snackbar.LENGTH_LONG).setAction("No Action", null).setDuration(5000).show();
            }
        });

    }

    private void initViews() {
        mAddItem = (Button) findViewById(R.id.add_sensor);
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_sensor:
                        addItemsToList();
                        break;
                }
            }
        });

    }

    private void addItemsToList() {

        Boolean eStatus = null;
        double eValue = 0;

        switch (mStatusTxt.getText().toString()) {
            case "On":
                eStatus = true;
                break;
            case "on":
                eStatus = true;
                break;
            case "Off":
                eStatus = false;
                break;
            case "off":
                eStatus = false;
                break;
            case "True":
                eStatus = true;
                break;
            case "true":
                eStatus = true;
                break;
            case "False":
                eStatus = false;
                break;
            case "false":
                eStatus = false;
                break;
        }

        //cleanup
        //database.sensorDAO().removeAllSensor();


//        msensor.add(new Sensor(mNameTxt.getText().toString(), mTypeTxt.getText().toString(), eStatus, Double.parseDouble(mValueTxt.getText().toString())));

//      mcustomSensorsAdapter = new CustomSensorsAdapter(msensor, getApplicationContext());
//        mcustomSensorsAdapter.notifyDataSetChanged();
        mcustomSensorsAdapter.changeCursor(mCursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

//    private void populateSensorsList() {
//        //Construct the data source
//        ArrayList<Sensor> arrayOfSensors = Sensor.getSensors();
//        // Create the adapter to convert the array to views
//        CustomSensorsAdapter customSensorsAdapter = new CustomSensorsAdapter(arrayOfSensors, this);
//        // Attach the adapter to a ListView
//        ListView listView = (ListView) findViewById(R.id.lvSensors);
//        listView.setAdapter(customSensorsAdapter);
//
//    }


}


