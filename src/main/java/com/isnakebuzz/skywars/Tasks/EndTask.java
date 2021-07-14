package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {

    private SkyWars plugin;

    public EndTask(SkyWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        if (plugin.getSkyWarsArena().getEndTimer() == 4) {
            plugin.getSkyWarsArena().SEND_ALL_TO_NEW_GAME();
        }

        if (plugin.getSkyWarsArena().getEndTimer() == 1) {
            plugin.getSkyWarsArena().SEND_ALL_TO_LOBBY();
        }

        if (plugin.getSkyWarsArena().getEndTimer() == 0) {
            //replace for others methods more efficients.
            plugin.debug("Restarting arena..");
            this.cancel();

            if (Statics.SnakeGameQueue) GameQueueAPI.removeGame(Statics.BungeeID);
            if (Statics.toRestart++ >= 20) {
                Bukkit.shutdown();
                return;
            }

            /* World restarting sync. */
            plugin.getListenerManager().reset();

            if (Bukkit.getOnlinePlayers().size() > 0) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    plugin.getScheduler().runSync(() -> online.kickPlayer("Arena restarted"));
                }
            }

            return;
        }

        plugin.getSkyWarsArena().setEndTimer(plugin.getSkyWarsArena().getEndTimer() - 1);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}