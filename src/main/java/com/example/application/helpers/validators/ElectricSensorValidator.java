package com.example.application.helpers.validators;

/**
 * Utils class for specific electric sensor data validations.
 */
public class ElectricSensorValidator {

    public static boolean validateLowHigh(Double low, Double high) {
        return low < high;
    }

}
