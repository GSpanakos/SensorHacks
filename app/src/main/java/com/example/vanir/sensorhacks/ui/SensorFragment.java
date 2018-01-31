package com.example.vanir.sensorhacks.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.SensorFragmentBinding;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.model.Sensor;
import com.example.vanir.sensorhacks.viewmodel.SensorViewModel;


/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorFragment extends Fragment {

    private static final String KEY_SENSOR_ID = "sensor_id";

    private SensorFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.sensor_fragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SensorViewModel.Factory factory = new SensorViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_SENSOR_ID));

        final SensorViewModel model = ViewModelProviders.of(this, factory)
                .get(SensorViewModel.class);

        mBinding.setSensorViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final SensorViewModel model) {

        // Observe sensor data
        model.getObservableSensor().observe(this, new Observer<SensorEntity>() {
            @Override
            public void onChanged(@Nullable SensorEntity sensorEntity) {
                model.setSensor(sensorEntity);
            }
        });
    }

    /**
     * Creates product fragment for specific product ID
     */
    public static SensorFragment forSensor(int sensorId) {
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_SENSOR_ID, sensorId);
        fragment.setArguments(args);
        return fragment;
    }

}
