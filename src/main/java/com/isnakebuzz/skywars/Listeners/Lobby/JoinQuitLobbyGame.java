package com.isnakebuzz.skywars.Listeners.Lobby;

import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Tasks.CageOpeningTask;
import com.isnakebuzz.skywars.Tasks.LobbyTask;
import com.isnakebuzz.skywars.Teams.Team;
import com.isnakebuzz.skywars.Utils.Console;
import com.isnakebuzz.skywars.Utils.Cuboids.Cage;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import com.isnakebuzz.skywars.Utils.LocUtils;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;


public class JoinQuitLobbyGame implements Listener {

    private final SkyWars plugin;

    public JoinQuitLobbyGame(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerLogin(AsyncPlayerPreLoginEvent e) {
        Console.debug("AsyncPlayerPreLoginEvent " + e.getName());


        /*Double join fix*/
        if (plugin.getPlayerManager().getDoubleJoinBug().contains(e.getUniqueId())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Double join bug");
            plugin.getPlayerManager().getDoubleJoinBug().remove(e.getUniqueId());
            plugin.getPlayerManager().removePlayer(e.getUniqueId());
            return;
        } else {
            plugin.getPlayerManager().getDoubleJoinBug().add(e.getUniqueId());
        }
        /*Double join fix END*/

        if (plugin.getSkyWarsArena().getGamePlayers().size() >= plugin.getSkyWarsArena().getMaxPlayers()) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "El servidor se encuentra lleno");
            return;
        }

        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            plugin.getDataManager().getDatabase().loadPlayer(e.getUniqueId(), e.getName());

        }
    }

    @EventHandler
    public void preventBugs(PlayerLoginEvent e) {
        if (plugin.getSkyWarsArena().getGamePlayers().size() >= plugin.getSkyWarsArena().getMaxPlayers()) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "El servidor actualmente se encuentra lleno :(");

            // Handling player denied bug
            if (plugin.getPlayerManager().containsPlayer(e.getPlayer().getUniqueId())) {
                plugin.getPlayerManager().removePlayer(e.getPlayer().getUniqueId());
            }
        }

    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        if (plugin.getSkyWarsArena().getGamePlayers().size() > plugin.getSkyWarsArena().getMaxPlayers()) {
            plugin.getSkyWarsArena().SEND_TO_NEW_GAME(e.getPlayer());
        }

        Console.debug("Player joining timings.");
        Long currentTimeMillis = System.currentTimeMillis();
        Configuration lang = plugin.getConfig("Lang");

        Player p = e.getPlayer();
        plugin.getSkyWarsArena().getGamePlayers().add(p);
        Console.debug("Players: " + plugin.getSkyWarsArena().getGamePlayers().size());

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


        /* Check start arena */
        if (plugin.getSkyWarsArena().checkStart()) {
            Console.debug("Starting arena");
            plugin.getSkyWarsArena().setGameStatus(GameStatus.STARTING);

            if (Statics.skyMode.equals(GameType.SOLO)) {

                /* OTHER THINGS */
                plugin.getListenerManager().loadCageOpens();

                plugin.getChestController().load();
                plugin.closeInventory();

                new CageOpeningTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20);

            } else if (Statics.skyMode.equals(GameType.TEAM)) {
                new LobbyTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20);
            }
        }

        Console.debug("Finish loading in " + counter(currentTimeMillis));
    }

    @EventHandler
    public void joinUtils(PlayerJoinEvent e) {
        if (Statics.SnakeGameQueue) {
            int playerOnline = Bukkit.getOnlinePlayers().size();
            GameQueueAPI.updatePlayers(Statics.BungeeID, playerOnline);
        }
    }

    @EventHandler
    public void PlayerJoinTeleport(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (Statics.skyMode.equals(GameType.SOLO)) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p.getUniqueId());

            Team team = plugin.getTeamManager().addTeam(skyPlayer);
            int spawnID = team.getSpawnID();

            Location spawnLocation = plugin.getSkyWarsArena().getSpawnLocations().get(spawnID);
            Location lobbyLocation = plugin.getSkyWarsArena().getLobbyLocation();

            LocUtils.teleport(p, spawnLocation, lobbyLocation);

            Cage cage = new Cage(plugin, spawnLocation, team.getCage());
            cage.paste();
            plugin.getCagesManager().addCage(spawnID, cage);
        } else if (Statics.skyMode.equals(GameType.TEAM)) {
            p.teleport(plugin.getSkyWarsArena().getLobbyLocation());
        }

    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Configuration lang = plugin.getConfig("Lang");
        Player p = e.getPlayer();
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p.getUniqueId());
        plugin.getTeamManager().removeTeam(skyPlayer);

        /* Check if team not equals null and removing cage :) */
        if (skyPlayer.getTeam() != null)
            plugin.getCagesManager().deleteCage(skyPlayer.getTeam().getSpawnID());

        plugin.getSkyWarsArena().getGamePlayers().remove(p);

        plugin.getScoreBoardAPI2().removeScoreBoard(p);
        plugin.getVoteManager().removeVoteFrom(e.getPlayer());

        if (Statics.SnakeGameQueue) {
            int playerOnline = Bukkit.getOnlinePlayers().size() - 1;
            GameQueueAPI.updatePlayers(Statics.BungeeID, playerOnline);
        }

        // Saving uuid in momentaneus ram..
        UUID uuid = p.getUniqueId();
        plugin.getPlayerManager().getDoubleJoinBug().remove(uuid);

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
