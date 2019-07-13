package com.isnakebuzz.skywars.QueueEvents;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.QueueEvents.enums.QueueType;
import com.isnakebuzz.skywars.Tasks.EventsQueue;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

public class EventsManager {

    private Main plugin;
    private List<QueueEvent> queue;

    //Events common settings
    private QueueEvent actualQueue = null;
    private Boolean isActived = true;

    public EventsManager(Main plugin) {
        this.plugin = plugin;
        this.queue = Lists.newArrayList();
    }

    public void loadQueueEvents() {
        if (!getConfig().getBoolean("Enabled")) return;

        Set<String> keys = getConfig().getConfigurationSection("Events").getKeys(false);

        for (String eventTag : keys) {
            ConfigurationSection eventConfig = getConfig(eventTag);

            Integer eventTime = eventConfig.getInt("time");

            String name = eventConfig.getString("event settings.name");
            String type = eventConfig.getString("event settings.type").toUpperCase();
            String placeholder = eventConfig.getString("placeholder");

            QueueEvent queueEvent = new QueueEvent(name, eventTime, QueueType.valueOf(type), placeholder);
            plugin.debug("Adding new event to queue: " + eventTag);
            this.queue.add(queueEvent);
        }
    }

    public void execute() {
        if (!getConfig().getBoolean("Enabled")) return;
        plugin.debug("Executing Queue task");
        new EventsQueue(plugin).runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public boolean isActived() {
        return isActived;
    }

    public void finish() {
        isActived = false;
    }

    public void setActualQueue(QueueEvent actualQueue) {
        this.actualQueue = actualQueue;
    }


    public QueueEvent getActualQueue() {
        return actualQueue;
    }

    public ConfigurationSection getConfig(String queueName) {
        return this.getConfig().getConfigurationSection("Events." + queueName);
    }

    public Configuration getConfig() {
        return this.plugin.getConfig("Extra/QueuedEvents");
    }

    public List<QueueEvent> getQueue() {
        return queue;
    }
}
