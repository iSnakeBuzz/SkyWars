package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {

    private Main plugin;

    public EndTask(Main plugin) {
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
            if (Statics.isFawe) {
                if (Statics.SnakeGameQueue) GameQueueAPI.removeGame(Statics.BungeeID);

                if (Statics.toRestart++ >= 20) {
                    Bukkit.shutdown();
                    return;
                }

                plugin.getWorldRestarting().restartWorld();
                plugin.getListenerManager().reset();
            } else {
                if (Statics.SnakeGameQueue) GameQueueAPI.removeGame(Statics.BungeeID);

                if (Statics.toRestart++ >= 20) {
                    Bukkit.shutdown();
                    return;
                }

                /* World restarting sync. */
                plugin.getScheduler().runSync(() -> {
                    World world = Bukkit.getWorld("world");
                    world.setAutoSave(false);
                    WorldCreator worldReseter = new WorldCreator(world.getName());
                    Bukkit.unloadWorld(world, false);
                    Bukkit.getServer().createWorld(worldReseter);
                });

                plugin.getListenerManager().reset();
                return;
            }


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