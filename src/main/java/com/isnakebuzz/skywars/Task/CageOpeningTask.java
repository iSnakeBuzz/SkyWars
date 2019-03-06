package com.isnakebuzz.skywars.Task;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CageOpeningTask implements Runnable {

    private Main plugin;

    public CageOpeningTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Lang");
        Set<String> keys = config.getConfigurationSection("Count Messages.Cages Open").getKeys(false);
        for (String time_config : keys) {
            if (plugin.getSkyWarsArena().getCageOpens() == Integer.valueOf(time_config)) {
                plugin.broadcast(config.getString("Count Messages.Cages Open." + time_config)
                        .replaceAll("%seconds%", String.valueOf(plugin.getSkyWarsArena().getCageOpens()))
                );
            }
        }

        if (plugin.getSkyWarsArena().getCageOpens() <= 1) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
                for (int id = 0; id <= plugin.getSkyWarsArena().getGamePlayers().size(); id++) {
                    int finalId = id;
                    if (id > plugin.getSkyWarsArena().getGamePlayers().size()) return;
                    Location location = plugin.getSkyWarsArena().getSpawnLocations().get(finalId);

                    plugin.getSkyWarsArena().deleteCage(location);
                }
            });

            Bukkit.getScheduler().cancelTask(plugin.getSkyWarsArena().getCageOpensTask());
            plugin.getSkyWarsArena().setGameStatus(GameStatus.INGAME);
            plugin.getEventsManager().unloadPrelobby();
            plugin.getSkyWarsArena().fillChests();

            Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
                plugin.getEventsManager().unloadCageOpens();
            }, 20 * 3);
        }

        plugin.getSkyWarsArena().setCageOpens(plugin.getSkyWarsArena().getCageOpens() - 1);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}