package com.example.vanir.sensorhacks.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Γιώργος on 3/6/2018.
 */

@Dao
public interface ActuatorDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ActuatorEntity> actuators);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActuatorEntity actuator);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateActuator(ActuatorEntity actuator);

    @Delete
    void deleteActuator(ActuatorEntity actuator);

    @Query("SELECT * FROM actuators")
    LiveData<List<ActuatorEntity>> loadAllActuators();

    @Query("SELECT * FROM actuators")
    List<ActuatorEntity> loadAllActuatorsSync();

    @Query("SELECT id FROM actuators")
    List<Integer> loadIds();

    @Query("SELECT * FROM actuators WHERE id = :actuatorId")
    LiveData<ActuatorEntity> loadActuator(int actuatorId);

    @Query("SELECT * FROM actuators WHERE id = :actuatorId")
    ActuatorEntity loadActuatorSync(int actuatorId);

    @Query("DELETE FROM actuators")
    void removeAllActuators();
}
