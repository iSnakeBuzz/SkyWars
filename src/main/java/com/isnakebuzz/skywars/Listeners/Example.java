package com.isnakebuzz.skywars.Listeners;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

public class Example implements Listener {

    private SkyWars plugin;

    public Example(SkyWars plugin) {
        this.plugin = plugin;
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
