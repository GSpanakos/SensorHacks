package com.example.vanir.sensorhacks.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.vanir.sensorhacks.model.Sensor;

/**
 * Created by Γιώργος on 14/1/2018.
 */

@Entity(tableName = "sensors")
public class SensorEntity implements Sensor {

    @PrimaryKey
    private int id;
    //  public final int id;
    private String name;
    private String type;
    private Boolean status;
    private double value;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public SensorEntity() {
    }

    public SensorEntity(int id, String name, String type, Boolean status, double value) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.value = value;
    }

    public SensorEntity(Sensor sensor) {
        this.id = sensor.getId();
        this.name = sensor.getName();
        this.type = sensor.getType();
        this.status = sensor.getStatus();
        this.value = sensor.getValue();
    }
}
