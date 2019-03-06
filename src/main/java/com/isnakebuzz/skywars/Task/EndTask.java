package com.isnakebuzz.skywars.Task;

import com.isnakebuzz.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class EndTask implements Runnable {

    private Main plugin;

    public EndTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        if (plugin.getSkyWarsArena().getEndTimer() <= 1) {
            //Send to lobby :)
        }

        if (plugin.getSkyWarsArena().getEndTimer() <= 0) {
            Bukkit.shutdown();
            Bukkit.getScheduler().cancelTask(plugin.getSkyWarsArena().getEndTask());
        }

        plugin.getSkyWarsArena().setEndTimer(plugin.getSkyWarsArena().getEndTimer()-1);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}