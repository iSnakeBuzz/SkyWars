package com.isnakebuzz.skywars.Listeners.Lobby;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Tasks.StartingTask;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;


public class JoinAndLeave implements Listener {

    private Main plugin;

    public JoinAndLeave(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerLogin(AsyncPlayerPreLoginEvent e) {
        plugin.debug("AsyncPlayerPreLoginEvent " + e.getName());

        if (plugin.getSkyWarsArena().getGamePlayers().size() >= plugin.getSkyWarsArena().getMaxPlayers()) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "El servidor se encuentra lleno");
            return;
        }

        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getDataManager().getDatabase().createPlayer(e.getUniqueId()), 15);
        }
    }

    @EventHandler
    public void preventBugs(PlayerLoginEvent e) {
        if (plugin.getSkyWarsArena().getGamePlayers().size() >= plugin.getSkyWarsArena().getMaxPlayers()) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "El servidor actualmente se encuentra lleno :(");
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        if (plugin.getSkyWarsArena().getGamePlayers().size() > plugin.getSkyWarsArena().getMaxPlayers()) {
            plugin.getSkyWarsArena().SEND_TO_NEW_GAME(e.getPlayer());
        }

        plugin.debug("Player joining timings.");
        Long currentTimeMillis = System.currentTimeMillis();
        Configuration lang = plugin.getConfig("Lang");

        Player p = e.getPlayer();
        p.teleport(plugin.getSkyWarsArena().getLobbyLocation());
        plugin.getSkyWarsArena().getGamePlayers().add(p);
        plugin.getInventories().setLobbyInventory(p);
        PacketsAPI.sendClean(p);

        plugin.getScoreBoardAPI2().setScoreBoard(p, ScoreboardType.PRELOBBY, false, false, false);

        e.setJoinMessage(c(lang.getString("JoinMessage")
                .replaceAll("%player%", p.getName())
                .replaceAll("%online%", String.valueOf(plugin.getSkyWarsArena().getGamePlayers().size()))
                .replaceAll("%max%", String.valueOf(plugin.getSkyWarsArena().getMaxPlayers()))
        ));
        PacketsAPI.sendTitle(
                plugin,
                p,
                lang.getString("Join Title.Title"),
                lang.getString("Join Title.SubTitle"),
                lang.getInt("Join Title.FadeIn"),
                lang.getInt("Join Title.Stay"),
                lang.getInt("Join Title.FadeOut")
        );

        if (Statics.SnakeGameQueue) {
            int playerOnline = Bukkit.getOnlinePlayers().size();
            GameQueueAPI.updatePlayers(Statics.BungeeID, playerOnline);
        }

        if (plugin.getSkyWarsArena().checkStart()) {
            plugin.debug("Starting arena");
            plugin.getSkyWarsArena().setGameStatus(GameStatus.STARTING);
            new StartingTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20);
        }
        plugin.debug("Finish loading in " + counter(currentTimeMillis));
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Configuration lang = plugin.getConfig("Lang");
        Player p = e.getPlayer();
        plugin.getSkyWarsArena().getGamePlayers().remove(p);

        plugin.getScoreBoardAPI2().removeScoreBoard(p);
        plugin.getVoteManager().removeVoteFrom(e.getPlayer());

        if (Statics.SnakeGameQueue) {
            int playerOnline = Bukkit.getOnlinePlayers().size() - 1;
            GameQueueAPI.updatePlayers(Statics.BungeeID, playerOnline);
        }


        // Saving uuid in momentaneus ram..
        UUID uuid = p.getUniqueId();

        // Removing player async
        plugin.getScheduler().runAsync(() -> plugin.getDb().savePlayer(uuid), false);

        e.setQuitMessage(c(lang.getString("LeaveMessage")
                .replaceAll("%player%", p.getName())
                .replaceAll("%online%", String.valueOf(plugin.getSkyWarsArena().getGamePlayers().size()))
                .replaceAll("%max%", String.valueOf(plugin.getSkyWarsArena().getMaxPlayers()))
        ));
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private Long counter(Long currentTimeMillis) {
        return System.currentTimeMillis() - currentTimeMillis;
    }

}
