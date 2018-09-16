package com.example.vanir.sensorhacks.ui.frags;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.Bluetooth;
import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.LinechartFragmentBinding;
import com.example.vanir.sensorhacks.db.SensorValueEntity;
import com.example.vanir.sensorhacks.viewmodel.LineChartViewModel;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private Bundle arguments = getArguments();
    private final long refe_timestamp = 1532649714;
    private LineData lineData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.linechart_fragment, container, false);
        mBinding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineData.clearValues();
                mBinding.linechartExample.invalidate();
                //mBinding.linechartExample.clear();
            }
        });


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
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        IAxisValueFormatter iAxisValueFormatter = new HourAxisValueFormatter(refe_timestamp);
        xl.setValueFormatter(iAxisValueFormatter);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mBinding.linechartExample.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mBinding.linechartExample.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setAxisMaximum(100f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawGridLines(true);

        mBinding.linechartExample.getAxisLeft().setDrawGridLines(true);
        mBinding.linechartExample.getXAxis().setDrawGridLines(true);
        mBinding.linechartExample.setDrawBorders(true);
        //mBinding.linechartExample.moveViewToX(data.getEntryCount());

        mBinding.linechartExample.clearValues();
        feedMultiple();

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (arguments != null) {

            LineChartViewModel.Factory factory = new LineChartViewModel.Factory(
                    getActivity().getApplication(), arguments.getInt("id"), arguments.getString("name")); //need to pass name and id here);

            final LineChartViewModel viewModel =
                    ViewModelProviders.of(this, factory).get(LineChartViewModel.class);

            subscribeUi(viewModel);
        } else {
            LineChartViewModel.Factory factory = new LineChartViewModel.Factory(
                    getActivity().getApplication(), -1, null); //need to pass name and id here);

            final LineChartViewModel viewModel =
                    ViewModelProviders.of(this, factory).get(LineChartViewModel.class);
            subscribeUi(viewModel);
        }


    }

    private void subscribeUi(LineChartViewModel viewModel) {
        viewModel.loadAllSensorValues().observe(this, new Observer<List<SensorValueEntity>>() {
            @Override
            public void onChanged(@Nullable List<SensorValueEntity> sensorValueEntities) {
                if (sensorValueEntities != null) {
                    //reference_timestamp = Float.parseFloat(Converters.dateToTimeStamp(sensorValueEntities.get(0).getDate()));
                    mBinding.setIsLoading(false);
                    if (plotData) {
                        Log.i(TAG, "onChanged: SUBSCRIBER CALLED:");
                        addEntry(sensorValueEntities.get(sensorValueEntities.size() - 1));
                        plotData = false;

                    }
                    // do stuff with graphs

                } else {
                    mBinding.setIsLoading(true);
                }
                mBinding.executePendingBindings();
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
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private void addEntry(SensorValueEntity valueEntity) {

        lineData = mBinding.linechartExample.getData();

        if (lineData != null) {
            ILineDataSet set = lineData.getDataSetByIndex(0);
            //set.addEntry(...) can also be called

            if (set == null) {
                set = createSet();
                lineData.addDataSet(set);
            }

            long xOld = valueEntity.getDate().getTime() / 1000;
            float xNew = xOld - refe_timestamp;
            float y = (float) valueEntity.getValue();
            long z = (Calendar.getInstance().getTime().getTime() / 1000) - xOld;
            Log.i(TAG, "addEntry: 4 : Value: " + y + " Time Old: " + xOld);
            Log.i(TAG, "addEntry: 5 : Value:" + y + " Time New: " + xNew);


            lineData.addEntry(new Entry(xNew, y), 0);
            //notify data and chart that the data has been changed

            lineData.notifyDataChanged();
            mBinding.linechartExample.notifyDataSetChanged();


            //max number of entries shown on each axis


            mBinding.linechartExample.setVisibleXRangeMaximum(100);


            //mBinding.linechartExample.setVisibleYRangeMaximum(30, YAxis.AxisDependency.LEFT);

            //move to the latest entry
            mBinding.linechartExample.moveViewToX(lineData.getEntryCount());

            Log.i(TAG, "addEntry: 6: Number: " + lineData.getEntryCount());


        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.BLUE);
        set.setHighlightEnabled(true);
        set.setDrawValues(true);
        set.setDrawCircles(true);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setLineWidth(2f);
        return set;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }

    }

    @Override
    public void onDestroy() {

        thread.interrupt();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class HourAxisValueFormatter implements IAxisValueFormatter {

        private long referenceTimestamp; // minimum timestamp in your data set
        private DateFormat mDataFormat;
        private Date mDate;

        public HourAxisValueFormatter(long referenceTimestamp) {
            this.referenceTimestamp = referenceTimestamp;
            this.mDataFormat = new SimpleDateFormat("HH:mm:ss");
            this.mDate = new Date();
        }


        /**
         * Called when a value from an axis is to be formatted
         * before being drawn. For performance reasons, avoid excessive calculations
         * and memory allocations inside this method.
         *
         * @param value the value to be formatted
         * @param axis  the axis the value belongs to
         * @return
         */
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // convertedTimestamp = originalTimestamp - referenceTimestamp
            long convertedTimestamp = (long) value;

            // Retrieve original timestamp
            long originalTimestamp = referenceTimestamp + convertedTimestamp;

            // Convert timestamp to hour:minute
            return getHour(originalTimestamp);
        }

        private String getHour(long timestamp) {
            try {
                mDate.setTime(timestamp * 1000);
                return mDataFormat.format(mDate);
            } catch (Exception ex) {
                return "xx";
            }
        }
    }

}
