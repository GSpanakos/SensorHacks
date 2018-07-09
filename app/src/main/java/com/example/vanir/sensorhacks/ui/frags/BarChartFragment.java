package com.example.vanir.sensorhacks.ui.frags;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.example.vanir.sensorhacks.databinding.BarchartFragmentBinding;

import java.util.ArrayList;

/**
 * Created by Γιώργος on 21/5/2018.
 */

public class BarChartFragment extends Fragment {

    //public View view;
    //public BarChart barChart;
    public BarchartFragmentBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        //view = inflater.inflate(R.layout.barchart_fragment, container, false);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.barchart_fragment, container, false);


        mBinding.barchartExample.setDrawBarShadow(false);
        mBinding.barchartExample.setDrawValueAboveBar(true);
        mBinding.barchartExample.setMaxVisibleValueCount(50);
        mBinding.barchartExample.setPinchZoom(false);
        mBinding.barchartExample.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for (int j = 0; j < 25; j++) {
            for (int i = 0; i < 50; i++) {
                barEntries.add(new BarEntry(i * j, 2 * i, "papakia"));
            }
        }

        BarDataSet bardataSet = new BarDataSet(barEntries, "PAPAKIA");
        bardataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(bardataSet);
        barData.setBarWidth(0.9f);

        mBinding.barchartExample.setData(barData);

        return mBinding.getRoot();

    }
}