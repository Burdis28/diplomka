package com.example.application.utils;

public enum Colors {

    GREEN ("#00ff00"),
    YELLOW("#ffff00"),
    ORANGE ("#ff8000"),
    RED ("#ff0000");

    private String rgb;

    Colors(String rgb) {
        this.rgb = rgb;
    }

    public String getRgb() {
        return rgb;
    }
}
