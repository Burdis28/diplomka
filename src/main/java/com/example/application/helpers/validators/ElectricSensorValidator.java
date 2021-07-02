package com.example.application.helpers.validators;

public class ElectricSensorValidator {

    public static boolean validateLowHigh(float low, float high) {
        return low < high;
    }

}
