package com.example.vanir.sensorhacks.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by Γιώργος on 9/7/2018.
 */

@Dao
public interface SensorValueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SensorValueEntity valueEntity);

    @Query("SELECT * FROM sensorValues ORDER BY date DESC LIMIT 1")
    SensorValueEntity getLastSensorEntry();

//    @Query("SELECT value, timestamp FROM sensorValues WHERE id = :id AND name = :name")
//    List<SensorValueEntity> loadValuesforSensor(int id, String name);
}
