package com.example.vanir.sensorhacks.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.model.Sensor;
import com.example.vanir.sensorhacks.ui.frags.AddSensorFragment;
import com.example.vanir.sensorhacks.ui.frags.SensorFragment;
import com.example.vanir.sensorhacks.ui.frags.SensorListFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;


/**
 * Created by Γιώργος on 12/5/2016.
 */
public class Sensors extends AppCompatActivity {

    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private final String DEVICE_ADDRESS = "98:D3:31:FC:9E:6A";
    private boolean deviceConnected = false;
    private byte buffer[];
    private StringBuffer stringBuffer;
    private InputStream inputStream;
    private OutputStream outputStream;
    public DrawerLayout drawerLayout;
    public FloatingActionButton fab;
    private static final String TAG = "Sensors";
    private boolean stopThread;
    private BluetoothSocket socket;
    // private String DEVICE_ADDRESS = null;
    private BluetoothDevice device;

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
        SensorFragment sensorFragment = SensorFragment.forSensor((SensorEntity) sensor);
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

    public void onConnect(View v) {
        if (BTinit()) {
            int flag = 0;
            if (BTconnect()) {
                deviceConnected = true;
                beginListenForData();
                Log.d(TAG, "\nonDownloadData: Connection Opened!\n");
                Toast.makeText(getApplicationContext(), "Connection Established", Toast.LENGTH_SHORT).show();
                //Snackbar.make(v, "Connection Established", Snackbar.LENGTH_LONG).show();
                flag = 1;
            }
            if (flag == 0) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                //Snackbar.make(v, "Something went wrong", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void onSendParametersForFetch(View v) {

        String string = (SensorFragment.mSensor.getName());
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // textView.append("\nSent Data:" + stringBuffer + "\n");

        Log.i(TAG, "onSendParametersForFetch: Data Sent" + string);

    }

    private boolean BTconnect() {
        boolean connected = true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
        if (connected) {
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }

    private boolean BTinit() {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
            //Log.i(TAG, "\nBTinit: Device doesnt Support Bluetooth\n");
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                try {
                    if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                        device = iterator;
                        found = true;
                        break;
                    }
                } catch (Exception device) {
                    Snackbar.make(findViewById(R.id.fetch_sensor_data), "Set device mac adress first from settings", Snackbar.LENGTH_LONG).show();
                }
            }
        }
        return found;
    }

    public void beginListenForData() {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        stringBuffer = new StringBuffer(buffer.length);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        int byteCount = inputStream.available();
                        if (byteCount > 0) {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string = new String(rawBytes, "UTF-8");
                            Sensors.this.stringBuffer.append(string);
                            //stringBuffer.append(read);
                            Log.i(TAG, "papakia: " + Sensors.this.stringBuffer);
                            String sstring[] = stringBuffer.toString().split("~");
                            Log.i(TAG, "SPLIIIIIIIIIIT" + Arrays.toString(sstring));

                            /** usefull only for UI calls, like textview editing **/
//                            handler.post(new Runnable() {
//                                public void run() {
//                                    //Toast.makeText(getApplicationContext(), "next measure: " + stringBuffer, Toast.LENGTH_SHORT).show();
//                                    //Log.i(TAG, "runs: " + string);
//                                    //textView.append(stringBuffer);
//                                }
//                            });

                        }
                    } catch (IOException ex) {
                        Log.i(TAG, "catch exception");
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopThread = true;
//        try {
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        deviceConnected = false;
//        Log.i(TAG, "\nonPause: Connection Closed!\n");
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread = true;
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deviceConnected = false;
        Log.i(TAG, "\nonDestroy: Connection Closed!\n");
    }
}


