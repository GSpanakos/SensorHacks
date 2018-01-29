package com.example.vanir.sensorhacks.model;

import java.util.ArrayList;

/**
 * Created by Γιώργος on 23/10/2017.
 */

public class Sensor {

    //declare private data instead of public to ensure the privacy of data field of each class
    String name;
    String type;
    boolean status;
    double value;

    public Sensor(String name, String type, boolean status, double value) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.value = value;
    }

    //retrieve sensor's specs

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

//    public static ArrayList<Sensor> getSensors() {
//        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
//        sensors.add(new Sensor("DHT23", "Temp", false , 36.8));
//        sensors.add(new Sensor("GTY42", "Press", true, 105.32));
//        sensors.add(new Sensor("KJH79", "Humid", true, 78));
//
//        return sensors;
//    }


}
