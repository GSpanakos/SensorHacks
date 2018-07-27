package com.example.vanir.sensorhacks.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.vanir.sensorhacks.BasicApp;
import com.example.vanir.sensorhacks.DataRepository;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.db.SensorValueEntity;

import java.util.List;

/**
 * Created by Γιώργος on 13/7/2018.
 */

public class LineChartViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<SensorValueEntity>> mObservableSensorValues;
    private static final String TAG = "fetch_sensor_values_from_db";
    private static DataRepository nRepository;
    private static int mId;
    private static String mName;


    public LineChartViewModel(@NonNull Application application, DataRepository repository, final int sensorId, final String sensorName) {
        super(application);

        nRepository = repository;

        mObservableSensorValues = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableSensorValues.setValue(null);

        LiveData<List<SensorValueEntity>> sensorValues = repository.loadAllSensorValues();

        // observe the changes of the sensors from the database and forward them
        mObservableSensorValues.addSource(sensorValues, mObservableSensorValues::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application mApplication;
        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int id, @Nullable String name) {
            mApplication = application;
            mId = id;
            mName = name;
            mRepository = ((BasicApp) application).getRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new LineChartViewModel(mApplication, mRepository, mId, mName);
        }

    }


    public LiveData<List<SensorValueEntity>> getValueOnIdandName() {
        return mObservableSensorValues;
    }

    public LiveData<List<SensorValueEntity>> loadAllSensorValues() {
        return mObservableSensorValues;
    }
}
