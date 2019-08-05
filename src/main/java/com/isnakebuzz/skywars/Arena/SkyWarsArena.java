package com.isnakebuzz.skywars.Arena;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Calls.Events.SkyStartEvent;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Teams.Team;
import com.isnakebuzz.skywars.Utils.Cuboids.Cuboid;
import com.isnakebuzz.skywars.Utils.Enums.*;
import com.isnakebuzz.skywars.Utils.LocUtils;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import com.isnakebuzz.snakegq.Enums.GameQueueStatus;
import com.isnakebuzz.snakegq.Enums.GameQueueType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

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

    public List<Location> getCenterChestLocs() {
        return centerChestLocs;
    }

    public List<Location> getIslandChestLocs() {
        return islandChestLocs;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        if (Statics.SnakeGameQueue) {
            switch (gameStatus) {
                case WAITING:
                    GameQueueAPI.updateStatus(Statics.BungeeID, GameQueueStatus.WAITING);
                    break;
                case STARTING:
                    GameQueueAPI.updateStatus(Statics.BungeeID, GameQueueStatus.STARTING);
                    break;
                case CAGEOPENING:
                    GameQueueAPI.updateStatus(Statics.BungeeID, GameQueueStatus.CAGEOPENING);
                    break;
                case FINISH:
                    GameQueueAPI.updateStatus(Statics.BungeeID, GameQueueStatus.FINISH);
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

                //Calling start event
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new SkyStartEvent(this)));

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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
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

    public void SEND_ALL_TO_NEW_GAME() {
        if (this.getGameType().equals(GameType.TEAM)) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                GameQueueAPI.nextGame(online, GameQueueType.TEAM);
            }
        } else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                GameQueueAPI.nextGame(online, GameQueueType.SOLO);
            }
        }
    }

    public void SEND_ALL_TO_LOBBY() {
        for (Player online : Bukkit.getOnlinePlayers()){
            String lobby = Statics.lobbies.get(new Random().nextInt(Statics.lobbies.size()));
            PacketsAPI.connect(plugin, online, lobby);
        }
    }

    public void SEND_TO_NEW_GAME(Player player) {
        if (this.getGameType().equals(GameType.TEAM))
            GameQueueAPI.nextGame(player, GameQueueType.TEAM);
        else
            GameQueueAPI.nextGame(player, GameQueueType.SOLO);
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
