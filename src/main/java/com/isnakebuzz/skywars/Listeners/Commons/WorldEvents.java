package com.isnakebuzz.skywars.Listeners.Commons;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
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

    @EventHandler
    public void PlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        ArmorStand armorStand = e.getRightClicked();
        if (!armorStand.isVisible() && !armorStand.hasBasePlate()) {
            e.setCancelled(true);
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
