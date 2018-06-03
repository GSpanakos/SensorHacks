package com.example.vanir.sensorhacks.ui.frags;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.AddActuatorFragmentBinding;
import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.model.Actuator;
import com.example.vanir.sensorhacks.viewmodel.ActuatorListViewModel;

/**
 * Created by Γιώργος on 3/6/2018.
 */

public class AddActuatorFragment extends Fragment {

    private static final String KEY_ACTUATOR_ID = "actuator_id";
    public AddActuatorFragmentBinding mBinding;
    public static final String TAG = "add_actuator_to_db";
    public ActuatorEntity mActuator, actuator;
    public Boolean toggleButton = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.add_actuator_fragment, container, false);

        mBinding.toggleActButtonstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButton = true;
                } else
                    toggleButton = false;
            }
        });

        mBinding.addActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    mActuator = new ActuatorEntity(Integer.parseInt(mBinding.uniqueActId.getText().toString()),
                            mBinding.editActTextName.getText().toString(),
                            mBinding.editActTextType.getText().toString(),
                            toggleButton);

                    ActuatorListViewModel.insertActuatorTask(mActuator, v);

                } catch (Exception e) {
                    Snackbar.make(v, "Id needs to be integer", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        return mBinding.getRoot();
    }
}
