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
        return switch (code) {
            case "Water" -> w;
            case "Electric" -> e;
            case "Gas" -> g;
            default -> null;
        };
    }
}
