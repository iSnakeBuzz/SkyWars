package com.isnakebuzz.skywars.Arena;

import com.google.common.collect.Lists;
import com.isnakebuzz.ccsigns.Enums.GameStates;
import com.isnakebuzz.ccsigns.Enums.PacketType;
import com.isnakebuzz.ccsigns.utils.SignsAPI;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Teams.Team;
import com.isnakebuzz.skywars.Utils.Cuboids.Cuboid;
import com.isnakebuzz.skywars.Utils.Enums.*;
import com.isnakebuzz.skywars.Utils.LocUtils;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SkyWarsArena {

    //Internal usages
    private Main plugin;
    private GameStatus gameStatus;
    private GameType gameType;
    private List<Player> gamePlayers;
    private HashMap<LivingEntity, LivingEntity> lastDamager;
    private HashMap<LivingEntity, Integer> lastDmgTime;

    //Map name
    private String mapName;

    //Players
    private int minPlayers;
    private int maxPlayers;

    //Arena is ingame
    private boolean started;

    //Timers
    private int startingTime;
    private int cageOpens;
    private int endTimer;
    private int refillTimer;

    //Locations
    private Location lobbyLocation;
    private List<Location> lobbyRegion;
    private List<Location> spawnLocations;
    private List<Location> centerChestLocs;
    private List<Location> islandChestLocs;

    //Vote managers
    private ChestType chestType;
    private TimeType timeType;
    private ProjectileType projectileType;

    public SkyWarsArena(Main plugin) {
        this.plugin = plugin;
        this.gamePlayers = new ArrayList<>();
        this.spawnLocations = new ArrayList<>();
        this.centerChestLocs = new ArrayList<>();
        this.islandChestLocs = new ArrayList<>();
        this.lobbyRegion = new ArrayList<>();
        this.lastDamager = new HashMap<>();
        this.lastDmgTime = new HashMap<>();

        Configuration arena = plugin.getConfig("Extra/Arena");
        this.minPlayers = arena.getInt("Players.min");
        this.maxPlayers = arena.getInt("Players.max");
        this.mapName = arena.getString("MapName");
        this.startingTime = arena.getInt("Timers.Starting");
        this.cageOpens = arena.getInt("Timers.CageOpens");
        this.endTimer = arena.getInt("Timers.End");
        this.refillTimer = arena.getInt("Timers.ChestRefill");

        this.lobbyLocation = LocUtils.stringToLoc(arena.getString("Lobby"));
        this.gameType = Statics.skyMode;
        this.chestType = ChestType.NORMAL;
        this.timeType = TimeType.DAY;
        this.projectileType = ProjectileType.NORMAL;

        //Loading spawns
        Set<String> keys = arena.getConfigurationSection("Spawns").getKeys(false);
        for (String key : keys) {
            String path = "Spawns." + key;
            this.spawnLocations.add(LocUtils.stringToLoc(arena.getString(path)));
        }

        //Loading lobby region
        Set<String> lobbyRegKeys = arena.getConfigurationSection("LobbyArea").getKeys(false);
        for (String key : lobbyRegKeys) {
            String path = "LobbyArea." + key;
            this.lobbyRegion.add(LocUtils.stringToLoc(arena.getString(path)));
        }

        //Loading chests
        Set<String> centerChestKeys = arena.getConfigurationSection("CenterChest").getKeys(false);
        for (String key : centerChestKeys) {
            String path = "CenterChest." + key;
            this.centerChestLocs.add(LocUtils.stringToLoc(arena.getString(path)));
        }
        Set<String> islandChestKeys = arena.getConfigurationSection("IslandChest").getKeys(false);
        for (String key : islandChestKeys) {
            String path = "IslandChest." + key;
            this.islandChestLocs.add(LocUtils.stringToLoc(arena.getString(path)));
        }

        this.started = false;
        setGameStatus(GameStatus.WAITING);
    }

    public void cancelStart() {
        this.started = false;
        Configuration arena = plugin.getConfig("Extra/Arena");

        this.startingTime = arena.getInt("Timers.Starting");
        this.cageOpens = arena.getInt("Timers.CageOpens");
        this.gameType = Statics.skyMode;
        this.chestType = ChestType.NORMAL;
        this.timeType = TimeType.DAY;
        this.projectileType = ProjectileType.NORMAL;
        setGameStatus(GameStatus.WAITING);


    }

    public String getMapName() {
        return mapName;
    }

    public int getCageOpens() {
        return cageOpens;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getStartingTime() {
        return startingTime;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public void setStatrtingTime(int startingTime) {
        this.startingTime = startingTime;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setCageOpens(int cageOpens) {
        this.cageOpens = cageOpens;
    }

    public int getRefillTimer() {
        return refillTimer;
    }

    public String getParsedRefill() {
        return plugin.getTimerManager().transformToDate(this.refillTimer);
    }

    public List<Location> getCenterChestLocs() {
        return centerChestLocs;
    }

    public List<Location> getIslandChestLocs() {
        return islandChestLocs;
    }

    public void setRefillTimer(int refillTimer) {
        this.refillTimer = refillTimer;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        if (Statics.isCCSings) {
            if (Bukkit.getOnlinePlayers().size() >= this.getMaxPlayers()) {
                SignsAPI.sendPacket(PacketType.STATE, Statics.BungeeID, GameStates.FULL);
                return;
            }
            switch (gameStatus) {
                case WAITING:
                    SignsAPI.sendPacket(PacketType.STATE, Statics.BungeeID, GameStates.WAITING);
                    break;
                case STARTING:
                    SignsAPI.sendPacket(PacketType.STATE, Statics.BungeeID, GameStates.STARTING);
                    break;
                case CAGEOPENING:
                    SignsAPI.sendPacket(PacketType.STATE, Statics.BungeeID, GameStates.INGAME);
                    break;
                case FINISH:
                    SignsAPI.sendPacket(PacketType.STATE, Statics.BungeeID, GameStates.RESTARTING);
                    break;
            }
        }
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public List<Player> getGamePlayers() {
        return gamePlayers;
    }

    public int getEndTimer() {
        return endTimer;
    }

    public void setEndTimer(int endTimer) {
        this.endTimer = endTimer;
    }

    public void setChestType(ChestType chestType) {
        this.chestType = chestType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public ChestType getChestType() {
        return chestType;
    }

    public synchronized boolean checkStart() {
        if (!this.started) {
            if (this.getGamePlayers().size() >= this.getMinPlayers()) {
                this.started = true;
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public Team checkWin() {
        List<Team> aliveTeams = Lists.newArrayList();
        for (Team team : plugin.getTeamManager().getTeamMap().values()) {
            if (!team.isDead() && team.getTeamPlayers().size() >= 1) aliveTeams.add(team);
        }

        //If aliveTeams is empty, return first team
        if (aliveTeams.isEmpty()) return plugin.getTeamManager().getTeam(1);

        plugin.debug("Check - Win: " + "Alive TEAMS: " + aliveTeams.size() + ", Is true?: " + ((aliveTeams.size() <= 1) ? aliveTeams.get(0).getName() : "none"));
        return (aliveTeams.size() <= 1) ? aliveTeams.get(0) : null;
    }

    public void fillChests() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
            plugin.debug("Filling chests");
            for (Location loc : this.islandChestLocs) {
                Block block = loc.getWorld().getBlockAt(loc);
                if (block.getState() instanceof Chest) {
                    Chest chest = (Chest) block.getState();
                    plugin.getChestController().reFillIslands(chest);
                }
            }

            for (Location loc : this.centerChestLocs) {
                Block block = loc.getWorld().getBlockAt(loc);
                if (block.getState() instanceof Chest) {
                    Chest chest = (Chest) block.getState();
                    plugin.getChestController().reFillCenter(chest);
                }
            }
        });
    }

    public HashMap<LivingEntity, Integer> getLastDmgTime() {
        return lastDmgTime;
    }

    public HashMap<LivingEntity, LivingEntity> getLastDamager() {
        return lastDamager;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void removeLobby() {
        Location loc1 = lobbyRegion.get(0);
        Location loc2 = lobbyRegion.get(1);

        Cuboid cuboid = new Cuboid(loc1, loc2);
        for (Block b : cuboid) {
            if (!b.getType().equals(Material.AIR)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> b.setType(Material.AIR));
            }
        }
    }

}
