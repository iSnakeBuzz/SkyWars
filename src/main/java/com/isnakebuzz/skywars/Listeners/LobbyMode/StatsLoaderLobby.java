package com.isnakebuzz.skywars.Listeners.LobbyMode;

import com.isnakebuzz.skywars.Player.LobbyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Console;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class StatsLoaderLobby implements Listener {

    private final SkyWars plugin;

    public StatsLoaderLobby(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(final AsyncPlayerPreLoginEvent e) {
        Console.debug("AsyncPlayerPreLoginEvent " + e.getName());

        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            plugin.getDataManager().getDatabase().loadPlayer(e.getUniqueId(), e.getName());

            LobbyPlayer lobbyPlayer = plugin.getPlayerManager().getLbPlayer(e.getUniqueId());

            plugin.getKitLoader().getDefaultKits().stream()
                    .filter(kit -> !lobbyPlayer.getPurchKits().contains(kit.getName()))
                    .forEach(kit -> lobbyPlayer.getPurchKits().add(kit.getName()));

            if (!lobbyPlayer.getPurchCages().contains("default")) {
                lobbyPlayer.getPurchCages().add("default");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLoginMonitor(PlayerLoginEvent e) {
        Console.debug("PlayerLoginEvent Monitor " + e.getPlayer().getName());

        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            Console.debug("onPlayerLoginMonitor player not allowed, removing cached");
            plugin.getPlayerManager().removeLbPlayer(e.getPlayer().getUniqueId());
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
