package com.example.vanir.sensorhacks.ui.frags;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.LinechartFragmentBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.List;

/**
 * Created by Γιώργος on 21/5/2018.
 */

public class LineChartFragment extends Fragment implements SensorEventListener {

    private static final String TAG = "LineChartFragment";
    public LinechartFragmentBinding mBinding;
    private boolean plotData;
    private Thread thread;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    //private LineChart mChart;
    //public View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        //view = inflater.inflate(R.layout.linechart_fragment, container, false);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.linechart_fragment, container, false);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        List<Sensor> hardwareSensors = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);

        for (int i = 0; i < hardwareSensors.size(); i++) {
            Log.d(TAG, "onCreateView: HardwareSesnor " + i + ": " + hardwareSensors.get(i).toString());

        }

        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        //mChart = (LineChart) getActivity().findViewById(R.id.linechart_example);
        mBinding.linechartExample.getDescription().setEnabled(true);
        mBinding.linechartExample.setTouchEnabled(true);
        mBinding.linechartExample.setDragEnabled(true);
        mBinding.linechartExample.setScaleEnabled(true);
        mBinding.linechartExample.setDrawGridBackground(false);
        mBinding.linechartExample.setPinchZoom(true);
        mBinding.linechartExample.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        mBinding.linechartExample.setData(data);

        Legend legend = mBinding.linechartExample.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);

        XAxis xl = mBinding.linechartExample.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mBinding.linechartExample.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mBinding.linechartExample.getAxisRight();
        rightAxis.setEnabled(false);

        mBinding.linechartExample.getAxisLeft().setDrawGridLines(false);
        mBinding.linechartExample.getXAxis().setDrawGridLines(false);
        mBinding.linechartExample.setDrawBorders(false);

        feedMultiple();

        return mBinding.getRoot();
    }

    private void feedMultiple() {

        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private void addEntry(SensorEvent event) {

        LineData lineData = mBinding.linechartExample.getData();

        if (lineData != null) {
            ILineDataSet set = lineData.getDataSetByIndex(0);
            //set.addEntry(...) can also be called

            if (set == null) {
                set = createSet();
                lineData.addDataSet(set);
            }

            //lineData.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            lineData.addEntry(new Entry(set.getEntryCount(), event.values[0] + 5f), 0);
            //notify data and chart that the data has been changed
            lineData.notifyDataChanged();
            mBinding.linechartExample.notifyDataSetChanged();
            //max number of entries shown on each axis
            mBinding.linechartExample.setVisibleXRangeMaximum(150);
            //mChart.setVisibleYRangeMaximum(30, YAxis.AxisDependency.LEFT);

            //move to the latest entry

            mBinding.linechartExample.moveViewToX(lineData.getEntryCount());

        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.MAGENTA);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {

        mSensorManager.unregisterListener(LineChartFragment.this);
        thread.interrupt();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (plotData) {
            addEntry(event);
            plotData = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do somth if accuracy changes
    }
}
