package com.isnakebuzz.skywars.Events.Lobby;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Task.StartingTask;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.sql.SQLException;

public class JoinAndLeave implements Listener {

    private Main plugin;

    public JoinAndLeave(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Configuration lang = plugin.getConfig("Lang");
        Player p = e.getPlayer();
        p.teleport(plugin.getSkyWarsArena().getLobbyLocation());
        plugin.getSkyWarsArena().getGamePlayers().add(p);
        plugin.getInventories().setLobbyInventory(p);
        PacketsAPI.sendClean(p);

        plugin.getScoreBoardAPI().setScoreBoard(p, ScoreBoardAPI.ScoreboardType.PRELOBBY, false, false, false);
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
            try {
                plugin.getDb().createPlayer(p);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }, 10);

        e.setJoinMessage(c(lang.getString("JoinMessage")
                .replaceAll("%player%", p.getName())
                .replaceAll("%online%", String.valueOf(plugin.getSkyWarsArena().getGamePlayers().size()))
                .replaceAll("%max%", String.valueOf(plugin.getSkyWarsArena().getMaxPlayers()))
        ));
        PacketsAPI.sendTitle(
                p,
                lang.getString("Join Title.Title"),
                lang.getString("Join Title.SubTitle"),
                lang.getInt("Join Title.FadeIn"),
                lang.getInt("Join Title.Stay"),
                lang.getInt("Join Title.FadeOut")
        );

        if (plugin.getSkyWarsArena().checkStart()) {
            plugin.getSkyWarsArena().setGameStatus(GameStatus.STARTING);
            plugin.getSkyWarsArena().setStartingTask(
                    Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new StartingTask(plugin), 0, 20)
            );
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Configuration lang = plugin.getConfig("Lang");
        Player p = e.getPlayer();
        plugin.getSkyWarsArena().getGamePlayers().remove(p);

        plugin.getScoreBoardAPI().removeScoreBoard(p);
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
            plugin.getDb().savePlayer(p);
        });

        e.setQuitMessage(c(lang.getString("LeaveMessage")
                .replaceAll("%player%", p.getName())
                .replaceAll("%online%", String.valueOf(plugin.getSkyWarsArena().getGamePlayers().size()))
                .replaceAll("%max%", String.valueOf(plugin.getSkyWarsArena().getMaxPlayers()))
        ));
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
