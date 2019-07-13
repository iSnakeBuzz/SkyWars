package com.isnakebuzz.skywars.Teams;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Calls.Events.SkyStatsEvent;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.StatType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Team {

    private String name;
    private int teamID;

    //Team settings
    private List<SkyPlayer> teamPlayers;
    private boolean isDead;

    public Team(String name, int teamID) {
        this.name = name;
        this.teamID = teamID;

        //Internal uses
        this.teamPlayers = new ArrayList<>();
        this.isDead = false;
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
        if (this.getTeamPlayers().isEmpty()) return "default";
        SkyPlayer random = this.getTeamPlayers().get(new Random().nextInt(this.getTeamPlayers().size()));
        return random.getCageName();
    }

    public int getID() {
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
        return (this.getID() - 1) < 0 ? 0 : (this.getID() - 1);
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    public void checkDead() {
        List<SkyPlayer> playersDead = Lists.newArrayList();
        for (SkyPlayer skyPlayer : this.getTeamPlayers()) if (skyPlayer.isDead()) playersDead.add(skyPlayer);

        if (playersDead.size() >= this.getTeamPlayers().size()) setDead(true);
    }

    public void addWin() {
        for (SkyPlayer skyPlayer : getTeamPlayers()) {
            SkyStatsEvent statsEvent = new SkyStatsEvent(skyPlayer, StatType.WIN);
            Bukkit.getPluginManager().callEvent(statsEvent);

            if (!statsEvent.isCancelled()) {
                skyPlayer.addWins(1);
            }
        }
    }
}
