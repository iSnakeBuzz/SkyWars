package com.isnakebuzz.skywars.Player;

import com.isnakebuzz.skywars.Teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkyPlayer {

    private UUID uuid;

    //Cosmetics
    private String cageName;
    private String kitName;
    private List<String> purchCages;
    private List<String> purchKits;

    //Stats
    private int wins;
    private int kills;
    private int deaths;
    private int killStreak;

    //Internal utils
    private boolean isDead;
    private boolean isSpectator;
    private boolean isStaff;
    private boolean isSpectating;
    private Team team;

    //Votes
    private boolean isChest;
    private boolean isTime;
    private boolean isProjectile;

    public SkyPlayer(UUID uuid) {
        this.uuid = uuid;

        //Cosmetics
        this.cageName = "default";
        this.kitName = "default";
        this.purchCages = new ArrayList<>();
        this.purchKits = new ArrayList<>();

        //Stats
        this.wins = 0;
        this.kills = 0;
        this.deaths = 0;
        this.killStreak = 0;

        //Internal usages
        this.isDead = false;
        this.isSpectator = false;
        this.isStaff = false;
        this.isSpectator = false;

        //Votes
        this.isChest = false;
        this.isTime = false;
        this.isProjectile = false;
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

    public void setProjectile(boolean projectile) {
        isProjectile = projectile;
    }

    public void setTime(boolean time) {
        isTime = time;
    }

    public void setChest(boolean chest) {
        isChest = chest;
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
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(40);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.hidePlayer(p);
            }
        } else {
            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(false);
            p.setFlying(false);

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

    public String getCageName() {
        return cageName;
    }

    public String getKitName() {
        return kitName;
    }

    public List<String> getPurchCages() {
        return purchCages;
    }

    public List<String> getPurchKits() {
        return purchKits;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public void setPurchKits(List<String> purchKits) {
        this.purchKits = purchKits;
    }

    public void setCageName(String cageName) {
        this.cageName = cageName;
    }

    public void setPurchCages(List<String> purchCages) {
        this.purchCages = purchCages;
    }

    public boolean isSpectating() {
        return isSpectating;
    }

    public void setSpectating(boolean spectating) {
        isSpectating = spectating;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isProjectile() {
        return isProjectile;
    }

    public boolean isChest() {
        return isChest;
    }

    public boolean isTime() {
        return isTime;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
