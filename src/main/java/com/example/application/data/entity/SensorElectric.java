package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SensorElectric {

    @Id
    private int sensor_id;
    private float pricePerKwHigh;
    private float pricePerKwLow;
    private float priceFix;
    private float priceService;
    private int implPerKw;
    private boolean isHighRate;

    public SensorElectric() {
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public float getPricePerKwHigh() {
        return pricePerKwHigh;
    }

    public void setPricePerKwHigh(float pricePerKwHigh) {
        this.pricePerKwHigh = pricePerKwHigh;
    }

    public float getPricePerKwLow() {
        return pricePerKwLow;
    }

    public void setPricePerKwLow(float pricePerKwLow) {
        this.pricePerKwLow = pricePerKwLow;
    }

    public float getPriceFix() {
        return priceFix;
    }

    public void setPriceFix(float priceFix) {
        this.priceFix = priceFix;
    }

    public float getPriceService() {
        return priceService;
    }

    public void setPriceService(float priceService) {
        this.priceService = priceService;
    }

    public int getImplPerKw() {
        return implPerKw;
    }

    public void setImplPerKw(int implPerKw) {
        this.implPerKw = implPerKw;
    }

    public boolean isHighRate() {
        return isHighRate;
    }

    public void setHighRate(boolean highRate) {
        isHighRate = highRate;
    }
}
