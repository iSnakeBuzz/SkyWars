package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Calls.Events.SkyQueueFinishEvent;
import com.isnakebuzz.skywars.Calls.Events.SkyQueueStartEvent;
import com.isnakebuzz.skywars.Calls.Events.SkyQueueUpdateEvent;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.QueueEvents.enums.QueueType;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RefillingChests implements Listener {

    private Main plugin;
    private String changeTo;

    public RefillingChests(Main plugin) {
        this.plugin = plugin;


        //Refill Utils
        ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Holograms.ChestRefill");
        changeTo = c(lang.getString("Name"));
    }

    @EventHandler
    public void SkyQueueStart(SkyQueueStartEvent e) {
        if (e.getQueueEvent().getQueueType().equals(QueueType.REFILL)) {
            // Activating refill system
            plugin.getChestRefillManager().setActived(true);
        }
    }

    @EventHandler
    public void SkyQueueUpdate(SkyQueueUpdateEvent e) {
        if (e.getQueueEvent().getQueueType().equals(QueueType.REFILL)) {
            String timer = plugin.getTimerManager().transformToDate(e.getQueueEvent().getEventTime());
            plugin.getChestRefillManager().update(changeTo.replaceAll("%timer%", timer));
        }
    }


    @EventHandler
    public void SkyQueueFinish(SkyQueueFinishEvent e) {
        if (e.getQueueEvent().getQueueType().equals(QueueType.REFILL)) {
            plugin.getSkyWarsArena().fillChests();
            plugin.getChestRefillManager().reset();

            Configuration lang = plugin.getConfig("Lang");

            PacketsAPI.broadcastTitle(plugin,
                    lang.getString("Chest Refilled.Title"),
                    lang.getString("Chest Refilled.SubTitle"),
                    lang.getInt("Chest Refilled.FadeIn"),
                    lang.getInt("Chest Refilled.Stay"),
                    lang.getInt("Chest Refilled.FadeOut")
            );

            // Desactivating refill system
            plugin.getChestRefillManager().setActived(false);
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
