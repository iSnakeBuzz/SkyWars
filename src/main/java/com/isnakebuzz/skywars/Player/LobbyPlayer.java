package com.isnakebuzz.skywars.Player;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class LobbyPlayer {

    private final UUID uuid;
    private final String name;

    //Cosmetics
    private String cageName;
    private String selectedKit;
    private List<String> purchCages;
    private List<String> purchKits;

    //Solo stats
    private int solo_wins;
    private int solo_kills;
    private int solo_deaths;

    //Team stats
    private int team_wins;
    private int team_kills;
    private int team_deaths;


    public LobbyPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        //Cosmetics
        this.cageName = "default";
        this.selectedKit = "default";
        this.purchCages = new ArrayList<>();
        this.purchKits = new ArrayList<>();

        //Solo Stats
        this.solo_wins = 0;
        this.solo_kills = 0;
        this.solo_deaths = 0;

        //Team Stats
        this.team_wins = 0;
        this.team_kills = 0;
        this.team_deaths = 0;

    }

}
