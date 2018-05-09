package com.example.vanir.sensorhacks.ui;

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
import android.widget.Button;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.ListFragmentBinding;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.model.Sensor;
import com.example.vanir.sensorhacks.viewmodel.SensorListViewModel;

import java.util.List;

/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorListFragment extends Fragment {

    public static final String TAG = "SensorListViewModel";
    private SensorAdapter mSensorAdapter;
    private ListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);
        mSensorAdapter = new SensorAdapter(mSensorClickCallback);
        mBinding.sensorsList.setAdapter(mSensorAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SensorListViewModel viewModel =
                ViewModelProviders.of(this).get(SensorListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(SensorListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getSensors().observe(this, new Observer<List<SensorEntity>>() {
            @Override
            public void onChanged(@Nullable List<SensorEntity> mySensors) {
                if (mySensors != null) {
                    mBinding.setIsLoading(false);
                    mSensorAdapter.setSensorList(mySensors);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }

    private final SensorClickCallback mSensorClickCallback = new SensorClickCallback() {
        @Override
        public void onClick(Sensor sensor) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((Sensors) getActivity()).show(sensor);
            }
        }
    };


}
