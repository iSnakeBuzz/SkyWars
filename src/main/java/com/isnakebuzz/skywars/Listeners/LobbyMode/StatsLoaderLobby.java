package com.isnakebuzz.skywars.Listeners.LobbyMode;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
    public void checkFreeKits(PlayerJoinEvent e) {
        plugin.getScheduler().runAsync(() -> {
            LobbyPlayer lobbyPlayer = plugin.getPlayerManager().getLbPlayer(e.getPlayer().getUniqueId());

            plugin.getKitLoader().getDefaultKits().stream()
                    .filter(kit -> !lobbyPlayer.getPurchKits().contains(kit.getName()))
                    .forEach(kit -> lobbyPlayer.getPurchKits().add(kit.getName()));

            if (!lobbyPlayer.getPurchCages().contains("default")) {
                lobbyPlayer.getPurchCages().add("default");
            }
        }, false);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        // Removing player async
        Player player = e.getPlayer();
        plugin.getScheduler().runAsync(() -> plugin.getDb().savePlayer(player.getUniqueId()), false);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
