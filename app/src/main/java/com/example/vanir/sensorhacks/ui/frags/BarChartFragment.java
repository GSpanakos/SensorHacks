package com.example.vanir.sensorhacks.ui.frags;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.db.SensorValueEntity;
import com.example.vanir.sensorhacks.viewmodel.BarChartViewModel;
import com.example.vanir.sensorhacks.viewmodel.LineChartViewModel;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.example.vanir.sensorhacks.databinding.BarchartFragmentBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Γιώργος on 21/5/2018.
 */

public class BarChartFragment extends Fragment {

    //public View view;
    //public BarChart barChart;
    public BarchartFragmentBinding mBinding;
    private final long refe_timestamp = 1532649714;
    private Thread thread;
    private boolean plotData = true;
    private BarData barData;
    private static final String TAG = "BarChartFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        //view = inflater.inflate(R.layout.barchart_fragment, container, false);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.barchart_fragment, container, false);
        mBinding.clearButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barData.clearValues();
                mBinding.barchartExample.invalidate();
                //mBinding.barchartExample.clear();
            }
        });

        mBinding.barchartExample.getDescription().setEnabled(true);
        mBinding.barchartExample.setTouchEnabled(true);
        mBinding.barchartExample.setDragEnabled(true);
        mBinding.barchartExample.setScaleEnabled(true);
        mBinding.barchartExample.setDrawBarShadow(false);
        mBinding.barchartExample.setDrawValueAboveBar(true);
        mBinding.barchartExample.setMaxVisibleValueCount(50);
        mBinding.barchartExample.setPinchZoom(true);
        mBinding.barchartExample.setDrawGridBackground(true);
        mBinding.barchartExample.setBackgroundColor(Color.WHITE);

//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//
//        for (int j = 0; j < 25; j++) {
//            for (int i = 0; i < 50; i++) {
//                barEntries.add(new BarEntry(i * j, 2 * i, "papakia"));
//            }
//        }

        BarData data = new BarData();
        data.setValueTextColor(Color.BLACK);

        mBinding.barchartExample.setData(data);

        Legend legend = mBinding.barchartExample.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setTextColor(Color.BLACK);

        XAxis xl = mBinding.barchartExample.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        IAxisValueFormatter iAxisValueFormatter = new HourAxisValueFormatter(refe_timestamp);
        xl.setValueFormatter(iAxisValueFormatter);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mBinding.barchartExample.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mBinding.barchartExample.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setTextColor(Color.BLACK);
        rightAxis.setAxisMaximum(100f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawGridLines(true);

        mBinding.barchartExample.getAxisLeft().setDrawGridLines(true);
        mBinding.barchartExample.getXAxis().setDrawGridLines(true);
        mBinding.barchartExample.setDrawBorders(true);

        mBinding.barchartExample.clearValues();
        feedMultiple();


//        BarDataSet bardataSet = new BarDataSet(barEntries, "PAPAKIA");
//        bardataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        BarData barData = new BarData(bardataSet);
//        barData.setBarWidth(0.9f);



        return mBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BarChartViewModel.Factory factory = new BarChartViewModel.Factory(
                getActivity().getApplication(), 1, null);

        final BarChartViewModel viewModel =
                ViewModelProviders.of(this, factory).get(BarChartViewModel.class);

        subscribeUi(viewModel);

    }

    private void subscribeUi(BarChartViewModel viewModel) {
        viewModel.loadAllSensorValues().observe(this, new Observer<List<SensorValueEntity>>() {
            @Override
            public void onChanged(@Nullable List<SensorValueEntity> sensorValueEntities) {
                if (sensorValueEntities != null) {
                    //reference_timestamp = Float.parseFloat(Converters.dateToTimeStamp(sensorValueEntities.get(0).getDate()));
                    mBinding.setIsLoading(false);
                    if (plotData) {
                        Log.i(TAG, "onChanged: SUBSCRIBER CALLED");

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

        barData = mBinding.barchartExample.getData();

        if (barData != null) {
            IBarDataSet set = barData.getDataSetByIndex(0);
            //set.addEntry(...) can also be called

            if (set == null) {
                set = createSet(valueEntity);
                barData.addDataSet(set);
            }

            long xOld = valueEntity.getDate().getTime() / 1000;
            float xNew = xOld - refe_timestamp;
            float y = (float) valueEntity.getValue();
            long z = (Calendar.getInstance().getTime().getTime() / 1000) - xOld;
            Log.i(TAG, "addEntry: 4 : Value: " + y + " Time Old: " + xOld);
            Log.i(TAG, "addEntry: 5 : Value:" + y + " Time New: " + xNew);


            //barData.addEntry(new Entry(xNew, y), 0);
            //notify data and chart that the data has been changed

            //barData.notifyDataChanged();
            //mBinding.barchartExample.notifyDataSetChanged();

            set.addEntry(new BarEntry(xNew, y));
            mBinding.barchartExample.getData().notifyDataChanged();
            mBinding.barchartExample.notifyDataSetChanged();


            //max number of entries shown on each axis


            //mBinding.barchartExample.setVisibleXRangeMaximum(100);


            //mBinding.barchartExample.setVisibleYRangeMaximum(30, YAxis.AxisDependency.LEFT);

            //move to the latest entry
            mBinding.barchartExample.moveViewToX(barData.getEntryCount());

            Log.i(TAG, "addEntry: 6: Number: " + barData.getEntryCount());


        }
    }

    private BarDataSet createSet(SensorValueEntity valueEntity) {

        long xOld = valueEntity.getDate().getTime() / 1000;
        float xNew = xOld - refe_timestamp;
        float y = (float) valueEntity.getValue();
        long z = (Calendar.getInstance().getTime().getTime() / 1000) - xOld;
        Log.i(TAG, "addEntry: 4 : Value: " + y + " Time Old: " + xOld);
        Log.i(TAG, "addEntry: 5 : Value:" + y + " Time New: " + xNew);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(0, new BarEntry(xNew, (float) valueEntity.getValue()));

        BarDataSet set = new BarDataSet(entries, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.BLUE);
        set.setHighlightEnabled(true);
        set.setDrawValues(true);
        set.setBarBorderWidth(2f);
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
