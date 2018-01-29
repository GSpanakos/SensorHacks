package com.example.vanir.sensorhacks.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Γιώργος on 14/1/2018.
 */

@Entity
public class SensorDB {

    @PrimaryKey
    public String name;
    //  public final int id;

    public String type;
    public Boolean status;
    public double value;

    public SensorDB(/*int id,*/String name, String type, Boolean status, double value) {
//      this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return Boolean.toString(status);
    }

    public String getValue() {
        return Double.toString(value);
    }
}
