package com.example.vanir.sensorhacks.model;

import java.util.ArrayList;

/**
 * Created by Γιώργος on 23/10/2017.
 */

public interface Sensor {
    int getId();

    String getName();

    String getType();

    Boolean getStatus();

    double getValue();
}
