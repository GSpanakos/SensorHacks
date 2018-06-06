package com.example.vanir.sensorhacks.ui.frags;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.SensorFragmentBinding;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.viewmodel.SensorViewModel;



/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorFragment extends Fragment {

    private static final String KEY_SENSOR_ID = "sensor_id";
    private SensorFragmentBinding mBinding;
    private static final String TAG = "Delete_Sensor", TAG2 = "After_Delete_Frag", TAG3 = "Edit_Sensor";
    public static int mSensorId;
    public static SensorEntity sensorForLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.sensor_fragment, container, false);
        //needed for textview horizontal scrolling/marqueeing
        mBinding.sensorFragForMarqueeId.setSelected(true);

        mBinding.deleteSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick delete: " + mSensorId);
                SensorViewModel.deleteSensorTask(mSensorId, v);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SensorListFragment(), null).commit();
            }
        });

        mBinding.editSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG3, "onClick: " + mSensorId);
                EditSensorFragment editSensorFragment = EditSensorFragment.forEditSensor(sensorForLayout);
                getFragmentManager().beginTransaction().addToBackStack(TAG3).replace(R.id.fragment_container, new EditSensorFragment(), null).commit();
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SensorViewModel.Factory factory = new SensorViewModel.Factory(getActivity().getApplication(), getArguments().getInt(KEY_SENSOR_ID));

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
    public static SensorFragment forSensor(SensorEntity sensor) {
        mSensorId = sensor.getId();
        sensorForLayout = sensor;
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_SENSOR_ID, sensor.getId());
        fragment.setArguments(args);
        return fragment;
    }


}
