package com.isnakebuzz.skywars.Arena;

import com.google.common.collect.Lists;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.isnakebuzz.skywars.Calls.Events.SkyStartEvent;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Teams.Team;
import com.isnakebuzz.skywars.Utils.*;
import com.isnakebuzz.skywars.Utils.Cuboids.Cuboid;
import com.isnakebuzz.skywars.Utils.Enums.*;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import com.isnakebuzz.snakegq.Enums.GameQueueStatus;
import com.isnakebuzz.snakegq.Enums.GameQueueType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class SkyWarsArena {

    //Internal usages
    private final SkyWars plugin;
    private final List<Player> gamePlayers;
    private final HashMap<LivingEntity, LivingEntity> lastDamager;
    private final HashMap<LivingEntity, Integer> lastDmgTime;
    //Map name
    private final String mapName;
    //Players
    private final int minPlayers;
    private final int maxPlayers;
    //Locations
    private final SnakeLocation lobbyLocation;
    private final List<SnakeLocation> lobbyRegion;
    private final List<SnakeLocation> spawnLocations;
    private final List<SnakeLocation> centerChestLocs;
    private final List<SnakeLocation> islandChestLocs;
    private GameStatus gameStatus;
    private GameType gameType;
    //Arena is ingame
    private boolean started;
    //Timers
    private int startingTime;
    private int cageOpens;
    private int endTimer;
    //Vote managers
    private ChestType chestType;
    private TimeType timeType;
    private ProjectileType projectileType;

    public SkyWarsArena(SkyWars plugin, Configuration arena) {
        this.plugin = plugin;
        this.gamePlayers = new ArrayList<>();
        this.spawnLocations = new ArrayList<>();
        this.centerChestLocs = new ArrayList<>();
        this.islandChestLocs = new ArrayList<>();
        this.lobbyRegion = new ArrayList<>();
        this.lastDamager = new HashMap<>();
        this.lastDmgTime = new HashMap<>();

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
        if (Statics.skyMode != GameType.SOLO) {
            Set<String> lobbyRegKeys = arena.getConfigurationSection("LobbyArea").getKeys(false);
            for (String key : lobbyRegKeys) {
                String path = "LobbyArea." + key;
                this.lobbyRegion.add(LocUtils.stringToLoc(arena.getString(path)));
            }
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

    public void setCageOpens(int cageOpens) {
        this.cageOpens = cageOpens;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getStartingTime() {
        return startingTime;
    }

    public List<SnakeLocation> getSpawnLocations() {
        return spawnLocations;
    }

    public void setStatrtingTime(int startingTime) {
        this.startingTime = startingTime;
    }

    public Location getLobbyLocation() {
        return lobbyLocation.getLocation();
    }

    public List<SnakeLocation> getCenterChestLocs() {
        return centerChestLocs;
    }

    public List<SnakeLocation> getIslandChestLocs() {
        return islandChestLocs;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        plugin.debug("Changing Status: " + gameStatus.toString());
        if (Statics.SnakeGameQueue) {
            switch (gameStatus) {
                case WAITING:
                    GameQueueAPI.updateStatus(Statics.BungeeID, GameQueueStatus.WAITING);
                    break;
                case STARTING:
                    GameQueueAPI.updateStatus(Statics.BungeeID, GameQueueStatus.STARTING);
                    break;
                case INGAME:
                    GameQueueAPI.updateStatus(Statics.BungeeID, GameQueueStatus.INGAME);
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

    public List<Player> getGamePlayers() {
        return gamePlayers;
    }

    public int getEndTimer() {
        return endTimer;
    }

    public void setEndTimer(int endTimer) {
        this.endTimer = endTimer;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    public ChestType getChestType() {
        return chestType;
    }

    public void setChestType(ChestType chestType) {
        this.chestType = chestType;
    }

    public synchronized boolean checkStart() {
        plugin.debug("Started: " + this.started);

        if (!this.started) {

            plugin.debug("Players: " + this.getMinPlayers() + ", " + this.getGamePlayers().size());

            if (this.getGamePlayers().size() >= this.getMinPlayers()) {
                this.started = true;

                //Calling start event
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new SkyStartEvent(this)));

                plugin.debug("Started = true");
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
        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.debug("Filling chests");
            for (SnakeLocation snakeLoc : this.islandChestLocs) {
                Location loc = snakeLoc.getLocation();
                Block block = loc.getWorld().getBlockAt(loc);
                if (block.getState() instanceof Chest) {
                    Chest chest = (Chest) block.getState();
                    plugin.getChestController().reFillIslands(chest);
                }
            }

            for (SnakeLocation snakeLoc : this.centerChestLocs) {
                Location loc = snakeLoc.getLocation();
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
        for (Player online : Bukkit.getOnlinePlayers()) {
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
        Location loc1 = lobbyRegion.get(0).getLocation();
        Location loc2 = lobbyRegion.get(1).getLocation();

        Cuboid cuboid = new Cuboid(loc1, loc2);
        for (Block b : cuboid) {
            if (!b.getType().equals(Material.AIR)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> b.setType(Material.AIR));
            }
        }
    }

    public void reloadWorld() {
        SlimePlugin slimeInstance = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader fileLoader = slimeInstance.getLoader("file");

        Console.debug(String.format("Reloading %s", mapName));
        Bukkit.unloadWorld(mapName, true);

        Bukkit.getScheduler().runTask(plugin, () -> {
            Console.debug(String.format("Reloading Task of %s", mapName));
            try {
                fileLoader.unlockWorld(mapName);
            } catch (UnknownWorldException | IOException e) {
                e.printStackTrace();
            }

            SlimePropertyMap slimeProperties = new SlimePropertyMap();
            slimeProperties.setString(SlimeProperties.WORLD_TYPE, "flat");
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = slimeInstance.loadWorld(fileLoader, mapName, true, slimeProperties);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }

            slimeInstance.generateWorld(slimeWorld);

            World world = Bukkit.getWorld(mapName);
            world.setAutoSave(false);
            world.getWorldBorder().setSize(10000);
            world.setDifficulty(Difficulty.HARD);
            world.setFullTime(6000);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            Bukkit.setSpawnRadius(0);

            Console.debug(String.format("Finished Task of %s", mapName));
        });
    }
}
