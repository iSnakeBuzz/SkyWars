package com.isnakebuzz.skywars;

import com.isnakebuzz.ccsigns.Enums.GameStates;
import com.isnakebuzz.ccsigns.Enums.PacketType;
import com.isnakebuzz.ccsigns.utils.SignsAPI;
import com.isnakebuzz.skywars.Arena.ArenaSetup;
import com.isnakebuzz.skywars.Arena.SkyWarsArena;
import com.isnakebuzz.skywars.Chest.ChestController;
import com.isnakebuzz.skywars.Configurations.ConfigCreator;
import com.isnakebuzz.skywars.Configurations.ConfigUtils;
import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Utils.Manager.*;
import com.isnakebuzz.skywars.Listeners.ListenerManager;
import com.isnakebuzz.skywars.Inventory.Inventories;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.skywars.Utils.World.FaweUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    private SkyWarsArena skyWarsArena;
    private ConfigUtils configUtils;
    private DataManager dataManager;
    private PlayerManager playerManager;
    private ScoreBoardAPI scoreBoardAPI;
    private Inventories inventories;
    private ArenaSetup arenaSetup;
    private ListenerManager listenerManager;
    private ChestController chestController;
    private DependManager dependManager;
    private FaweUtils worldRestarting;
    private CagesManager cagesManager;

    public Main() {
        this.cagesManager = new CagesManager(this);
        this.dependManager = new DependManager(this);
        this.chestController = new ChestController(this);
        this.listenerManager = new ListenerManager(this);
        this.arenaSetup = new ArenaSetup(this);
        this.inventories = new Inventories(this);
        this.scoreBoardAPI = new ScoreBoardAPI(this);
        this.dataManager = new DataManager(this);
        this.playerManager = new PlayerManager(this);
        this.configUtils = new ConfigUtils();
    }

    @Override
    public void onEnable() {
        log(Statics.logPrefix, "Initializing SkyWars..");
        this.dependManager.loadDepends();

        //Exporting and loading configuration
        ConfigCreator.get().setup(this, "Settings");
        ConfigCreator.get().setup(this, "Lang");
        ConfigCreator.get().setup(this, "Extra/Database");
        ConfigCreator.get().setup(this, "Extra/ScoreBoards");
        ConfigCreator.get().setup(this, "Extra/Arena");
        ConfigCreator.get().setup(this, "Extra/Inventory");

        Configuration settings = this.getConfig("Settings");
        Configuration database = this.getConfig("Extra/Database");
        Configuration arena = this.getConfig("Extra/Arena");
        Statics.skyMode = settings.getString("Mode").toUpperCase();
        Statics.baseMode = database.getString("Type").toUpperCase();
        Statics.BungeeID = settings.getString("BungeeID", "none");
        Statics.mapName = arena.getString("MapName", "none");

        //Loading database
        this.dataManager.loadDatabase();

        //Load Listeners
        this.getListenerManager().loadInitialsEvents();

        // Create new skywars arena
        this.resetArena();

        //Detecting fawe for instant world restart without lag
        if (Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
            Statics.isFawe = true;
            this.worldRestarting = new FaweUtils(this);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("CCSigns")) {
            Statics.isCCSings = true;

            String playerOnline = String.valueOf(Bukkit.getOnlinePlayers().size());
            String maxPlayer = String.valueOf(this.getSkyWarsArena().getMaxPlayers());

            SignsAPI.sendPacket(PacketType.CREATE, Statics.BungeeID, playerOnline, maxPlayer, GameStates.WAITING, Statics.mapName);
        }

    }

    @Override
    public void onDisable() {
        this.dataManager.getDatabase().closeConnection();
        if (Statics.isCCSings) {
            if (SignsAPI.game != null) {
                SignsAPI.sendPacket(PacketType.REMOVE, Statics.BungeeID);
            }
        }
    }

    public void log(String logger, String log) {
        Bukkit.getConsoleSender().sendMessage(c("&a&l" + logger + " &8|&e " + log));
    }

    public void debug(String log) {
        if (this.getConfig("Settings").getBoolean("debug")) {
            Bukkit.getConsoleSender().sendMessage(c("&a&lDebug &8|&e " + log));
        }
    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(c(message));
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }

    public FileConfiguration getConfig(String configName) {
        return this.getConfigUtils().getConfig(this, configName);
    }

    public SkyWarsArena getSkyWarsArena() {
        return skyWarsArena;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Database getDb() {
        return dataManager.getDatabase();
    }

    public ScoreBoardAPI getScoreBoardAPI() {
        return scoreBoardAPI;
    }

    public ArenaSetup getArenaSetup() {
        return arenaSetup;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public Inventories getInventories() {
        return inventories;
    }

    public FaweUtils getWorldRestarting() {
        return worldRestarting;
    }

    public CagesManager getCagesManager() {
        return cagesManager;
    }

    public ChestController getChestController() {
        return chestController;
    }

    public void resetArena() {
        this.skyWarsArena = new SkyWarsArena(this);
        if (Statics.isCCSings) {
            if (SignsAPI.game != null) {
                SignsAPI.sendPacket(PacketType.REMOVE, Statics.BungeeID);
            }
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
