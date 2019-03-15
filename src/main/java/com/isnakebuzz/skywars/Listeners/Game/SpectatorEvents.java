package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SpectatorEvents implements Listener {

    private Main plugin;

    public SpectatorEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void putIn(PlayerInteractAtEntityEvent e) throws NoSuchFieldException, IllegalAccessException {
        Player p = e.getPlayer();
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
        if (skyPlayer.isSpectator()) {
            Configuration lang = plugin.getConfig("Lang");

            if (e.getRightClicked() instanceof Player && !skyPlayer.isSpectating()) {
                Player interacted = (Player) e.getRightClicked();
                PacketsAPI.setSpectating(p, interacted, true);
                skyPlayer.setSpectating(true);
                PacketsAPI.sendTitle(
                        plugin,
                        p,
                        lang.getString("Spectating Titles.Spectating.Title").replaceAll("%player%", interacted.getName()),
                        lang.getString("Spectating Titles.Spectating.SubTitle"),
                        lang.getInt("Spectating Titles.Spectating.FadeIn"),
                        lang.getInt("Spectating Titles.Spectating.Stay"),
                        lang.getInt("Spectating Titles.Spectating.FadeOut")
                );
            }
        }
    }

    @EventHandler
    public void outSpectator(PlayerToggleSneakEvent e) throws NoSuchFieldException, IllegalAccessException {
        if (!e.isSneaking()) return;

        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
        if (skyPlayer.isSpectator()) {
            if (skyPlayer.isSpectating()) {
                Configuration lang = plugin.getConfig("Lang");
                PacketsAPI.setSpectating(e.getPlayer(), null, false);
                skyPlayer.setSpectating(false);
                PacketsAPI.sendTitle(
                        plugin,
                        e.getPlayer(),
                        lang.getString("Spectating Titles.Exiting.Title"),
                        lang.getString("Spectating Titles.Exiting.SubTitle"),
                        lang.getInt("Spectating Titles.Exiting.FadeIn"),
                        lang.getInt("Spectating Titles.Exiting.Stay"),
                        lang.getInt("Spectating Titles.Exiting.FadeOut")
                );
            }
        }
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
    public void PlayerPickUp(PlayerPickupItemEvent e) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
        if (skyPlayer.isSpectator()) {
            e.setCancelled(true);
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
