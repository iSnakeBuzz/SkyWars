package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Calls.Events.SkyCagesOpenEvent;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

import static com.isnakebuzz.skywars.Utils.Strings.StringUtils.centerText;

public class CageOpeningTask extends BukkitRunnable {

    private SkyWars plugin;

    public CageOpeningTask(SkyWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Lang");
        Set<String> keys = config.getConfigurationSection("Count Messages.Cages Open").getKeys(false);
        for (String time_config : keys) {
            if (plugin.getSkyWarsArena().getCageOpens() == Integer.parseInt(time_config)) {
                plugin.broadcast(config.getString("Count Messages.Cages Open." + time_config)
                        .replaceAll("%seconds%", String.valueOf(plugin.getSkyWarsArena().getCageOpens()))
                );
                break;
            }
        }

        if (plugin.getSkyWarsArena().getCageOpens() == 1) {

            plugin.getCagesManager().deleteAllCages();

            /*Active on team*/
            plugin.getSkyWarsArena().setGameStatus(GameStatus.INGAME);

            plugin.getListenerManager().unloadPrelobby();
            plugin.getSkyWarsArena().fillChests();
            plugin.closeInventory();

            for (String msgs : config.getStringList("Banner")) {
                String message = c(msgs);
                if (message.contains("%center%")) {
                    message = centerText(message.replaceAll("%center%", ""), 150);
                }

                plugin.broadcast(message);
            }

            /*
             * Giving kits to players
             * 1.0.0 System
             */

            for (Player player : Bukkit.getOnlinePlayers()) {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
                if (!skyPlayer.isSpectator()) {
                    plugin.getKitLoader().giveKit(skyPlayer);
                    Bukkit.getScheduler().runTask(plugin, () -> player.setGameMode(GameMode.SURVIVAL));
                }
            }


            new InGame(plugin).runTaskTimerAsynchronously(plugin, 0, 20);

            //Bukkit.getScheduler().runTask(plugin, () -> new RefillTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20));
            // Execute game queue events
            plugin.getEventsManager().execute();
            //Calling SkyCagesOPenEvent
            Bukkit.getPluginManager().callEvent(new SkyCagesOpenEvent());

            if (Statics.skyMode.equals(GameType.SOLO)) {
                plugin.getListenerManager().loadInGame();
                plugin.getVoteManager().checkVotes();

                plugin.getScheduler().runSync(() -> {
                    plugin.getSkyWarsArena().getGamePlayers().forEach(player -> {
                        plugin.getScoreBoardAPI2().setScoreBoard(player, ScoreboardType.INGAME, true, false, true);
                    });
                });
            }

            /*Removing game*/
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> GameQueueAPI.removeGame(Statics.BungeeID), 20 * 3);


            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> plugin.getListenerManager().unloadCageOpens(), 20 * 5);
            this.cancel();
        }


        if (Statics.skyMode.equals(GameType.SOLO)) {

            if (plugin.getSkyWarsArena().getGamePlayers().size() < plugin.getSkyWarsArena().getMinPlayers()
                    && plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.STARTING)) {
                this.cancel();
                plugin.getSkyWarsArena().cancelStart();
                plugin.debug("Cancelling starting task..");
            }

        } else if (Statics.skyMode.equals(GameType.TEAM)) {
            if (plugin.getSkyWarsArena().getGamePlayers().size() < plugin.getSkyWarsArena().getMinPlayers() && !plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.FINISH)) {
                this.cancel();
                plugin.getSkyWarsArena().cancelStart();
                plugin.debug("Cancelling starting task..");
                return;
            }
        }

        plugin.getSkyWarsArena().setCageOpens(plugin.getSkyWarsArena().getCageOpens() - 1);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}