package com.example.vanir.sensorhacks;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Γιώργος on 9/7/2018.
 */

public class Converters {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    //date.getTime()

    @TypeConverter
    public static Date fromTimeStamp(String value) {
        try {
            return value == null ? null : dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String dateToTimeStamp(Date date) {
        return date == null ? null : dateFormat.format(date.getTime());
    }
}
