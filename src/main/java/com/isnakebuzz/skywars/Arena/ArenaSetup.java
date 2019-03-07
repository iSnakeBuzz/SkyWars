package com.isnakebuzz.skywars.Arena;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.LocUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ArenaSetup {

    private Main plugin;

    public ArenaSetup(Main plugin) {
        this.plugin = plugin;
    }

    public void setLobbyAndSpect(Player p) throws IOException {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");
        config.set("Lobby", LocUtils.locToString(p.getLocation()));
        config.save(plugin.getConfigUtils().getFile(plugin, "Extra/Arena"));
        p.sendMessage(c("&aYou has been setted Lobby"));
        p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
    }

    public void setCenterSchematic(Player p) throws IOException {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");
        config.set("Schematic Center", LocUtils.locToString(p.getLocation()));
        config.save(plugin.getConfigUtils().getFile(plugin, "Extra/Arena"));
        p.sendMessage(c("&aYou has been setted Center for schematic"));
        p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
    }

    public void addSpawn(Player p) throws IOException {
        FileConfiguration arenaConfig = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");

        int spawn;
        try {
            spawn = arenaConfig.getConfigurationSection("Spawns").getKeys(false).size() + 1;
        } catch (Exception ex) {
            spawn = 1;
        }

        arenaConfig.set("Spawns." + spawn, LocUtils.locToString(p.getLocation()));
        arenaConfig.save(plugin.getConfigUtils().getFile(plugin, "Extra/Arena"));
        p.sendMessage(c("&aHas been added  Spawn #" + spawn));
        p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
    }

    public void removeSpawn(Player p) throws IOException {
        FileConfiguration arenaConfig = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");

        int spawn;
        try {
            spawn = arenaConfig.getConfigurationSection("Spawns").getKeys(false).size();
        } catch (Exception ex) {
            spawn = 1;
        }

        arenaConfig.set("Spawns." + spawn, null);
        arenaConfig.save(plugin.getConfigUtils().getFile(plugin, "Extra/Arena"));
        p.sendMessage(c("&aHas been removed  Spawn #" + spawn));
        p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
    }

    public void setSettings(Player p, String path, Object value) throws IOException {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");
        config.set(path, value);
        config.save(plugin.getConfigUtils().getFile(plugin, "Extra/Arena"));
        p.sendMessage(c("&aYou has been setted Path: " + path + ", Value: " + value));
        p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
    }

    public Object getObject(String path, Object defaultValue) {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");
        return config.get(path, defaultValue);
    }


    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}