package com.example.vanir.sensorhacks;

/**
 * Created by Γιώργος on 31/1/2018.
 */

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.db.AppDatabase;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.model.Actuator;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository handling the work with sensors.
 */
public class DataRepository {

    private static DataRepository sInstance;
    private final AppDatabase mDatabase;
    private MediatorLiveData<List<SensorEntity>> mObservableSensors;
    private MediatorLiveData<List<ActuatorEntity>> mObservableActuators;

    private DataRepository(final AppDatabase database) {

        mDatabase = database;
        mObservableSensors = new MediatorLiveData<>();
        mObservableActuators = new MediatorLiveData<>();

        mObservableSensors.addSource(mDatabase.sensorDAO().loadAllSensors(),
                sensorEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableSensors.postValue(sensorEntities);
                    }
                });

        mObservableActuators.addSource(mDatabase.actuatorDAO().loadAllActuators(),
                actuatorEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableActuators.postValue(actuatorEntities);
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
     * Get the list of sensors from the database and get notified when the data changes.
     */
    public LiveData<List<SensorEntity>> getSensors() {
        return mObservableSensors;
    }

    public List<SensorEntity> loadAllSensorsSync() {
        return mDatabase.sensorDAO().loadAllSensorsSync();
    }

    public List<Integer> loadSensorIds() {
        return mDatabase.sensorDAO().loadIds();
    }

    public LiveData<SensorEntity> loadSensor(final int sensorId) {
        return mDatabase.sensorDAO().loadSensor(sensorId);
    }

    public SensorEntity loadSensorSync(final int sensorId) {
        return mDatabase.sensorDAO().loadSensorSync(sensorId);
    }

    public void insert(SensorEntity sensorEntity) {
        mDatabase.sensorDAO().insert(sensorEntity);
    }

    public void delete(SensorEntity sensorEntity) {
        mDatabase.sensorDAO().deleteSensor(sensorEntity);
    }

    /**
     * Get the list of actuators from the database and get notified when the data changes.
     */

    public LiveData<List<ActuatorEntity>> getActuators() {
        return mObservableActuators;
    }

    public List<ActuatorEntity> loadAllActuatorsSync() {
        return mDatabase.actuatorDAO().loadAllActuatorsSync();
    }

    public List<Integer> loadActuatorIds() {
        return mDatabase.actuatorDAO().loadIds();
    }

    public LiveData<ActuatorEntity> loadActuator(final int actuatorId) {
        return mDatabase.actuatorDAO().loadActuator(actuatorId);
    }

    public ActuatorEntity loadActuatorSync(final int actuatorId) {
        return mDatabase.actuatorDAO().loadActuatorSync(actuatorId);
    }

    public void insert(ActuatorEntity actuatorEntity) {
        mDatabase.actuatorDAO().insert(actuatorEntity);
    }

    public void delete(ActuatorEntity actuatorEntity) {
        mDatabase.actuatorDAO().deleteActuator(actuatorEntity);
    }

}
