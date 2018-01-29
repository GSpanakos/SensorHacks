package com.example.vanir.sensorhacks.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Γιώργος on 29/1/2018.
 */

public class DataGenerator {

    private static final String[] FIRST = new String[]{
            "KWA", "KEH", "LDU", "WYT", "AHE"};
    private static final String[] SECOND = new String[]{
            "452", "963", "937", "194"};
    private static final String[] TYPES = new String[]{
            "Temperature", "Humidity", "Pressure", "Mass", "Current", "Heat"};
    private static final String[] STATUSS = new String[]{"True", "False"};

    public static List<SensorEntity> generateSensors() {
        List<SensorEntity> sensors = new ArrayList<>(FIRST.length * SECOND.length);
        Random rnd = new Random();
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                SensorEntity sensor = new SensorEntity();
                sensor.setName(FIRST[i] + SECOND[j]);
                sensor.setType(TYPES[j]);
                sensor.setStatus(Boolean.valueOf(STATUSS[j / 2]));
                sensor.setValue(rnd.nextInt(240));
                sensor.setId(FIRST.length * i + j + 1);
                sensors.add(sensor);
            }
        }
        return sensors;
    }
}
