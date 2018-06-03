package com.example.vanir.sensorhacks.model;

/**
 * Created by Γιώργος on 3/6/2018.
 */

public interface Actuator {

    int getId();

    String getName();

    String getType();

    Boolean getStatus();
}
