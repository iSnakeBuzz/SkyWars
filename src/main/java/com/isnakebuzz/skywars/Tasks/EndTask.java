package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {

    private Main plugin;

    public EndTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        if (plugin.getSkyWarsArena().getEndTimer() == 2) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                plugin.getDb().savePlayer(online.getUniqueId());
            }
        }

        if (plugin.getSkyWarsArena().getEndTimer() == 1) {
            plugin.getSkyWarsArena().SEND_ALL_TO_NEW_GAME();
        }

        if (plugin.getSkyWarsArena().getEndTimer() == 0) {
            //replace for others methodes more efficients.
            plugin.debug("Restarting arena..");
            this.cancel();
            if (Statics.isFawe) {
                if (Statics.toRestart++ >= 6) {
                    Bukkit.shutdown();
                    return;
                }

                plugin.getWorldRestarting().restartWorld();
                plugin.getListenerManager().reset();
                plugin.resetArena();
            } else {
                if (Statics.SnakeGameQueue) {
                    GameQueueAPI.removeGame(Statics.BungeeID);
                }
                Bukkit.shutdown();
                return;
            }


            for (Player online : Bukkit.getOnlinePlayers()) {
                PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(online, "Rejoined");
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    for (Player online2 : Bukkit.getOnlinePlayers()) {
                        if (!online.equals(online2)) {
                            online.showPlayer(online2);
                        }
                    }
                    Bukkit.getPluginManager().callEvent(playerJoinEvent);
                });
                AsyncPlayerPreLoginEvent asyncPlayerPreLoginEvent = new AsyncPlayerPreLoginEvent(online.getName(), online.getAddress().getAddress(), online.getUniqueId());
                Bukkit.getPluginManager().callEvent(asyncPlayerPreLoginEvent);
            }
            return;
        }

        plugin.getSkyWarsArena().setEndTimer(plugin.getSkyWarsArena().getEndTimer() - 1);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}