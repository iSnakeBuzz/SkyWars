package com.isnakebuzz.skywars;

import com.isnakebuzz.skywars.Arena.ArenaSetup;
import com.isnakebuzz.skywars.Arena.SkyWarsArena;
import com.isnakebuzz.skywars.Chest.ChestController;
import com.isnakebuzz.skywars.Commands.Cmds;
import com.isnakebuzz.skywars.Configurations.ConfigCreator;
import com.isnakebuzz.skywars.Configurations.ConfigUtils;
import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Events.EventsManager;
import com.isnakebuzz.skywars.Inventory.Inventories;
import com.isnakebuzz.skywars.Utils.Manager.DataManager;
import com.isnakebuzz.skywars.Utils.Manager.PlayerManager;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private SkyWarsArena skyWarsArena;
    private ConfigUtils configUtils;
    private DataManager dataManager;
    private PlayerManager playerManager;
    private ScoreBoardAPI scoreBoardAPI;
    private Inventories inventories;
    private ArenaSetup arenaSetup;
    private EventsManager eventsManager;
    private ChestController chestController;

    public Main() {
        this.chestController = new ChestController(this);
        this.eventsManager = new EventsManager(this);
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
        ConfigCreator.get().setup(this, "Settings");
        ConfigCreator.get().setup(this, "Lang");
        ConfigCreator.get().setup(this, "Extra/Database");
        ConfigCreator.get().setup(this, "Extra/ScoreBoards");
        ConfigCreator.get().setup(this, "Extra/Arena");
        ConfigCreator.get().setup(this, "Extra/Inventory");

        Configuration settings = this.getConfig("Settings");
        Configuration database = this.getConfig("Extra/Database");
        Statics.skyMode = settings.getString("Mode").toUpperCase();
        Statics.baseMode = database.getString("Type").toUpperCase();

        this.dataManager.loadDatabase();

        //Load commands
        this.getCommand("skywars").setExecutor(new Cmds(this));

        //Load Events
        this.getEventsManager().loadInitialsEvents();

        // Create new skywars arena
        try {
            this.skyWarsArena = new SkyWarsArena(this);
        } catch (Exception ex) {
        }

    }

    @Override
    public void onDisable() {
        this.dataManager.getDatabase().closeConnection();
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

    public EventsManager getEventsManager() {
        return eventsManager;
    }

    public Inventories getInventories() {
        return inventories;
    }

    public ChestController getChestController() {
        return chestController;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
