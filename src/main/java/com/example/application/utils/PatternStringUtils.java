package com.example.application.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Month;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternStringUtils {

    public static final String passwordRegex = "^(?=.*[A-Z])(?=.*[\\W])(?=.*[0-9])(?=.*[a-z]).{8,128}$";
    public static final String passwordErrorMessage = "Password needs to be at least 8 characters long, use both upper and lower case letter" +
            ", special character and a number.";
    public static final String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
    public static final String phoneErrorMessage = "This is not a valid phone number.";
    public static final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String emailErrorMessage = "Please enter a valid email address.";
    public static final String fieldIsRequired = "This field is required / wrong input.";
    public static final String onlyNumbersRegex = "^[0-9]+$";
    public static final String doubleNumberRegex62 = "^(\\d{1,4})(?:.\\d{1,2})?\\r?$";
    public static final String doubleNumberRegex63 = "^(\\d{1,3})(?:.\\d{1,3})?\\r?$";
    public static final String doubleNumberRegex103 = "^(\\d{1,7})(?:.\\d{1,3})?\\r?$";
    public static final String yearRegex = "(?:(?:19|20)[0-9]{2})";
    public static final String yearErrorMessage = "Enter a valid year (1900-2099).";
    public static final List<Month> months = Arrays.asList(Month.values());

    public static String formatNumberToText(Double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.000", symbols);
        return df.format(number);
    }


}
