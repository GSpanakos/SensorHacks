package com.example.vanir.sensorhacks;

/**
 * Created by Γιώργος on 31/1/2018.
 */

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.util.Log;

import com.example.vanir.sensorhacks.db.ActuatorEntity;
import com.example.vanir.sensorhacks.db.AppDatabase;
import com.example.vanir.sensorhacks.db.SensorEntity;
import com.example.vanir.sensorhacks.db.SensorValueEntity;

import java.util.Date;
import java.util.List;

/**
 * Repository handling the work with sensors.
 */
public class DataRepository {

    private static final String TAG = "AESFsf";
    private static DataRepository sInstance;
    private final AppDatabase mDatabase;
    private MediatorLiveData<List<SensorEntity>> mObservableSensors;
    private MediatorLiveData<List<ActuatorEntity>> mObservableActuators;
    private MediatorLiveData<List<SensorValueEntity>> mObservableSensorValues;
    private int id;
    private String name;

    private DataRepository(final AppDatabase database) {

        mDatabase = database;
        mObservableSensors = new MediatorLiveData<>();
        mObservableActuators = new MediatorLiveData<>();
        mObservableSensorValues = new MediatorLiveData<>();


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

        mObservableSensorValues.addSource(mDatabase.sensorValueDao().loadAllSensorValues(),
                sensorValueEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableSensorValues.postValue(sensorValueEntities);
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

    public List<String> loadSensorNames() {
        return mDatabase.sensorDAO().loadNames();
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

    public void updateSensor(SensorEntity sensorEntity) {
        mDatabase.sensorDAO().updateSensor(sensorEntity);
    }

    public void updateSensorValue(Double newValue, int sensorId) {
        mDatabase.sensorDAO().updateSensorValue(newValue, sensorId);
    }

    /**
     * Here we query sensor values
     */

    public void insertSensorValue(SensorValueEntity valueEntity) {
        mDatabase.sensorValueDao().insert(valueEntity);
    }

    public SensorValueEntity getLastSensorEntry() {
        return mDatabase.sensorValueDao().getLastSensorEntry();
    }

    public LiveData<List<SensorValueEntity>> loadAllSensorValues() {
        return mObservableSensorValues;
    }

    public List<SensorValueEntity> loadAllSensorValuesSync(int id) {
        return mDatabase.sensorValueDao().loadAllSensorValuesSync(id);
    }

    public LiveData<List<SensorValueEntity>> getValuesOnIdandName(int id, String name) {
        this.id = id;
        this.name = name;
        return mObservableSensorValues;
    }

//    public void loadValuesforSensor(int id, String name) {
//        mDatabase.sensorValueDao().loadValuesforSensor(id, name);
//    }

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

    public void updateActuator(ActuatorEntity actuator) {
        mDatabase.actuatorDAO().updateActuator(actuator);
    }

}
