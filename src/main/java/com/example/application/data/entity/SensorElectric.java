package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SensorElectric {

    @Id
    private int sensor_id;
    private Double pricePerKwHigh;
    private Double pricePerKwLow;
    private Double priceFix;
    private Double priceService;
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

    public Double getPricePerKwHigh() {
        return pricePerKwHigh;
    }

    public void setPricePerKwHigh(Double pricePerKwHigh) {
        this.pricePerKwHigh = pricePerKwHigh;
    }

    public Double getPricePerKwLow() {
        return pricePerKwLow;
    }

    public void setPricePerKwLow(Double pricePerKwLow) {
        this.pricePerKwLow = pricePerKwLow;
    }

    public Double getPriceFix() {
        return priceFix;
    }

    public void setPriceFix(Double priceFix) {
        this.priceFix = priceFix;
    }

    public Double getPriceService() {
        return priceService;
    }

    public void setPriceService(Double priceService) {
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
