package com.isnakebuzz.skywars.Listeners.Lobby;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Protector implements Listener {

    private SkyWars plugin;

    public Protector(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void dropItems(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void PlayerBreakEvent(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void PlayerPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void PlayerDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void FoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
