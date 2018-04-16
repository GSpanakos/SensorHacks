package com.example.vanir.sensorhacks.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.vanir.sensorhacks.BasicApp;
import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.db.SensorEntity;

import java.util.List;

/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorViewModel extends AndroidViewModel {

    private final LiveData<SensorEntity> mObservableSensor;

    public ObservableField<SensorEntity> sensor = new ObservableField<>();

    private final int mSensorId;

    public SensorViewModel(@NonNull Application application, DataRepository repository,
                           final int sensorId) {
        super(application);
        mSensorId = sensorId;

        mObservableSensor = repository.loadSensor(mSensorId);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<SensorEntity> getObservableSensor() {
        return mObservableSensor;
    }

    public void setSensor(SensorEntity sensor) {
        this.sensor.set(sensor);
    }

    /**
     * A creator is used to inject the sensor ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the sensor ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mSensorId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int sensorId) {
            mApplication = application;
            mSensorId = sensorId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new SensorViewModel(mApplication, mRepository, mSensorId);
        }
    }

}
