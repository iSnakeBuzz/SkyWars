package com.isnakebuzz.skywars.Player;

import com.isnakebuzz.netcore.NetCoreAPI;
import com.isnakebuzz.skywars.Teams.Team;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class SkyPlayer implements Comparable<SkyPlayer> {

    private final UUID uuid;
    private final String name;

    //Cosmetics
    private String cageName;
    private String selectedKit;
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

    public SkyPlayer(UUID uuid, String username) {
        this.uuid = uuid;
        this.name = username;

        //Cosmetics
        this.cageName = "default";
        this.selectedKit = "default";
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
        this.team = null;

        //Votes
        this.isChest = false;
        this.isTime = false;
        this.isProjectile = false;
    }

    public String getName() {
        return name;
    }

    public void addDeaths(int value) {
        this.setDeaths(getDeaths() + value);
    }

    public void addKills(int value) {
        this.setKills(getKills() + value);
    }

    public void addWins(int value) {
        this.setWins(getWins() + value);
    }

    public void setSpectator(boolean spectator) {
        isSpectator = spectator;

        Player p = Bukkit.getPlayer(this.uuid);
        if (spectator) {
            PlayerUtils.clean(p, new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setGameSpect(true);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.hidePlayer(p);
            }
        } else {
            PlayerUtils.clean(p);
            p.setGameMode(GameMode.SURVIVAL);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(p);
            }
        }
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
        isSpectator = staff;

        Player p = Bukkit.getPlayer(this.uuid);
        if (staff) {
            PlayerUtils.clean(p, new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setGameSpect(true);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.hidePlayer(p);
            }
        } else {
            PlayerUtils.clean(p);
            p.setGameMode(GameMode.SURVIVAL);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(p);
            }
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public void addKillStreak() {
        this.setKillStreak(this.getKillStreak() + 1);
    }

    public void setTeam(Team team) {
        System.out.println("Team Name: " + team.getName());
        team.addPlayer(this);
        this.team = team;
    }

    public void addCoins(int coins) {
        if (this.getPlayer() != null) {
            NetCoreAPI.addCoins(this.getPlayer(), coins);
        }
    }

    @Override
    public int compareTo(SkyPlayer o) {
        return o.getKillStreak() - this.killStreak;
    }

    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
    }
}
