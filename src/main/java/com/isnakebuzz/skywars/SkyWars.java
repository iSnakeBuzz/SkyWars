package com.isnakebuzz.skywars;

import com.isnakebuzz.skywars.Arena.ArenaSetup;
import com.isnakebuzz.skywars.Arena.SkyWarsArena;
import com.isnakebuzz.skywars.Chest.ChestController;
import com.isnakebuzz.skywars.Configurations.ConfigCreator;
import com.isnakebuzz.skywars.Configurations.ConfigUtils;
import com.isnakebuzz.skywars.Database.IDatabase;
import com.isnakebuzz.skywars.Inventory.Inventories;
import com.isnakebuzz.skywars.Kits.KitLoader;
import com.isnakebuzz.skywars.Listeners.ListenerManager;
import com.isnakebuzz.skywars.QueueEvents.EventsManager;
import com.isnakebuzz.skywars.Scheduler.BukkitScheduler;
import com.isnakebuzz.skywars.Scheduler.IScheduler;
import com.isnakebuzz.skywars.Teams.TeamManager;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.Manager.*;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.skywars.Utils.Utils;
import com.isnakebuzz.skywars.Utils.World.FaweUtils;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static com.isnakebuzz.skywars.Utils.Statics.c;

@Getter
public final class SkyWars extends JavaPlugin {

    private final ConfigUtils configUtils;
    private final DataManager dataManager;
    private final ScoreBoardAPI scoreBoardAPI;
    private final ListenerManager listenerManager;
    private final ChestController chestController;
    private final DependManager dependManager;
    private final CagesManager cagesManager;
    private final TimerManager timerManager;
    private final Inventories inventories;
    private final ArenaSetup arenaSetup;
    private final IScheduler scheduler;
    private final KitLoader kitLoader;
    private final Utils utils;
    private SkyWarsArena skyWarsArena;
    private ChestRefillManager chestRefillManager;
    private EventsManager eventsManager;
    private PlayerManager playerManager;
    private FaweUtils worldRestarting;
    private VoteManager voteManager;
    private TeamManager teamManager;

    public SkyWars() {
        this.scheduler = new BukkitScheduler(this);
        this.teamManager = new TeamManager(this);
        this.utils = new Utils(this);
        this.kitLoader = new KitLoader(this);
        this.chestRefillManager = new ChestRefillManager(this);
        this.timerManager = new TimerManager(this);
        this.voteManager = new VoteManager(this);
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
        this.eventsManager = new EventsManager(this);
    }

    @Override
    public void onEnable() {
        log(Statics.logPrefix, "Initializing SkyWars..");
        this.dependManager.loadDepends();

        //Register bungeecord channels
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        //Exporting and loading configuration
        ConfigCreator.get().setup(this, "Settings");
        ConfigCreator.get().setup(this, "Lang");
        ConfigCreator.get().setup(this, "Extra/Database");
        ConfigCreator.get().setup(this, "Extra/ScoreBoards");
        ConfigCreator.get().setup(this, "Extra/Arena");
        ConfigCreator.get().setup(this, "Extra/Inventory");
        ConfigCreator.get().setup(this, "Extra/MenuCreator");
        ConfigCreator.get().setup(this, "Extra/QueuedEvents");

        Configuration settings = this.getConfig("Settings");
        Configuration database = this.getConfig("Extra/Database");
        Configuration arena = this.getConfig("Extra/Arena");
        Statics.skyMode = GameType.valueOf(settings.getString("Mode").toUpperCase());
        Statics.baseMode = database.getString("Type", "mysql").toUpperCase();
        Statics.BungeeID = settings.getString("BungeeID", Bukkit.getServerName());
        Statics.lobbies = settings.getStringList("Lobbies");
        Statics.mapName = arena.getString("MapName", "none");
        Statics.API_URL = arena.getString("SnakeDb", "http://localhost:3000/games/");

        this.debug("Settings Mode: " + settings.getString("Mode").toUpperCase());

        //Loading database
        this.dataManager.loadDatabase();

        //Detecting fawe for instant world restart without lag
        if (Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit") && Statics.skyMode != GameType.LOBBY) {
            Statics.isFawe = true;
            this.worldRestarting = new FaweUtils(this);
        }

        //Load Listeners
        this.getListenerManager().loadInitialsEvents();

        //Loading kits
        this.getKitLoader().loadKits();
    }

    @Override
    public void onDisable() {
        this.dataManager.getDatabase().closeConnection();
        if (Statics.SnakeGameQueue) {
            GameQueueAPI.removeGame(Statics.BungeeID);
        }

        if (Bukkit.getOnlinePlayers().size() > 0)
            Bukkit.getOnlinePlayers().forEach(o -> o.kickPlayer("Reloading SkyWars Plugin. " + ChatColor.DARK_RED + "(DON'T USE RELOAD)"));
    }

    /**
     * Log message into the console
     *
     * @param logger prefix
     * @param log    message
     */
    @Deprecated
    public void log(String logger, String log) {
        Bukkit.getConsoleSender().sendMessage(c("&a&l" + logger + " &8|&e " + log));
    }

    /**
     * Log message into the console
     *
     * @param log message
     */
    @Deprecated
    public void debug(String log) {
        if (this.getConfig("Settings").getBoolean("debug")) {
            Bukkit.getConsoleSender().sendMessage(c("&a&lSkyWars-Debug &8|&e " + log));
        }
    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(c(message));
    }

    public void broadcast(BaseComponent... baseComponent) {
        Bukkit.spigot().broadcast(baseComponent);
    }

    public void closeInventory() {
        for (Player online : Bukkit.getOnlinePlayers()) online.closeInventory();
    }


    public FileConfiguration getConfig(String configName) {
        return this.getConfigUtils().getConfig(this, configName);
    }


    public IDatabase getDb() {
        return dataManager.getDatabase();
    }

    public ScoreBoardAPI getScoreBoardAPI2() {
        return scoreBoardAPI;
    }

    public synchronized void resetArena() {
        this.getChestRefillManager().reset();
        this.getPlayerManager().reset();
        this.skyWarsArena = new SkyWarsArena(this);
        this.voteManager = new VoteManager(this);
        this.chestRefillManager = new ChestRefillManager(this);
        this.teamManager = new TeamManager(this);
        this.playerManager = new PlayerManager(this);
        this.eventsManager = new EventsManager(this);
        this.teamManager.loadTeams();
        this.getEventsManager().loadQueueEvents();

        if (Bukkit.getOnlinePlayers().size() >= 1) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                getSkyWarsArena().SEND_TO_NEW_GAME(onlinePlayer);
            }
        }
    }

}
