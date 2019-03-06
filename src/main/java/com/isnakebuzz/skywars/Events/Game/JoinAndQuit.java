package com.isnakebuzz.skywars.Events.Game;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class JoinAndQuit implements Listener {

    private Main plugin;

    public JoinAndQuit(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerLoginEvent(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("sw.join.bypass")) {
            e.allow();
        } else {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "In Game");
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        plugin.getInventories().setSpectInventory(p);
        for (Player players : plugin.getSkyWarsArena().getGamePlayers()) {
            players.hidePlayer(p);
        }
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
            try {
                plugin.getDb().createPlayer(p);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
                skyPlayer.setStaff(true);
            });
        }, 10);
        e.setJoinMessage(null);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        plugin.getDb().savePlayer(p);
        PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(p, new ArrayList<>(), 0, null);
        plugin.getScoreBoardAPI().removeScoreBoard(p);
        Bukkit.getPluginManager().callEvent(playerDeathEvent);
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
            plugin.getDb().savePlayer(p);
        });
        e.setQuitMessage(null);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
