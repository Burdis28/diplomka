package com.example.application.utils;

public enum RouteNames {
    SENSORS ("sensors"),
    CREATE_SENSOR("createsensor"),
    DASHBOARD ("dashboard"),
    LOGS ("logs"),
    USER ("user"),
    LOGOUT ("logout");

    private String name;

    RouteNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RouteNames fromName(String name) {
        return RouteNames.valueOf(name);
    }
}
