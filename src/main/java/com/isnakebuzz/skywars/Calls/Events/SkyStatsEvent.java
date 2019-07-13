package com.isnakebuzz.skywars.Calls.Events;

import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.StatType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyStatsEvent extends Event implements Cancellable {

    //Internal usages
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    //Public usages
    private SkyPlayer skyPlayer;
    private StatType statType;

    public SkyStatsEvent(SkyPlayer skyPlayer, StatType statType) {
        this.skyPlayer = skyPlayer;
        this.statType = statType;
    }

    public SkyPlayer getSkyPlayer() {
        return skyPlayer;
    }

    public StatType getStatType() {
        return statType;
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
        isCancelled = cancelled;
    }
}
