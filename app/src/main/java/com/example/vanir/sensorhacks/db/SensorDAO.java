package com.example.vanir.sensorhacks.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Γιώργος on 16/1/2018.
 */

@Dao
public interface SensorDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SensorEntity> sensors);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SensorEntity Sensor);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSensor(SensorEntity sensor);

    @Delete
    void deleteSensor(SensorEntity sensor);

    @Query("SELECT * FROM sensors")
    LiveData<List<SensorEntity>> loadAllSensors();

    @Query("SELECT * FROM sensors")
    List<SensorEntity> loadAllSensorsSync();

    @Query("SELECT id FROM sensors")
    List<Integer> loadIds();

    @Query("SELECT * FROM sensors WHERE id = :sensorId")
    LiveData<SensorEntity> loadSensor(int sensorId);

    @Query("SELECT * FROM sensors WHERE id = :sensorId")
    SensorEntity loadSensorSync(int sensorId);

    @Query("DELETE FROM sensors")
    void removeAllSensors();
}
