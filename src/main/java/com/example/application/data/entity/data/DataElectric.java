package com.example.application.data.entity.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class DataElectric {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    private int sensorId;
    private Timestamp time;
    private Double highRate;
    private Double lowRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getHighRate() {
        return highRate;
    }

    public void setHighRate(Double highRate) {
        this.highRate = highRate;
    }

    public Double getLowRate() {
        return lowRate;
    }

    public void setLowRate(Double lowRate) {
        this.lowRate = lowRate;
    }
}
