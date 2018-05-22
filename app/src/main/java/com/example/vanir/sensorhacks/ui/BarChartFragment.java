package com.example.vanir.sensorhacks.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.BarchartFragmentBinding;

/**
 * Created by Γιώργος on 21/5/2018.
 */

public class BarChartFragment extends Fragment {

    public BarchartFragmentBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.barchart_fragment, container, false);

        return mBinding.getRoot();
    }
}
