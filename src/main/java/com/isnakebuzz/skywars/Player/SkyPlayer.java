package com.isnakebuzz.skywars.Player;

import com.isnakebuzz.skywars.Teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkyPlayer {

    private UUID uuid;

    //Stats
    private int wins;
    private int kills;
    private int deaths;
    private int killStreak;

    //Internal utils
    private boolean isDead;
    private boolean isSpectator;
    private boolean isStaff;
    private Team team;

    public SkyPlayer(UUID uuid) {
        this.uuid = uuid;

        this.wins = 0;
        this.kills = 0;
        this.deaths = 0;
        this.killStreak = 0;

        this.isDead = false;
        this.isSpectator = false;
        this.isStaff = false;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeaths(int value) {
        this.setDeaths(getDeaths() + value);
    }

    public int getKills() {
        return kills;
    }

    public void addKills(int value) {
        this.setKills(getKills() + value);
    }

    public int getWins() {
        return wins;
    }

    public void addWins(int value) {
        this.setWins(getWins() + value);
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;

        Player p = Bukkit.getPlayer(this.uuid);
        if (staff) {
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setGameMode(GameMode.ADVENTURE);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(40);
        } else {
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setGameMode(GameMode.SURVIVAL);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(40);
        }
    }

    public void setSpectator(boolean spectator) {
        isSpectator = spectator;

        Player p = Bukkit.getPlayer(this.uuid);
        if (spectator) {
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setGameMode(GameMode.ADVENTURE);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(40);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.hidePlayer(p);
            }
        } else {
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setGameMode(GameMode.SURVIVAL);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(40);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(p);
            }
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public int getKillStreak() {
        return killStreak;
    }

    public void setKillStreak(int killStreak) {
        this.killStreak = killStreak;
    }

    public void addKillStreak() {
        this.setKills(this.getKillStreak() + 1);
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
