package com.example.application.views.sensors.components;

import java.time.LocalDate;

public class MonthYearKey {

    private LocalDate date;
    private String key;

    public MonthYearKey() {
    }

    public MonthYearKey(LocalDate date, String key) {
        this.date = date;
        this.key = key;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
