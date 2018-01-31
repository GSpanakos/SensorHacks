package com.example.vanir.sensorhacks.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.vanir.sensorhacks.db.SensorEntity;

import java.util.List;

/**
 * Created by Γιώργος on 31/1/2018.
 */

public class SensorListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<SensorEntity>> mObservableSensors;

    public SensorListViewModel(Application application) {
        super(application);

        mObservableSensors = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableSensors.setValue(null);

        LiveData<List<SensorEntity>> sensors = ((BasicApp) application).getRepository()
                .getSensors();

        // observe the changes of the products from the database and forward them
        mObservableSensors.addSource(sensors, mObservableSensors::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<SensorEntity>> getSensors() {
        return mObservableSensors;
    }
}
