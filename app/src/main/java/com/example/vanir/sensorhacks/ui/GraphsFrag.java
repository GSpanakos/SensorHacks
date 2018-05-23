package com.example.vanir.sensorhacks.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.ContentGrpFragmentBinding;

/**
 * Created by Γιώργος on 23/5/2018.
 */

public class GraphsFrag extends Fragment {

    public static final String TAGB = "Start Bar Chart";
    public static final String TAGL = "Start Line Chart";

    public ContentGrpFragmentBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.content_grp_fragment, container, false);

        mBinding.barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(TAGB).replace(R.id.chart_container, new BarChartFragment(), TAGB).commit();
                Log.d(null, "onClick: gamw to spiti sou");
            }
        });

        mBinding.lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(TAGL).replace(R.id.chart_container, new LineChartFragment(), TAGL).commit();
            }
        });

        return mBinding.getRoot();
    }
}
