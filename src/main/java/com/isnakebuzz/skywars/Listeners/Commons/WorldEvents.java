package com.isnakebuzz.skywars.Listeners.Commons;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldEvents implements Listener {

    private Main plugin;

    public WorldEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void WorldWeather(WeatherChangeEvent e) {
        if (!e.getWorld().hasStorm()) {
            e.setCancelled(true);
        } else {
            e.getWorld().setStorm(false);
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
