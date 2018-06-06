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
import com.example.vanir.sensorhacks.databinding.ActuatorFragmentBinding;
import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.viewmodel.ActuatorViewModel;

/**
 * Created by Γιώργος on 3/6/2018.
 */

public class ActuatorFragment extends Fragment {

    private static final String KEY_ACTUATOR_ID = "actuator_id";
    private ActuatorFragmentBinding mBinding;
    private static final String TAG = "Delete_Actuator", TAG2 = "After_Delete_Frag";
    public static int mActuatorId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.actuator_fragment, container, false);
        mBinding.deleteActuator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick :" + mActuatorId);
                ActuatorViewModel.deleteActuatorTask(mActuatorId, v);
                getFragmentManager().beginTransaction().replace(R.id.fragment_actuator_container, new ActuatorListFragment(), null).commit();
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActuatorViewModel.Factory factory = new ActuatorViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_ACTUATOR_ID));

        final ActuatorViewModel model = ViewModelProviders.of(this, factory)
                .get(ActuatorViewModel.class);

        mBinding.setActuatorViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final ActuatorViewModel model) {

        // Observe actuator data
        model.getObservableActuator().observe(this, new Observer<ActuatorEntity>() {
            @Override
            public void onChanged(@Nullable ActuatorEntity actuatorEntity) {
                model.setActuator(actuatorEntity);
            }
        });
    }

    /**
     * Creates actuator fragment for specific actuator ID
     */
    public static ActuatorFragment forActuator(int actuatorId) {
        mActuatorId = actuatorId;
        ActuatorFragment fragment = new ActuatorFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_ACTUATOR_ID, actuatorId);
        fragment.setArguments(args);
        return fragment;
    }
}
