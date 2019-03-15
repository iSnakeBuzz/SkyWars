package com.isnakebuzz.skywars.Listeners.Lobby;

import com.isnakebuzz.ccsigns.Enums.PacketType;
import com.isnakebuzz.ccsigns.utils.SignsAPI;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Tasks.StartingTask;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        plugin.debug("Player joining timings.");
        Long currentTimeMillis = System.currentTimeMillis();
        Configuration lang = plugin.getConfig("Lang");

        Player p = e.getPlayer();
        p.teleport(plugin.getSkyWarsArena().getLobbyLocation());
        plugin.getSkyWarsArena().getGamePlayers().add(p);
        plugin.getInventories().setLobbyInventory(p);
        PacketsAPI.sendClean(p);

        plugin.getScoreBoardAPI().setScoreBoard(p, ScoreBoardAPI.ScoreboardType.PRELOBBY, false, false, false);
        plugin.getDb().createPlayer(p);

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

        if (Statics.isCCSings) {
            String playerOnline = String.valueOf(Bukkit.getOnlinePlayers().size());
            String maxPlayer = String.valueOf(plugin.getSkyWarsArena().getMaxPlayers());

            SignsAPI.sendPacket(PacketType.PLAYERS, Statics.BungeeID, playerOnline, maxPlayer);
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

        plugin.getScoreBoardAPI().removeScoreBoard(p);
        plugin.getDb().savePlayer(p);

        if (Statics.isCCSings) {
            String playerOnline = String.valueOf(Bukkit.getOnlinePlayers().size() - 1);
            String maxPlayer = String.valueOf(plugin.getSkyWarsArena().getMaxPlayers());

            SignsAPI.sendPacket(PacketType.PLAYERS, Statics.BungeeID, playerOnline, maxPlayer);
        }

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
