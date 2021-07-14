package com.isnakebuzz.skywars.Listeners.Commons;

import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Console;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldEvents implements Listener {

    private SkyWars plugin;

    public WorldEvents(SkyWars plugin) {
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

    @EventHandler
    public void worldLoad(WorldLoadEvent e) {
        Console.debug(String.format("Loading world %s", e.getWorld().getName()));
    }

    @EventHandler
    public void worldUnload(WorldUnloadEvent e) {
        Console.debug(String.format("Unloading world %s", e.getWorld().getName()));
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
