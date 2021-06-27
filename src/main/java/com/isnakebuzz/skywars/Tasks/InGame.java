package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Listeners.DeathMessages.Tagging;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class InGame extends BukkitRunnable {

    private SkyWars plugin;

    public InGame(SkyWars plugin) {
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