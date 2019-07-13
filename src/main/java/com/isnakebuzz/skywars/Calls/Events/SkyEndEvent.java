package com.isnakebuzz.skywars.Calls.Events;

import com.isnakebuzz.skywars.Teams.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyEndEvent extends Event {

    //Internal usages
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    //Public usages
    private Team team;

    public SkyEndEvent(Team team) {
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

}
