package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class ExampleTask extends BukkitRunnable {

    private Main plugin;

    public ExampleTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}