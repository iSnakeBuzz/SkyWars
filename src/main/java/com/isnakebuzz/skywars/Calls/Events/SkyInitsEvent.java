package com.isnakebuzz.skywars.Calls.Events;

import com.isnakebuzz.skywars.Arena.SkyWarsArena;
import com.isnakebuzz.skywars.Utils.Manager.PlayerManager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyInitsEvent extends Event {

    //Internal usages
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    //Public usages
    private SkyWarsArena game;
    private PlayerManager playerManager;

    public SkyInitsEvent(SkyWarsArena game, PlayerManager playerManager) {
        this.game = game;
        this.playerManager = playerManager;
    }

    public SkyWarsArena getGame() {
        return game;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
