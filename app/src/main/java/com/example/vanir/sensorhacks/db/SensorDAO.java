package com.example.vanir.sensorhacks.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.vanir.sensorhacks.db.SensorDB;

import java.util.ArrayList;

/**
 * Created by Γιώργος on 16/1/2018.
 */

@Dao
public interface SensorDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSensor(SensorDB sensorDB);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSensor(SensorDB sensorDB);

    @Delete
    void deleteSensor(SensorDB sensorDB);

    @Query("select * from sensorDB")
    ArrayList<SensorDB> getallSensor();

    @Query("select * from sensorDB where id = :sensorId")
    SensorDB getSensor(double sensorId);

    @Query("delete from sensorDB")
    void removeAllSensor();
}
