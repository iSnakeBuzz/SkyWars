package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;

public class ExampleTask implements Runnable {

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