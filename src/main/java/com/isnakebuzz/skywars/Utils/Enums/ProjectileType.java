package com.isnakebuzz.skywars.Utils.Enums;

import java.util.NavigableMap;

public enum ProjectileType {

    TWROWABLES("no throwables"),
    NORMAL("normal projectiles"),
    SOFTBLOCKS("soft blocks");

    String name;

    ProjectileType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
