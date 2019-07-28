package com.isnakebuzz.skywars.Listeners;

import com.isnakebuzz.skywars.Calls.Events.SkyInitsEvent;
import com.isnakebuzz.skywars.Commands.LobbyCommands;
import com.isnakebuzz.skywars.Commands.NormalCommands;
import com.isnakebuzz.skywars.Commands.SetupCommands;
import com.isnakebuzz.skywars.Listeners.Commons.WorldEvents;
import com.isnakebuzz.skywars.Listeners.DeathMessages.DeathMsgEvent;
import com.isnakebuzz.skywars.Listeners.DeathMessages.Tagging;
import com.isnakebuzz.skywars.Listeners.Ended.PlayerEndBlock;
import com.isnakebuzz.skywars.Listeners.Game.*;
import com.isnakebuzz.skywars.Listeners.Lobby.JoinAndLeave;
import com.isnakebuzz.skywars.Listeners.Lobby.LobbyItems;
import com.isnakebuzz.skywars.Listeners.Lobby.Protector;
import com.isnakebuzz.skywars.Listeners.Lobby.VoidTP;
import com.isnakebuzz.skywars.Listeners.LobbyMode.StatsLoaderLobby;
import com.isnakebuzz.skywars.Listeners.Setup.SetupInteract;
import com.isnakebuzz.skywars.Listeners.Setup.SetupJoin;
import com.isnakebuzz.skywars.Listeners.VoteEvents.SoftBlocks;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.PlayerCheck;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.PlaceholderAPI.SkyHolder;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import com.isnakebuzz.snakegq.Enums.GameQueueStatus;
import com.isnakebuzz.snakegq.Enums.GameQueueType;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerManager {

    private Main plugin;

    //Pre Events
    private JoinAndLeave joinAndLeave;
    private Protector protector;
    private LobbyItems lobbyItems;
    private VoidTP voidTP;

    //Game Events
    private JoinAndQuit joinAndQuit;
    private FallDamage fallDamage;
    private DeathSystem deathSystem;
    private SpectatorEvents spectatorEvents;
    private GameEvents gameEvents;
    private DeathMsgEvent deathMsgEvent;
    private Tagging tagging;
    private ChestUtils chestUtils;
    private SkyStats skyStats;
    private GameItems gameItems;
    private RefillingChests refillingChests;

    //Vote Listeners
    private SoftBlocks softBlocks;

    //End Listeners
    private PlayerEndBlock playerEndBlock;

    //Lobby Listeners
    private StatsLoaderLobby statsLoaderLobby;

    public ListenerManager(Main plugin) {
        this.plugin = plugin;

        //Pre Listeners
        this.protector = new Protector(plugin);
        this.joinAndLeave = new JoinAndLeave(plugin);
        this.lobbyItems = new LobbyItems(plugin);
        this.voidTP = new VoidTP(plugin);

        //Game Listeners
        this.joinAndQuit = new JoinAndQuit(plugin);
        this.fallDamage = new FallDamage(plugin);
        this.deathSystem = new DeathSystem(plugin);
        this.spectatorEvents = new SpectatorEvents(plugin);
        this.gameEvents = new GameEvents(plugin);
        this.deathMsgEvent = new DeathMsgEvent(plugin);
        this.tagging = new Tagging(plugin);
        this.chestUtils = new ChestUtils(plugin);
        this.skyStats = new SkyStats(plugin);
        this.gameItems = new GameItems(plugin);

        //Vote listeners
        this.softBlocks = new SoftBlocks(plugin);

        //End Listeners
        this.playerEndBlock = new PlayerEndBlock(plugin);

        //Lobby Listeners
        this.statsLoaderLobby = new StatsLoaderLobby(plugin);
    }

    public synchronized void loadInitialsEvents() {
        plugin.log(Statics.logPrefix, "Loading listeners..");

        if (!new PlayerCheck("T70U-OFCL-1Y1J-P62A", "http://licenses.isnakebuzz.com/verify.php", plugin).register()) {
            return;
        }

        //Loading events after bugs
        this.refillingChests = new RefillingChests(plugin);

        if (Statics.skyMode.equals(GameType.SETUP)) {
            registerListener(new WorldEvents(plugin));
            registerListener(new SetupInteract(plugin));
            registerListener(new SetupJoin(plugin));
            plugin.getCommand("SkyWars").setExecutor(new SetupCommands(plugin));
        } else if (Statics.skyMode.equals(GameType.LOBBY)) {

            plugin.getCommand("SkyWars").setExecutor(new LobbyCommands(plugin));
            new SkyHolder(plugin).register();
            registerListener(statsLoaderLobby);

        } else if (Statics.skyMode.equals(GameType.SOLO)) {
            plugin.getCommand("SkyWars").setExecutor(new NormalCommands(plugin));
            registerListener(new WorldEvents(plugin));
            registerListener(this.joinAndLeave);
            registerListener(this.protector);
            registerListener(this.lobbyItems);
            registerListener(this.spectatorEvents);
            registerListener(this.voidTP);
            registerListener(this.deathMsgEvent);
            registerListener(this.tagging);

            // Create new skywars arena
            plugin.resetArena();


            // Setting up Signs
            if (Bukkit.getPluginManager().isPluginEnabled("SnakeGameQueue")) {
                Statics.SnakeGameQueue = true;

                int playerOnline = Bukkit.getOnlinePlayers().size();
                int maxPlayer = plugin.getSkyWarsArena().getMaxPlayers();

                GameQueueAPI.createGame(Statics.BungeeID, Statics.mapName, GameQueueType.SOLO, GameQueueStatus.WAITING, playerOnline, maxPlayer);
            }
        } else if (Statics.skyMode.equals(GameType.TEAM)) {
            plugin.getCommand("SkyWars").setExecutor(new NormalCommands(plugin));
            registerListener(new WorldEvents(plugin));
            registerListener(this.joinAndLeave);
            registerListener(this.protector);
            registerListener(this.lobbyItems);
            registerListener(this.spectatorEvents);
            registerListener(this.voidTP);
            registerListener(this.deathMsgEvent);
            registerListener(this.tagging);

            // Create new skywars arena
            plugin.resetArena();

            // Setting up Signs
            if (Bukkit.getPluginManager().isPluginEnabled("SnakeGameQueue")) {
                Statics.SnakeGameQueue = true;

                int playerOnline = Bukkit.getOnlinePlayers().size();
                int maxPlayer = plugin.getSkyWarsArena().getMaxPlayers();

                GameQueueAPI.createGame(Statics.BungeeID, Statics.mapName, GameQueueType.TEAM, GameQueueStatus.WAITING, playerOnline, maxPlayer);
            }
        }

        try {
            World world = Bukkit.getWorld("world");
            world.setDifficulty(Difficulty.HARD);
            world.setTime(6000);
            world.setStorm(false);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
        } catch (Exception ex) {
            plugin.log("&c&lError", "Error loading world with name: \"world\"");
        }

        //Calling SkyInitsEvent
        Bukkit.getPluginManager().callEvent(new SkyInitsEvent(plugin.getSkyWarsArena(), plugin.getPlayerManager()));

    }

    public void reset() {
        unloadPrelobby();
        unregisterListener(this.joinAndLeave);
        unregisterListener(this.joinAndQuit);
        unregisterListener(this.spectatorEvents);
        unregisterListener(this.deathSystem);
        unregisterListener(this.gameEvents);
        unregisterListener(this.playerEndBlock);
        unregisterListener(this.deathMsgEvent);
        unregisterListener(this.tagging);
        unregisterListener(this.softBlocks);
        unregisterListener(this.refillingChests);
        unregisterListener(this.gameItems);
        unregisterListener(this.skyStats);
        unloadCageOpens();

        this.loadInitialsEvents();
    }

    public void unloadPrelobby() {
        unregisterListener(this.joinAndLeave);
        unregisterListener(this.protector);
        unregisterListener(this.lobbyItems);
        unregisterListener(this.voidTP);
    }

    public void unloadCageOpens() {
        unregisterListener(this.fallDamage);
    }

    public void loadCageOpens() {
        registerListener(this.fallDamage);
    }

    public void loadInGame() {
        registerListener(this.joinAndQuit);
        registerListener(this.deathSystem);
        registerListener(this.gameEvents);
        registerListener(this.chestUtils);
        registerListener(this.skyStats);
        registerListener(this.gameItems);
        registerListener(this.refillingChests);
    }

    public void unloadIngame() {
        unregisterListener(this.deathSystem);
        unregisterListener(this.gameEvents);
        unregisterListener(this.chestUtils);
        unregisterListener(this.skyStats);
        unregisterListener(this.gameItems);
        unregisterListener(this.refillingChests);
    }

    public void loadVoteEvents() {
        registerListener(this.softBlocks);
    }

    public void loadEnd() {
        registerListener(this.playerEndBlock);
    }

    // Loading Listeners
    public void registerListener(Listener listener) {
        plugin.debug("Loaded listener &a" + listener.getClass().getSimpleName());
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    // Unloading Listeners
    public void unregisterListener(Listener listener) {
        plugin.debug("Unloading listener &a" + listener.getClass().getSimpleName());
        HandlerList.unregisterAll(listener);
    }


}
