package com.isnakebuzz.skywars.Calls.Events;

import com.isnakebuzz.skywars.Arena.SkyWarsArena;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyStartEvent extends Event {

    //Internal usages
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    //Public usages
    private SkyWarsArena game;

    public SkyStartEvent(SkyWarsArena game) {
        this.game = game;
    }

    public SkyWarsArena getGame() {
        return game;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
