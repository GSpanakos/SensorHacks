package com.example.vanir.sensorhacks.ui.frags;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.adapters.AdapterViewBindingAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.EditSensorFragmentBinding;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.viewmodel.EditSensorViewModel;

/**
 * Created by Γιώργος on 5/6/2018.
 */

public class EditSensorFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String KEY_SENSOR_ID = "sensor_id";
    private static int mSensorId;
    public EditSensorFragmentBinding mBinding;
    public static final String TAG = "update_sensor_to_db";
    public static SensorEntity mSensor;
    public Boolean toggleButton = false;
    private String sensor_Type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout

        mBinding = DataBindingUtil.inflate(inflater, R.layout.edit_sensor_fragment, container, false);
        mBinding.editToggleSensorButtonStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleButton = isChecked;
            }
        });

        mBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    mSensor = new SensorEntity(mSensor.getId(),
                            mSensor.getName(),
                            sensor_Type,
                            toggleButton,
                            mSensor.getValue());


                    Log.d(TAG, "onClick: HALO " + mSensorId);

                    EditSensorViewModel.editSensorTask(mSensor, v);

                } catch (Exception e) {
                    Snackbar.make(v, "Value must be double or int", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        mBinding.sensorNameInEditScreen.setSelected(true);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sensor_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.editSensorTypeSpinner.setAdapter(spinnerAdapter);
        mBinding.editSensorTypeSpinner.setOnItemSelectedListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditSensorViewModel.Factory factory = new EditSensorViewModel.Factory(getActivity().getApplication(), mSensorId);

        final EditSensorViewModel model = ViewModelProviders.of(this, factory)
                .get(EditSensorViewModel.class);

        mBinding.setEditSensorViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final EditSensorViewModel model) {

        //Observe sensor in db
        model.getObservableSensor().observe(this, new Observer<SensorEntity>() {
            @Override
            public void onChanged(@Nullable SensorEntity sensorEntity) {
                if ((sensorEntity != null) && (mSensor == null)) {

                    mSensor = sensorEntity;
                    mBinding.setSensor(mSensor);
                    model.setSensor(mSensor);

                } else {
                    mBinding.setSensor(sensorEntity);
                    model.setSensor(sensorEntity);
                }


            }
        });
    }

    /**
     * Creates frag for editing sensor, neat trick to pass the id of the sensor from
     * click listener on SensorFragment
     */

    public static EditSensorFragment forEditSensor(SensorEntity sensor) {
        mSensorId = sensor.getId();
        mSensor = new SensorEntity(sensor);
        return new EditSensorFragment();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sensor_Type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
