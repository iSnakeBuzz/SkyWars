package com.isnakebuzz.skywars.Calls.Events;

import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyWinEvent extends Event implements Cancellable {

    //Internal usages
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    //Public usages
    private SkyPlayer skyPlayer;

    public SkyWinEvent(SkyPlayer skyPlayer) {
        this.skyPlayer = skyPlayer;
    }

    public SkyPlayer getSkyPlayer() {
        return skyPlayer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }
}
