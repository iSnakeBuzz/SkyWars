package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
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
            Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {

                int id = 0;
                for (Player inGame : plugin.getSkyWarsArena().getGamePlayers()) {
                    int finalId = id;
                    Location location = plugin.getSkyWarsArena().getSpawnLocations().get(finalId);

                    plugin.getSkyWarsArena().generateCage(location);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        inGame.teleport(location);
                        inGame.getInventory().clear();
                        plugin.getScoreBoardAPI().setScoreBoard(inGame, ScoreBoardAPI.ScoreboardType.INGAME, true, false, true);
                    });
                    id++;
                }
            });

            plugin.getSkyWarsArena().setGameStatus(GameStatus.CAGEOPENING);
            new CageOpeningTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20);
            plugin.getListenerManager().loadCageOpens();
            plugin.getListenerManager().loadInGame();
            this.cancel();
            return;
        }

        plugin.getSkyWarsArena().setStartingTime(plugin.getSkyWarsArena().getStartingTime() - 1);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}