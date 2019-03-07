package com.isnakebuzz.skywars.Listeners;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

public class Example implements Listener {

    private Main plugin;

    public Example(Main plugin) {
        this.plugin = plugin;
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
