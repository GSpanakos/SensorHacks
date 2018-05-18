package com.example.vanir.sensorhacks.ui;

import android.arch.lifecycle.Lifecycle;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.vanir.sensorhacks.AppExecutors;
import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.AddSensorFragmentBinding;
import com.example.vanir.sensorhacks.db.AppDatabase;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.viewmodel.SensorListViewModel;

import static android.content.ContentValues.TAG;

/**
 * Created by Γιώργος on 8/5/2018.
 */

public class AddSensorFragment extends Fragment {

    private static final String KEY_SENSOR_ID = "sensor_id";
    AddSensorFragmentBinding mBinding;
    public static final String TAG = "add_sensor_to_db";
    public SensorEntity mSensor, sensor;
    Boolean toggleButton = false;
    AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.add_sensor_fragment, container, false);

        mBinding.toggleButtonstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButton = true;
                } else
                    toggleButton = false;
            }
        });

        mBinding.addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Log.d(TAG, "onClick: Id: " + mBinding.uniqueId.getText().toString());
                    Log.d(TAG, "onClick: Name: " + mBinding.editTextname.getText().toString());
                    Log.d(TAG, "onClick: Type: " + mBinding.editTexttype.getText().toString());
                    Log.d(TAG, "onClick: Status: " + toggleButton.toString());
                    Log.d(TAG, "onClick: Value: " + mBinding.editTextvalue.getText().toString());

                    mSensor = new SensorEntity(Integer.parseInt(mBinding.uniqueId.getText().toString()),
                            mBinding.editTextname.getText().toString(),
                            mBinding.editTexttype.getText().toString(),
                            toggleButton,
                            Double.parseDouble(mBinding.editTextvalue.getText().toString()));

                    SensorListViewModel.insertSensorTask(mSensor, v);
                    getFragmentManager().beginTransaction().addToBackStack(TAG).replace(R.id.fragment_container, new SensorListFragment(), TAG).commit();
                } catch (Exception e) {
                    Snackbar.make(v, "Id needs to be integer and Value double", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        return mBinding.getRoot();
    }


}
