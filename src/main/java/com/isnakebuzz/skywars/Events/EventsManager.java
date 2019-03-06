package com.isnakebuzz.skywars.Events;

import com.isnakebuzz.skywars.Events.Commons.WorldEvents;
import com.isnakebuzz.skywars.Events.Game.*;
import com.isnakebuzz.skywars.Events.Lobby.JoinAndLeave;
import com.isnakebuzz.skywars.Events.Lobby.LobbyItems;
import com.isnakebuzz.skywars.Events.Lobby.Protector;
import com.isnakebuzz.skywars.Events.Setup.SetupInteract;
import com.isnakebuzz.skywars.Events.Setup.SetupJoin;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class EventsManager {

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

    public EventsManager(Main plugin) {
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
        } else if (Statics.skyMode.equalsIgnoreCase("LOBBY")) {
        } else if (Statics.skyMode.equalsIgnoreCase("SOLO")) {
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
