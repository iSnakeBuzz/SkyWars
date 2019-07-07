package com.isnakebuzz.skywars.Teams;

import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Team {

    private List<SkyPlayer> teamPlayers;
    private String name;
    private int teamID;

    public Team(String name, int teamID) {
        this.name = name;
        this.teamID = teamID;

        //Internal uses
        this.teamPlayers = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(SkyPlayer p) {
        if (!this.teamPlayers.contains(p)) {
            this.teamPlayers.add(p);
        }
    }

    public void removePlayer(SkyPlayer p) {
        if (this.teamPlayers.contains(p)) {
            this.teamPlayers.remove(p);
        }
    }

    public String getCage() {
        SkyPlayer random = this.getTeamPlayers().get(new Random().nextInt(this.getTeamPlayers().size()));
        return random.getCageName();
    }

    public int getTeamID() {
        return teamID;
    }

    public boolean containsPlayer(Player p) {
        return this.teamPlayers.contains(p);
    }

    public List<SkyPlayer> getTeamPlayers() {
        return teamPlayers;
    }

    public int size() {
        return this.teamPlayers.size();
    }

    public int getSpawnID() {
        return Math.min(0, (this.getTeamID() - 1));
    }
}
