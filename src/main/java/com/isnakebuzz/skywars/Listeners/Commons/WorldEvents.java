package com.isnakebuzz.skywars.Listeners.Commons;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;

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
    public void WorldInit(WorldInitEvent e) {
        e.getWorld().setKeepSpawnInMemory(false);
        e.getWorld().setAutoSave(false);
        e.getWorld().getWorldBorder().setSize(10000);
        e.getWorld().setDifficulty(Difficulty.HARD);
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
