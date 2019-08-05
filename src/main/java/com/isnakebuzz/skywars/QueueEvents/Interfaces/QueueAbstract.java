package com.isnakebuzz.skywars.QueueEvents.Interfaces;

import com.isnakebuzz.skywars.Calls.Events.SkyQueueStartEvent;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.QueueEvents.QueueEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class QueueAbstract extends BukkitRunnable {

    private Main plugin;

    // Ingame addons
    private Integer queueInt = 0;
    private Integer queueMax;
    private QueueEvent inQueue;

    public QueueAbstract(Main plugin) {
        this.plugin = plugin;

        this.queueMax = plugin.getEventsManager().getQueue().size();
        this.inQueue = plugin.getEventsManager().getQueue().get(queueInt);
        this.plugin.getEventsManager().setActualQueue(this.inQueue);

        //Calling skyQueueStartEvent
        SkyQueueStartEvent skyQueueStartEvent = new SkyQueueStartEvent(this.inQueue, this.isFinal(), false);
        Bukkit.getPluginManager().callEvent(skyQueueStartEvent);
        if (skyQueueStartEvent.isCancelled()) {
            next();
        }
    }

    protected void updateTime(Integer newTime) {
        this.inQueue.setEventTime(newTime);
    }

    public Integer getTime() {
        return this.inQueue.getEventTime();
    }

    public boolean isFinal() {
        plugin.debug("Queue Next: " + (queueInt >= (queueMax - 1)) + " | " + queueInt + " : " + (queueMax - 1));

        return queueInt >= (queueMax - 1);
    }

    public void next() {
        plugin.debug("Queue Next: " + (queueInt < (queueMax - 1)) + " | " + queueInt + " : " + (queueMax - 1));

        if (queueInt < (queueMax - 1)) {
            queueInt++;

            //Setting up inQueue
            this.inQueue = plugin.getEventsManager().getQueue().get(queueInt);
            this.plugin.getEventsManager().setActualQueue(this.inQueue);

            //Calling skyQueueStartEvent
            SkyQueueStartEvent skyQueueStartEvent = new SkyQueueStartEvent(this.inQueue, this.isFinal(), true);
            Bukkit.getPluginManager().callEvent(skyQueueStartEvent);
            if (skyQueueStartEvent.isCancelled()) {
                next();
            }
        }
    }

    protected QueueEvent getInQueue() {
        return inQueue;
    }

    @Override
    public void run() {
    }


}
