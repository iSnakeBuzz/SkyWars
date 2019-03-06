package com.isnakebuzz.skywars.Events.Game;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallDamage implements Listener {

    private Main plugin;

    public FallDamage(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void FallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                e.setCancelled(true);
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
