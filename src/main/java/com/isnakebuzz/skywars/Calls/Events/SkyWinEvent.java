package com.isnakebuzz.skywars.Calls.Events;

import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Teams.Team;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyWinEvent extends Event implements Cancellable {

    //Internal usages
    private static HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    //Public usages
    private Team team;

    public SkyWinEvent(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
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
