package com.example.vanir.sensorhacks.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.SensorItemBinding;
import com.example.vanir.sensorhacks.model.Sensor;

import java.util.List;
import java.util.Objects;

/**
 * Created by Γιώργος on 24/10/2017.
 */

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {

    List<? extends Sensor> mSensorList;

    @Nullable
    private final SensorClickCallback mSensorClickCallback;

    public SensorAdapter(@Nullable SensorClickCallback clickCallback) {
        mSensorClickCallback = clickCallback;
    }

    public void setSensorList(final List<? extends Sensor> sensorList) {
        if (mSensorList == null) {
            mSensorList = sensorList;
            notifyItemRangeInserted(0, sensorList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mSensorList.size();
                }

                @Override
                public int getNewListSize() {
                    return sensorList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mSensorList.get(oldItemPosition).getId() ==
                            sensorList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Sensor newSensor = sensorList.get(newItemPosition);
                    Sensor oldSensor = mSensorList.get(oldItemPosition);
                    return newSensor.getId() == oldSensor.getId()
                            && Objects.equals(newSensor.getName(), oldSensor.getName())
                            && Objects.equals(newSensor.getType(), oldSensor.getType())
                            && newSensor.getValue() == oldSensor.getValue()
                            && newSensor.getStatus() == oldSensor.getStatus();
                }
            });
            mSensorList = sensorList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public SensorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SensorItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sensor_item, parent, false);
        binding.setCallback(mSensorClickCallback);
        return new SensorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SensorViewHolder holder, int position) {
        holder.binding.setSensor(mSensorList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSensorList == null ? 0 : mSensorList.size();
    }

    public class SensorViewHolder extends RecyclerView.ViewHolder {

        final SensorItemBinding binding;

        public SensorViewHolder(SensorItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
