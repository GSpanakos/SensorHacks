package com.example.vanir.sensorhacks.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.SensorFragmentBinding;
import com.example.vanir.sensorhacks.db.AppDatabase;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.model.Sensor;
import com.example.vanir.sensorhacks.viewmodel.SensorViewModel;



/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorFragment extends Fragment {

    private static final String KEY_SENSOR_ID = "sensor_id";
    private SensorFragmentBinding mBinding;
    private static final String TAG = "Delete_Sensor";
    public static int mSensorId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.sensor_fragment, container, false);
        mBinding.deleteSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + mSensorId);
                SensorViewModel.deleteSensorTask();
            }
        });

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
     * Creates sensor fragment for specific sensor ID
     */
    public static SensorFragment forSensor(int sensorId) {
        mSensorId = sensorId;
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_SENSOR_ID, sensorId);
        fragment.setArguments(args);
        return fragment;
    }


}
