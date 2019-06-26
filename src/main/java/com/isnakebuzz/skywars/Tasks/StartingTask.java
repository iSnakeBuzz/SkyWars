package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.ccsigns.Enums.PacketType;
import com.isnakebuzz.ccsigns.utils.SignsAPI;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Cuboids.Cage;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class StartingTask extends BukkitRunnable {

    private Main plugin;

    public StartingTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Lang");
        Set<String> keys = config.getConfigurationSection("Count Messages.Starting").getKeys(false);
        for (String time_config : keys) {
            if (plugin.getSkyWarsArena().getStartingTime() == Integer.valueOf(time_config)) {
                plugin.broadcast(config.getString("Count Messages.Starting." + time_config)
                        .replaceAll("%seconds%", String.valueOf(plugin.getSkyWarsArena().getStartingTime()))
                );
            }
        }

        if (plugin.getSkyWarsArena().getStartingTime() <= 1) {
            int id = 0;
            for (Player inGame : plugin.getSkyWarsArena().getGamePlayers()) {
                int finalId = id;
                Location location = plugin.getSkyWarsArena().getSpawnLocations().get(finalId);

                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(inGame);
                Cage cage = new Cage(plugin, location, skyPlayer.getCageName());
                cage.paste();
                plugin.getCagesManager().addCage(cage);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    inGame.teleport(location);
                    inGame.getInventory().clear();
                    plugin.getScoreBoardAPI2().setScoreBoard(inGame, ScoreboardType.INGAME, true, false, true);
                });
                id++;
            }

            /* REMOVING LOBBY */
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSkyWarsArena().removeLobby());

            /* OTHER THINGS */
            plugin.getSkyWarsArena().setGameStatus(GameStatus.CAGEOPENING);
            Bukkit.getScheduler().runTask(plugin, () -> new CageOpeningTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20));
            plugin.getListenerManager().loadCageOpens();
            plugin.getListenerManager().loadInGame();
            plugin.getVoteManager().checkVotes();
            plugin.getChestController().load();
            plugin.closeInventory();
            this.cancel();
            if (Statics.isCCSings) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> SignsAPI.sendPacket(PacketType.REMOVE, Statics.BungeeID), 20 * 3);
            }
            return;
        }

        plugin.getSkyWarsArena().setStatrtingTime(plugin.getSkyWarsArena().getStartingTime() - 1);

        if (plugin.getSkyWarsArena().getGamePlayers().size() < plugin.getSkyWarsArena().getMinPlayers() && plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.STARTING)) {
            this.cancel();
            plugin.getSkyWarsArena().cancelStart();
            plugin.debug("Cancelling starting task..");
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}