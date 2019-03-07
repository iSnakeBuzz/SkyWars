package com.isnakebuzz.skywars.Listeners;

import com.isnakebuzz.skywars.Commands.NormalCommands;
import com.isnakebuzz.skywars.Commands.SetupCommands;
import com.isnakebuzz.skywars.Listeners.Commons.WorldEvents;
import com.isnakebuzz.skywars.Listeners.Game.*;
import com.isnakebuzz.skywars.Listeners.Lobby.JoinAndLeave;
import com.isnakebuzz.skywars.Listeners.Lobby.LobbyItems;
import com.isnakebuzz.skywars.Listeners.Lobby.Protector;
import com.isnakebuzz.skywars.Listeners.Setup.SetupInteract;
import com.isnakebuzz.skywars.Listeners.Setup.SetupJoin;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Statics;
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

    //Game Events
    private JoinAndQuit joinAndQuit;
    private FallDamage fallDamage;
    private DeathSystem deathSystem;
    private SpectatorEvents spectatorEvents;
    private GameEvents gameEvents;

    public ListenerManager(Main plugin) {
        this.plugin = plugin;

        //Pre events
        this.protector = new Protector(plugin);
        this.joinAndLeave = new JoinAndLeave(plugin);
        this.lobbyItems = new LobbyItems(plugin);

        //Game Events
        this.joinAndQuit = new JoinAndQuit(plugin);
        this.fallDamage = new FallDamage(plugin);
        this.deathSystem = new DeathSystem(plugin);
        this.spectatorEvents = new SpectatorEvents(plugin);
        this.gameEvents = new GameEvents(plugin);
    }

    public void loadInitialsEvents() {
        plugin.log(Statics.logPrefix, "Loading listeners..");

        if (Statics.skyMode.equalsIgnoreCase("SETUP")) {
            registerListener(new WorldEvents(plugin));
            registerListener(new SetupInteract(plugin));
            registerListener(new SetupJoin(plugin));

            //Loading chests
            plugin.getChestController().load();
            plugin.getCommand("SkyWars").setExecutor(new SetupCommands(plugin));
        } else if (Statics.skyMode.equalsIgnoreCase("LOBBY")) {
        } else if (Statics.skyMode.equalsIgnoreCase("SOLO")) {
            plugin.getCommand("SkyWars").setExecutor(new NormalCommands(plugin));
            registerListener(new WorldEvents(plugin));
            registerListener(this.joinAndLeave);
            registerListener(this.protector);
            registerListener(this.lobbyItems);
            registerListener(this.spectatorEvents);

            //Loading chests
            plugin.getChestController().load();
        } else if (Statics.skyMode.equalsIgnoreCase("TEAM")) {
            registerListener(new WorldEvents(plugin));
            registerListener(this.joinAndLeave);
            registerListener(this.protector);
            registerListener(this.lobbyItems);
            registerListener(this.spectatorEvents);

            //Loading chests
            plugin.getChestController().load();
        }

        try {
            World world = Bukkit.getWorld("world");
            world.setDifficulty(Difficulty.HARD);
            world.setTime(6000);
            world.setStorm(false);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
        } catch (Exception ex) {
            plugin.debug("Error loading world with name: \"world\"");
        }
    }

    public void reset() {
        unloadPrelobby();
        unregisterListener(this.joinAndLeave);
        unregisterListener(this.joinAndQuit);
        unregisterListener(this.spectatorEvents);
        unregisterListener(this.deathSystem);
        unregisterListener(this.gameEvents);
        unloadCageOpens();

        this.loadInitialsEvents();
    }

    public void unloadPrelobby() {
        unregisterListener(this.joinAndLeave);
        unregisterListener(this.protector);
        unregisterListener(this.lobbyItems);
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
    }

    public void unloadIngame() {
        unregisterListener(this.deathSystem);
        unregisterListener(this.gameEvents);
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
