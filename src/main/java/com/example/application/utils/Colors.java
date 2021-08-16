package com.example.application.utils;

public enum Colors {

    CEZ_TYPE_ORANGE("#f24f00"),
    GREEN ("#0abf56"),
    YELLOW("#ffff00"),
    ORANGE ("#ff8000"),
    GRAY("#696969"),
    BLUE("#1676f3"),
    BLACK("#000000"),
    RED ("#ff4238");

    private String rgb;

    Colors(String rgb) {
        this.rgb = rgb;
    }

    public String getRgb() {
        return rgb;
    }
}
