package com.isnakebuzz.skywars.Calls.Events;

import com.isnakebuzz.skywars.QueueEvents.QueueEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyQueueStartEvent extends Event implements Cancellable {

    //Internal usages
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    //Public usages
    private QueueEvent queueEvent;
    private Boolean isFinal;

    public SkyQueueStartEvent(QueueEvent queueEvent, Boolean isFinal, boolean isAsync) {
        super(isAsync);
        this.queueEvent = queueEvent;
        this.isFinal = isFinal;
    }

    public QueueEvent getQueueEvent() {
        return queueEvent;
    }

    public boolean isFinal() {
        return isFinal;
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
