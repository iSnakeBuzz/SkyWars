package com.isnakebuzz.skywars.Events.Setup;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SetupJoin implements Listener {

    private Main plugin;

    public SetupJoin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerJOin(PlayerJoinEvent e) {
        e.getPlayer().setAllowFlight(true);
        e.getPlayer().setFlying(true);
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        e.getPlayer().setGameMode(GameMode.CREATIVE);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
