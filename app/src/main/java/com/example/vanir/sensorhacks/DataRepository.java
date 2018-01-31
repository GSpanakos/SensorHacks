package com.example.vanir.sensorhacks;

/**
 * Created by Γιώργος on 31/1/2018.
 */

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.vanir.sensorhacks.db.AppDatabase;
import com.example.vanir.sensorhacks.db.SensorEntity;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<SensorEntity>> mObservableSensors;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableSensors = new MediatorLiveData<>();

        mObservableSensors.addSource(mDatabase.sensorDAO().loadAllSensors(),
                sensorEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableSensors.postValue(sensorEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<SensorEntity>> getSensors() {
        return mObservableSensors;
    }

    public LiveData<SensorEntity> loadSensor(final int sensorId) {
        return mDatabase.sensorDAO().loadSensor(sensorId);
    }

}
