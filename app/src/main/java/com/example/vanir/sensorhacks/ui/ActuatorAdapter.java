package com.example.vanir.sensorhacks.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.databinding.ActuatorItemBinding;
import com.example.vanir.sensorhacks.model.Actuator;

import java.util.List;
import java.util.Objects;

/**
 * Created by Γιώργος on 3/6/2018.
 */

public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ActuatorViewHolder> {

    List<? extends Actuator> mActuatorList;

    @Nullable
    private final ActuatorClickCallBack mActuatorClickCallback;

    public ActuatorAdapter(@Nullable ActuatorClickCallBack clickCallback) {
        mActuatorClickCallback = clickCallback;
    }

    public void setActuatorList(final List<? extends Actuator> actuatorList) {
        if (mActuatorList == null) {
            mActuatorList = actuatorList;
            notifyItemRangeInserted(0, actuatorList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mActuatorList.size();
                }

                @Override
                public int getNewListSize() {
                    return actuatorList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mActuatorList.get(oldItemPosition).getId() ==
                            actuatorList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Actuator newActuator = actuatorList.get(newItemPosition);
                    Actuator oldActuator = mActuatorList.get(oldItemPosition);
                    return newActuator.getId() == oldActuator.getId()
                            && Objects.equals(newActuator.getName(), oldActuator.getName())
                            && Objects.equals(newActuator.getType(), oldActuator.getType())
                            && newActuator.getStatus() == oldActuator.getStatus();
                }
            });
            mActuatorList = actuatorList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public ActuatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActuatorItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.actuator_item, parent, false);
        binding.setCallback(mActuatorClickCallback);
        return new ActuatorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ActuatorViewHolder holder, int position) {
        holder.binding.setActuator(mActuatorList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mActuatorList == null ? 0 : mActuatorList.size();
    }

    public class ActuatorViewHolder extends RecyclerView.ViewHolder {

        final ActuatorItemBinding binding;

        public ActuatorViewHolder(ActuatorItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
