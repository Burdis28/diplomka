package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
public class SensorWater extends AbstractEntity {

    private float pricerPerM3;
    private int implPerLit;
    private int state;
    private int stateModifierUserId;
    private Timestamp stateModifiedDate;
    private boolean stateModifierType;
    private int timeBtwImpl;
    private int countStop;
    private int countStopNight;
    private int nightStartHour;
    private int nightStartMinute;
    private int nightEndHour;
    private int nightEndMinute;

    public SensorWater() {
    }

    public float getPricerPerM3() {
        return pricerPerM3;
    }

    public void setPricerPerM3(float pricerPerM3) {
        this.pricerPerM3 = pricerPerM3;
    }

    public int getImplPerLit() {
        return implPerLit;
    }

    public void setImplPerLit(int implPerLit) {
        this.implPerLit = implPerLit;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStateModifierUserId() {
        return stateModifierUserId;
    }

    public void setStateModifierUserId(int stateModifierUserId) {
        this.stateModifierUserId = stateModifierUserId;
    }

    public Timestamp getStateModifiedDate() {
        return stateModifiedDate;
    }

    public void setStateModifiedDate(Timestamp stateModifiedDate) {
        this.stateModifiedDate = stateModifiedDate;
    }

    public boolean isStateModifierType() {
        return stateModifierType;
    }

    public void setStateModifierType(boolean stateModifierType) {
        this.stateModifierType = stateModifierType;
    }

    public int getTimeBtwImpl() {
        return timeBtwImpl;
    }

    public void setTimeBtwImpl(int timeBtwImpl) {
        this.timeBtwImpl = timeBtwImpl;
    }

    public int getCountStop() {
        return countStop;
    }

    public void setCountStop(int countStop) {
        this.countStop = countStop;
    }

    public int getCountStopNight() {
        return countStopNight;
    }

    public void setCountStopNight(int countStopNight) {
        this.countStopNight = countStopNight;
    }

    public int getNightStartHour() {
        return nightStartHour;
    }

    public void setNightStartHour(int nightStartHour) {
        this.nightStartHour = nightStartHour;
    }

    public int getNightStartMinute() {
        return nightStartMinute;
    }

    public void setNightStartMinute(int nightStartMinute) {
        this.nightStartMinute = nightStartMinute;
    }

    public int getNightEndHour() {
        return nightEndHour;
    }

    public void setNightEndHour(int nightEndHour) {
        this.nightEndHour = nightEndHour;
    }

    public int getNightEndMinute() {
        return nightEndMinute;
    }

    public void setNightEndMinute(int nightEndMinute) {
        this.nightEndMinute = nightEndMinute;
    }
}
