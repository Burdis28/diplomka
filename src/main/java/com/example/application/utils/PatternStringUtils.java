package com.example.application.utils;

public class PatternStringUtils {

    public static final String passwordRegex = "^(?=.*[A-Z])(?=.*[\\W])(?=.*[0-9])(?=.*[a-z]).{8,128}$";
    public static final String passwordErrorMessage = "Password needs to be at least 8 characters long, use both upper and lower case letter" +
            ", special character and a number.";
    public static final String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
    public static final String phoneErrorMessage = "This is not a valid phone number.";
    public static final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String emailErrorMessage = "Please enter a valid email address.";
}
