package com.isnakebuzz.skywars.Database.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoutePath {

    PLAYER("sw_player"),
    SOLO("sw_solo"),
    TEAM("sw_team");

    private final String path;

}
