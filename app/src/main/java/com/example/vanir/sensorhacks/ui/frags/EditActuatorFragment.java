package com.example.vanir.sensorhacks.ui.frags;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.databinding.EditActuatorFragmentBinding;
import com.example.vanir.sensorhacks.viewmodel.EditActuatorViewModel;

/**
 * Created by Γιώργος on 7/6/2018.
 */

public class EditActuatorFragment extends Fragment {

    private static final String KEY_ACTUATOR_ID = "actuator_id";
    public static int mActuatorId;
    public static ActuatorEntity mActuator;
    private EditActuatorFragmentBinding mBinding;
    private boolean toggleButton = false;
    public static final String TAG = "update_actuator_to_db";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate with data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.edit_actuator_fragment, container, false);
        mBinding.editToggleActuatorButtonStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleButton = isChecked;
            }
        });

        mBinding.editActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    mActuator = new ActuatorEntity(mActuatorId,
                            mBinding.editActuatorTextName.getText().toString(),
                            mBinding.editActuatorTextType.getText().toString(),
                            toggleButton);

                    Log.d(TAG, "onClick: HALO " + mActuatorId);

                    EditActuatorViewModel.editActuatorTask(mActuator, v);

                } catch (Exception e) {
                    Snackbar.make(v, "Something went wrong", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditActuatorViewModel.Factory factory = new EditActuatorViewModel.Factory(getActivity().getApplication(), mActuatorId);

        final EditActuatorViewModel model = ViewModelProviders.of(this, factory)
                .get(EditActuatorViewModel.class);

        mBinding.setEditActuatorViewModel(model);

        subscribeToModel(model);

    }

    private void subscribeToModel(EditActuatorViewModel model) {

        //Observe Actuator in db
        model.getObservableActuator().observe(this, new Observer<ActuatorEntity>() {

            @Override
            public void onChanged(@Nullable ActuatorEntity actuatorEntity) {
                if ((actuatorEntity != null) && (mActuator == null)) {

                    mActuator = actuatorEntity;
                    mBinding.setActuator(mActuator);
                    model.setActuator(mActuator);

                } else {
                    mBinding.setActuator(actuatorEntity);
                    model.setActuator(actuatorEntity);
                }
            }
        });
    }

    public static EditActuatorFragment forEditActuator(ActuatorEntity actuator) {
        mActuatorId = actuator.getId();
        mActuator = actuator;
        return new EditActuatorFragment();
    }
}
