package com.example.vanir.sensorhacks.ui.frags;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.ActuatorListFragmentBinding;
import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.model.Actuator;
import com.example.vanir.sensorhacks.ui.ActuatorAdapter;
import com.example.vanir.sensorhacks.ui.ActuatorClickCallBack;
import com.example.vanir.sensorhacks.ui.Actuators;
import com.example.vanir.sensorhacks.viewmodel.ActuatorListViewModel;

import java.util.List;

/**
 * Created by Γιώργος on 3/6/2018.
 */

public class ActuatorListFragment extends Fragment {

    public static final String TAG = "ActuatorListViewModel";
    private ActuatorAdapter mActuatorAdapter;
    private ActuatorListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.actuator_list_fragment, container, false);
        mActuatorAdapter = new ActuatorAdapter(mActuatorClickCallback);
        mBinding.actuatorsList.setAdapter(mActuatorAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActuatorListViewModel.Factory factory = new ActuatorListViewModel.Factory(
                getActivity().getApplication());

        final ActuatorListViewModel viewModel =
                ViewModelProviders.of(this, factory).get(ActuatorListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(ActuatorListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getActuators().observe(this, new Observer<List<ActuatorEntity>>() {
            @Override
            public void onChanged(@Nullable List<ActuatorEntity> myActuators) {
                if (myActuators != null) {
                    mBinding.setIsLoading(false);
                    mActuatorAdapter.setActuatorList(myActuators);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }

    private final ActuatorClickCallBack mActuatorClickCallback = new ActuatorClickCallBack() {
        @Override
        public void onClick(Actuator actuator) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((Actuators) getActivity()).show(actuator);
            }
        }
    };
}
