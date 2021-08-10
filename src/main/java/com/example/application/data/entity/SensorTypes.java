package com.example.application.data.entity;

public enum SensorTypes {
    w ("Water"),
    e ("Electric"),
    g ("Gas");

    private final String sensorType;

    SensorTypes(String sensorType) {
        this.sensorType = sensorType;
    }

    @Override
    public String toString() {
        return sensorType;
    }

    public static SensorTypes fromCode(String code) {
        switch (code) {
            case "Water": return w;
            case "Electric": return e;
            case "Gas": return g;
            default: return null;
        }
    }
}
