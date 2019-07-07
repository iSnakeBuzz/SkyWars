package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Listeners.DeathMessages.Tagging;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class InGame extends BukkitRunnable {

    private Main plugin;

    public InGame(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.INGAME)) {
            Tagging.increaseTimers();
        } else {
            this.cancel();
        }

        if (plugin.getSkyWarsArena().getGamePlayers().size() < 2 && !plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.FINISH)) {
            this.cancel();
            plugin.resetArena();
        }

    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}