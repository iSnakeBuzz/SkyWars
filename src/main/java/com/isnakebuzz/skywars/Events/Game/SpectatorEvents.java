package com.isnakebuzz.skywars.Events.Game;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class SpectatorEvents implements Listener {

    private Main plugin;

    public SpectatorEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer((Player) e.getEntity());
            if (skyPlayer.isSpectator()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void EntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer((Player) e.getDamager());
            if (skyPlayer.isSpectator()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerDrops(PlayerDropItemEvent e) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
        if (skyPlayer.isSpectator()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerBreak(BlockBreakEvent e) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
        if (skyPlayer.isSpectator()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerPlace(BlockPlaceEvent e) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
        if (skyPlayer.isSpectator()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void FoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer((Player) e.getEntity());
            if (skyPlayer.isSpectator()) {
                e.setCancelled(true);
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
