package com.example.application.data.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    private String idHw;
    private String name;
    private String type;
    private Timestamp createdDate;
    private Double limit_day;
    private Double limit_month;
    private Double consumptionActual;
    private Double consumptionCorrelation;
    private String currencyString;
    private int pinId;

    public String getIdHw() {
        return idHw;
    }

    public void setIdHw(String idHw) {
        this.idHw = idHw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Double getLimit_day() {
        return limit_day;
    }

    public void setLimit_day(Double limit_day) {
        this.limit_day = limit_day;
    }

    public Double getLimit_month() {
        return limit_month;
    }

    public void setLimit_month(Double limit_month) {
        this.limit_month = limit_month;
    }

    public String getCurrencyString() {
        return currencyString;
    }

    public void setCurrencyString(String currencyString) {
        this.currencyString = currencyString;
    }

    public Double getConsumptionActual() {
        return consumptionActual;
    }

    public void setConsumptionActual(Double consumptionActual) {
        this.consumptionActual = consumptionActual;
    }

    public Double getConsumptionCorrelation() {
        return consumptionCorrelation;
    }

    public void setConsumptionCorrelation(Double consumptionCorrelation) {
        this.consumptionCorrelation = consumptionCorrelation;
    }

    public int getPinId() {
        return pinId;
    }

    public void setPinId(int pinId) {
        this.pinId = pinId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
