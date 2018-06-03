package com.example.vanir.sensorhacks.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.vanir.sensorhacks.model.Actuator;

/**
 * Created by Γιώργος on 3/6/2018.
 */

@Entity(tableName = "actuators")
public class ActuatorEntity implements Actuator {

    @PrimaryKey
    private int id;
    private String name;
    private String type;
    private Boolean status;

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

    public ActuatorEntity() {
    }

    public ActuatorEntity(int id, String name, String type, Boolean status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
    }


    public ActuatorEntity(Actuator actuator) {
        this.id = actuator.getId();
        this.name = actuator.getName();
        this.type = actuator.getType();
        this.status = actuator.getStatus();
    }
}
