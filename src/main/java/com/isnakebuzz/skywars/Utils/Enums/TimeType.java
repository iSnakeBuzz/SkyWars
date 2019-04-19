package com.isnakebuzz.skywars.Utils.Enums;

public enum TimeType {

    DAY("day"),
    SUNSET("sunset"),
    NIGHT("night");

    String name;

    TimeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
