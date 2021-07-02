package com.example.application.data.entity;

public enum SensorTypes {
    w ("Water"),
    e ("Electric"),
    g ("Gas");

    private final String name;

    SensorTypes(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
