package com.example.application.utils;

import com.example.application.data.entity.Sensor;

public class SensorsUtils {

    public static String getBadgeType(Sensor sensor) {
        return switch (sensor.getType()) {
            case "w" -> "badge primary";
            case "e" -> "badge error primary";
            case "g" -> "badge success primary";
            default -> "";
        };
    }
}
