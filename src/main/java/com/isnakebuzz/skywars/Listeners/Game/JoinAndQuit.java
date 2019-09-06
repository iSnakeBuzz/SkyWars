package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

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
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "En juego, acceso exclusivo para staffs (perm: sw.join.bypass)");
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        plugin.getInventories().setSpectInventory(p);
        for (Player players : plugin.getSkyWarsArena().getGamePlayers()) {
            players.hidePlayer(p);
        }

        //teleporting to arena lobby
        p.teleport(plugin.getSkyWarsArena().getLobbyLocation());

        //setting staff mode to player
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        skyPlayer.setStaff(true);

        // Adding player to stats
        plugin.getPlayerManager().addPlayer(p.getUniqueId(), skyPlayer);

        //Giving scoreboard to player
        plugin.getScoreBoardAPI2().setScoreBoard(p, ScoreboardType.INGAME, true, true, true);

        //plugin.getDb().createPlayer(p);
        e.setJoinMessage(null);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        plugin.debug("");

        // Calling leave death
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        if (!skyPlayer.isSpectator() && plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.INGAME)) {
            PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(p, Arrays.asList(p.getInventory().getContents()), 0, "player left the game");
            Bukkit.getPluginManager().callEvent(playerDeathEvent);
            plugin.debug("Calling sky death by quit (Spectator)");
        }

        //Removing player scoreboard
        plugin.getScoreBoardAPI2().removeScoreBoard(p);

        // Removing player async
        plugin.getScheduler().runAsync(() -> plugin.getDb().savePlayer(p.getUniqueId()), false);

        e.setQuitMessage(null);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
