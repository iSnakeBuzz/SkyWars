package com.isnakebuzz.skywars.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyPlayer {

    private UUID uuid;

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


    public LobbyPlayer(UUID uuid) {
        this.uuid = uuid;

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

    public int getTeam_deaths() {
        return team_deaths;
    }

    public int getTeam_kills() {
        return team_kills;
    }

    public int getTeam_wins() {
        return team_wins;
    }

    public int getSolo_deaths() {
        return solo_deaths;
    }

    public int getSolo_kills() {
        return solo_kills;
    }

    public List<String> getPurchCages() {
        return purchCages;
    }

    public String getCageName() {
        return cageName;
    }

    public List<String> getPurchKits() {
        return purchKits;
    }

    public int getSolo_wins() {
        return solo_wins;
    }

    public String getSelectedKit() {
        return selectedKit;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setCageName(String cageName) {
        this.cageName = cageName;
    }

    public void setPurchCages(List<String> purchCages) {
        this.purchCages = purchCages;
    }

    public void setSelectedKit(String selectedKit) {
        this.selectedKit = selectedKit;
    }

    public void setPurchKits(List<String> purchKits) {
        this.purchKits = purchKits;
    }

    public void setSolo_deaths(int solo_deaths) {
        this.solo_deaths = solo_deaths;
    }

    public void setSolo_kills(int solo_kills) {
        this.solo_kills = solo_kills;
    }

    public void setSolo_wins(int solo_wins) {
        this.solo_wins = solo_wins;
    }

    public void setTeam_deaths(int team_deaths) {
        this.team_deaths = team_deaths;
    }

    public void setTeam_kills(int team_kills) {
        this.team_kills = team_kills;
    }

    public void setTeam_wins(int team_wins) {
        this.team_wins = team_wins;
    }
}
