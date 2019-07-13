package com.isnakebuzz.skywars.Calls.Events;

import com.isnakebuzz.skywars.QueueEvents.QueueEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkyQueueUpdateEvent extends Event {

    //Internal usages
    private static final HandlerList handlers = new HandlerList();

    //Public usages
    private QueueEvent queueEvent;
    private Boolean isFinal;

    public SkyQueueUpdateEvent(QueueEvent queueEvent, Boolean isFinal) {
        super(true);
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

}
