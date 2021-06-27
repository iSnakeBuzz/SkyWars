package com.isnakebuzz.skywars.Listeners.Lobby;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class VoidTP implements Listener {

    private SkyWars plugin;

    public VoidTP(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                e.getEntity().teleport(plugin.getSkyWarsArena().getLobbyLocation());
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
