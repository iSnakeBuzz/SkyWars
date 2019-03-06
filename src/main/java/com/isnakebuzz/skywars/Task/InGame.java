package com.isnakebuzz.skywars.Task;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;

public class InGame implements Runnable {

    private Main plugin;

    public InGame(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}