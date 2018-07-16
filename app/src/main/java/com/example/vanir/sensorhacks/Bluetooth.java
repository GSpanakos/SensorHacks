package com.example.vanir.sensorhacks;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.vanir.sensorhacks.db.SensorValueEntity;
import com.example.vanir.sensorhacks.ui.Sensors;
import com.example.vanir.sensorhacks.viewmodel.SensorViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Γιώργος on 17/6/2018.
 */

public class Bluetooth {

    public static boolean deviceConnected;
    private static final String TAG = "Bluetooth";
    private Context mContext;
    public static OutputStream outputStream;
    public static InputStream inputStream;
    public static BluetoothSocket socket;
    private BluetoothDevice device;
    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private final String DEVICE_ADDRESS = "98:D3:31:FC:9E:6A";
    public static boolean stopThread;
    private byte[] buffer;
    private StringBuffer stringBuffer;
    private static DataRepository mRepository;
    private static SimpleDateFormat simpleDateFormat;


    public Bluetooth(DataRepository repository, Context context) {
        mContext = context;
        mRepository = repository;
    }


    public Boolean onConnect() {
        Boolean flag = false;
        if (BTinit()) {
            if (BTconnect()) {
                deviceConnected = true;
                beginListenForData();
                Log.d(TAG, "\n onDownloadData: Connection Opened!\n");
                Toast.makeText(mContext, "Connection Established", Toast.LENGTH_SHORT).show();
                //Snackbar.make(v, "Connection Established", Snackbar.LENGTH_LONG).show();
                flag = true;
            }
            if (!flag) {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                //Snackbar.make(v, "Something went wrong", Snackbar.LENGTH_LONG).show();
            }
        }
        return flag;
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
            Toast.makeText(mContext, "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
            //Log.i(TAG, "\nBTinit: Device doesnt Support Bluetooth\n");
        }
        assert bluetoothAdapter != null;
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            if (mContext instanceof Activity) {
                ((Activity) mContext).startActivityForResult(enableAdapter, 0);
            } else {
                Log.e(TAG, "BTinit: mContext should be an instanceof Activity");
            }
            //startActivityForResult(enableAdapter, 0);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices.isEmpty()) {
            Toast.makeText(mContext, "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                try {
                    if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                        device = iterator;
                        found = true;
                        break;
                    }
                } catch (Exception device) {
                    Toast.makeText(mContext, "Set device mac address first from settings", Toast.LENGTH_LONG).show();
                }
            }
        }
        return found;
    }

    private void beginListenForData() {
        //final Handler handler = new Handler();
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
                            final String string = new String(rawBytes);
                            stringBuffer.append(string);

                            Log.i(TAG, "papakia: " + Bluetooth.this.stringBuffer);

                            if ((stringBuffer.length()) > 50) {
                                stringBuffer = new StringBuffer(buffer.length);
                            }

                            String sstring[] = stringBuffer.toString().split("~");
                            Log.i(TAG, "SPLIIIIIIIIIIT" + Arrays.toString(sstring));
                            if (sstring.length > 1) {
                                updateValue(sstring[sstring.length - 2]);
                            }

                            Log.i(TAG, "an doulevei allagi sensorid: " + stringBuffer);

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

    private void updateValue(String string) {
        UpdateSensorValueTask task = new UpdateSensorValueTask();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                task.execute(Double.parseDouble(string));
            }
        });
    }

    private static class UpdateSensorValueTask extends AsyncTask<Double, Void, Void> {

        @Override
        protected Void doInBackground(Double... doubles) {
            mRepository.updateSensorValue(doubles[0], Sensors.mSensorId);

            Date date = Calendar.getInstance().getTime();
            SensorValueEntity mockValue = mRepository.getLastSensorEntry();
            SimpleDateFormat secFormat = new SimpleDateFormat("mmss");

            if (mockValue == null) {
                mRepository.insertSensorValue(new SensorValueEntity(Sensors.mSensorId, Sensors.mSensorName, date, doubles[0]));
                mockValue = mRepository.getLastSensorEntry();
            }


//            Log.i(TAG, "doInBackground: TWRA - DB = " + secFormat.format(date) + " - " + secFormat.format(mockValue.getDate()));
//            Log.i(TAG, "doInBackground: DIAFORA: " + (Long.parseLong(secFormat.format(date)) - Long.parseLong(secFormat.format(mockValue.getDate()))));

            if ((Long.parseLong(secFormat.format(date)) - Long.parseLong(secFormat.format(mockValue.getDate()))) < 3) {
                Log.i(TAG, "doInBackground: skipped entry due to same date");
            } else {
//                Log.i(TAG, "Metrhseis pou THA mpoun: ID: " + Sensors.mSensorId + " NAME: " + Sensors.mSensorName + " DATE: " + date + " VALUE: " + doubles[0]);
                mRepository.insertSensorValue(new SensorValueEntity(Sensors.mSensorId, Sensors.mSensorName, date, doubles[0]));
            }


//            Log.i(TAG, "doInBackground: TELEFTEO ENTRY: ID: " + mockValue.getId() + " NAME: " + mockValue.getName() + " DATE: " + mockValue.getDate() + " VALUE: " + mockValue.getValue());


            return null;
        }
    }


}
