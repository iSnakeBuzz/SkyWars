package com.isnakebuzz.skywars.Database.Utils;

import lombok.Getter;

@Getter
public enum RoutePath {

    PLAYER("player/"),
    SOLO("solo/"),
    TEAM("team/");

    private String path;

    RoutePath(String path) {
        this.path = path;
    }

}
