package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Calls.Events.SkyQueueFinishEvent;
import com.isnakebuzz.skywars.Calls.Events.SkyQueueUpdateEvent;
import com.isnakebuzz.skywars.Listeners.DeathMessages.Tagging;
import com.isnakebuzz.skywars.QueueEvents.Interfaces.QueueAbstract;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class EventsQueue extends QueueAbstract {

    private SkyWars plugin;

    public EventsQueue(SkyWars plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.INGAME)) {
            Tagging.increaseTimers();
        } else {
            this.cancel();
            return;
        }

        if (this.getTime() > 0) {
            Bukkit.getPluginManager().callEvent(new SkyQueueUpdateEvent(this.getInQueue(), this.isFinal()));
        }


        if (this.getTime() == 0) {
            SkyQueueFinishEvent skyQueueFinishEvent = new SkyQueueFinishEvent(this.getInQueue(), this.isFinal());
            Bukkit.getPluginManager().callEvent(skyQueueFinishEvent);

            if (!this.isFinal()) {
                this.next();
            } else {
                plugin.getEventsManager().finish();
                this.cancel();
            }
        }


        // Updating time
        this.updateTime(this.getTime() - 1);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}