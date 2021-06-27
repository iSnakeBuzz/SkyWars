package com.isnakebuzz.skywars.Listeners.Ended;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerEndBlock implements Listener {

    private SkyWars plugin;

    public PlayerEndBlock(SkyWars plugin) {
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

    @EventHandler
    public void PlayerBucketEmptyEvent(PlayerBucketEmptyEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void PlayerBucketFillEvent(PlayerBucketFillEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void VoidTP(EntityDamageEvent e) {
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
