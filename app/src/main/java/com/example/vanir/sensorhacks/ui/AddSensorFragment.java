package com.example.vanir.sensorhacks.ui;

import android.support.v4.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.AddSensorFragmentBinding;

/**
 * Created by Γιώργος on 8/5/2018.
 */

public class AddSensorFragment extends Fragment {

    private static final String KEY_SENSOR_ID = "sensor_id";
    AddSensorFragmentBinding mBinding;

    public AddSensorFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.add_sensor_fragment, container, false);
        return mBinding.getRoot();
    }

}
