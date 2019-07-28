package com.isnakebuzz.skywars.Listeners.LobbyMode;

import com.isnakebuzz.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StatsLoaderLobby implements Listener {

    private Main plugin;

    public StatsLoaderLobby(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(final AsyncPlayerPreLoginEvent e) {
        plugin.debug("AsyncPlayerPreLoginEvent " + e.getName());

        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getDataManager().getDatabase().createPlayer(e.getUniqueId()), 15);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLoginMonitor(PlayerLoginEvent e) {
        plugin.debug("PlayerLoginEvent Monitor " + e.getPlayer().getName());

        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            plugin.debug("onPlayerLoginMonitor player not allowed, removing cached");
            plugin.getPlayerManager().removeLbPlayer(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        // Removing player async
        plugin.getScheduler().runAsync(() -> plugin.getDb().savePlayer(e.getPlayer().getUniqueId()), false);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
