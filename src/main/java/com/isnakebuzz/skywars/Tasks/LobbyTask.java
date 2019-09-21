package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Teams.Team;
import com.isnakebuzz.skywars.Utils.Cuboids.Cage;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import com.isnakebuzz.skywars.Utils.LocUtils;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class LobbyTask extends BukkitRunnable {

    private Main plugin;

    public LobbyTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Lang");
        Set<String> keys = config.getConfigurationSection("Count Messages.Starting").getKeys(false);
        for (String time_config : keys) {
            if (plugin.getSkyWarsArena().getStartingTime() == Integer.parseInt(time_config)) {
                plugin.broadcast(config.getString("Count Messages.Starting." + time_config)
                        .replaceAll("%seconds%", String.valueOf(plugin.getSkyWarsArena().getStartingTime()))
                );
                break;
            }
        }

        if (plugin.getSkyWarsArena().getStartingTime() == 1) {

            //Setting up the CageOpening
            plugin.getSkyWarsArena().setGameStatus(GameStatus.CAGEOPENING);

            // Giving teams to players :)
            plugin.getTeamManager().giveTeams();

            // Spawning cages
            for (Team team : plugin.getTeamManager().getTeamMap().values()) {
                Location location = plugin.getSkyWarsArena().getSpawnLocations().get(team.getSpawnID());
                Cage cage = new Cage(plugin, location, team.getCage());
                cage.paste();
                plugin.getCagesManager().addCage(team.getSpawnID(), cage);
            }

            Location lobbyLocation = plugin.getSkyWarsArena().getLobbyLocation();

            // Teleporting players
            for (Player inGame : plugin.getSkyWarsArena().getGamePlayers()) {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(inGame.getUniqueId());
                plugin.debug("Team of Skyplayer: " + skyPlayer.getTeam().getID());
                Location location = plugin.getSkyWarsArena().getSpawnLocations().get(skyPlayer.getTeam().getSpawnID());

                Bukkit.getScheduler().runTask(plugin, () -> {
                    LocUtils.teleport(inGame, location, lobbyLocation);
                    inGame.getInventory().clear();
                    plugin.getScoreBoardAPI2().setScoreBoard(inGame, ScoreboardType.INGAME, true, false, true);
                });
            }

            /* REMOVING LOBBY */
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSkyWarsArena().removeLobby());

            plugin.debug("BUG CHECK. IF THIS MESSAGE APPEAR ONE MORE TIME, GAME BUGGED; PLEASE CHECK CODE :(");

            /* OTHER THINGS */
            plugin.getListenerManager().loadCageOpens();
            plugin.getListenerManager().loadInGame();
            plugin.getVoteManager().checkVotes();
            plugin.getChestController().load();
            plugin.closeInventory();

            new CageOpeningTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20);
            if (Statics.SnakeGameQueue) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> GameQueueAPI.removeGame(Statics.BungeeID), 20 * 3);
            }

            this.cancel();
        }

        plugin.getSkyWarsArena().setStatrtingTime(plugin.getSkyWarsArena().getStartingTime() - 1);

        if (plugin.getSkyWarsArena().getGamePlayers().size() < plugin.getSkyWarsArena().getMinPlayers()
                && plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.STARTING)
                && plugin.getSkyWarsArena().getStartingTime() >= 5) {
            this.cancel();
            plugin.getSkyWarsArena().cancelStart();
            plugin.debug("Cancelling starting task..");
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}