package com.isnakebuzz.skywars.Teams;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private List<Player> teamPlayers;
    private String name;

    public Team(String name) {
        this.name = name;

        //Internal uses
        this.teamPlayers = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(Player p) {
        if (!this.teamPlayers.contains(p)) {
            this.teamPlayers.add(p);
        }
    }

    public void removePlayer(Player p) {
        if (this.teamPlayers.contains(p)) {
            this.teamPlayers.remove(p);
        }
    }

    public boolean containsPlayer(Player p) {
        return this.teamPlayers.contains(p);
    }

    public List<Player> getTeamPlayers() {
        return teamPlayers;
    }
}
