package com.example.vanir.sensorhacks.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.vanir.sensorhacks.Converters;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Γιώργος on 9/7/2018.
 */

@Entity(tableName = "sensorValues",
        indices = {@Index(value = {"id", "name"})},
        foreignKeys = @ForeignKey(entity = SensorEntity.class,
                parentColumns = {"id", "name"},
                childColumns = {"id", "name"},
                onDelete = CASCADE))
public class SensorValueEntity {

    @PrimaryKey(autoGenerate = true)
    private Integer row;


    private int id;
    @NonNull
    private String name;

    @TypeConverters({Converters.class})
    private Date date;

    private double value;

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SensorValueEntity(int id, @NonNull String name, Date date, double value) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.value = value;
    }

    public SensorValueEntity(SensorValueEntity valueEntity) {
        this.id = valueEntity.getId();
        this.name = valueEntity.getName();
        this.date = valueEntity.getDate();
        this.value = valueEntity.getValue();
    }
}
