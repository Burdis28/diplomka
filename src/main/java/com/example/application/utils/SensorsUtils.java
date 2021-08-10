package com.example.application.utils;

import com.example.application.data.entity.Sensor;

public class SensorsUtils {

    public static String getBadgeType(Sensor sensor) {
        switch (sensor.getType()) {
            case "w": return "badge primary";
            case "e": return "badge error primary";
            case "g": return "badge success primary";
            default: return "";
        }
    }
}
