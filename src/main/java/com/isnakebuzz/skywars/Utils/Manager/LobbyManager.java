package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.LocUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class LobbyManager {

    private SkyWars plugin;

    public LobbyManager(SkyWars plugin) {
        this.plugin = plugin;
    }

    public void removeLobby(Player p) throws IOException {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");
        config.set("Lobby", null);
        config.save(plugin.getConfigUtils().getFile(plugin, "Extra/Arena"));
        p.sendMessage(c("&aYou has been removed Lobby"));
        p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 0);
    }

    public Location getLobby() {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");
        return LocUtils.stringToLoc(config.getString("Lobby")).getLocation();
    }

    public void setLobby(Player p) throws IOException {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Arena");
        config.set("Lobby", LocUtils.locToString(p.getLocation()));
        config.save(plugin.getConfigUtils().getFile(plugin, "Extra/Arena"));
        p.sendMessage(c("&aYou has been setted Lobby"));
        p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
