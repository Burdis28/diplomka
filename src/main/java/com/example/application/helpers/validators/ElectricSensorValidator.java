package com.example.application.helpers.validators;

public class ElectricSensorValidator {

    public static boolean validateLowHigh(Double low, Double high) {
        return low < high;
    }

}
