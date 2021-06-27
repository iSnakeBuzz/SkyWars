package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class ExampleTask extends BukkitRunnable {

    private SkyWars plugin;

    public ExampleTask(SkyWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}