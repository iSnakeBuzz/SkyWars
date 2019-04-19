package com.isnakebuzz.skywars.Utils.Enums;

public enum ChestType {

    OVERPOWERED("overpowered"),
    NORMAL("normal"),
    BASIC("basic");

    String name;

    ChestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
