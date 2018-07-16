package com.example.vanir.sensorhacks.ui.frags;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.LinechartFragmentBinding;
import com.example.vanir.sensorhacks.db.SensorValueEntity;
import com.example.vanir.sensorhacks.viewmodel.LineChartViewModel;
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

public class LineChartFragment extends Fragment {

    //implements SensorEventListener (for in device sensors)

    private static final String TAG = "LineChartFragment";
    public LinechartFragmentBinding mBinding;
    private boolean plotData = true;
    private Thread thread;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    //private LineChart mChart;
    //public View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        //view = inflater.inflate(R.layout.linechart_fragment, container, false);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.linechart_fragment, container, false);

//        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
//        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//        List<Sensor> hardwareSensors = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
//
//        for (int i = 0; i < hardwareSensors.size(); i++) {
//            Log.d(TAG, "onCreateView: HardwareSesnor " + i + ": " + hardwareSensors.get(i).toString());
//
//        }
//
//        if (mAccelerometer != null) {
//            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
//        }

        //mChart = (LineChart) getActivity().findViewById(R.id.linechart_example);
        mBinding.linechartExample.getDescription().setEnabled(true);
        mBinding.linechartExample.setTouchEnabled(true);
        mBinding.linechartExample.setDragEnabled(true);
        mBinding.linechartExample.setScaleEnabled(true);
        mBinding.linechartExample.setDrawGridBackground(true);
        mBinding.linechartExample.setPinchZoom(true);
        mBinding.linechartExample.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        mBinding.linechartExample.setData(data);

        Legend legend = mBinding.linechartExample.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.BLACK);

        XAxis xl = mBinding.linechartExample.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mBinding.linechartExample.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(12f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mBinding.linechartExample.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setAxisMaximum(12f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawGridLines(true);

        mBinding.linechartExample.getAxisLeft().setDrawGridLines(true);
        mBinding.linechartExample.getXAxis().setDrawGridLines(true);
        mBinding.linechartExample.setDrawBorders(true);

        feedMultiple();

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LineChartViewModel.Factory factory = new LineChartViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt("id"), getArguments().getString("name")); //need to pass name and id here);

        final LineChartViewModel viewModel =
                ViewModelProviders.of(this, factory).get(LineChartViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(LineChartViewModel viewModel) {
        viewModel.getValueOnIdandName().observe(this, new Observer<List<SensorValueEntity>>() {
            @Override
            public void onChanged(@Nullable List<SensorValueEntity> sensorValueEntities) {
                if (sensorValueEntities != null) {
                    mBinding.setIsLoading(false);
                    if (plotData) {
                        addEntry(event);
                        plotData = false;
                    }
                    // do stuff with graphs
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
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
                        Thread.sleep(100);
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

            //lineData.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 2) + 5f), 0);
            lineData.addEntry(new Entry(set.getEntryCount(), event.values[0] + 6f), 0);
            //notify data and chart that the data has been changed
            lineData.notifyDataChanged();
            mBinding.linechartExample.notifyDataSetChanged();
            //max number of entries shown on each axis
            mBinding.linechartExample.setVisibleXRangeMaximum(100);
            //mBinding.linechartExample.setVisibleYRangeMaximum(30, YAxis.AxisDependency.LEFT);

            //move to the latest entry

            mBinding.linechartExample.moveViewToX(lineData.getEntryCount());

        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(4f);
        set.setColor(Color.BLUE);
        set.setHighlightEnabled(true);
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

//        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {

//        mSensorManager.unregisterListener(LineChartFragment.this);
        thread.interrupt();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        if (plotData) {
//            addEntry(event);
//            plotData = false;
//        }
//    }

//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        //do somth if accuracy changes
//    }
}
