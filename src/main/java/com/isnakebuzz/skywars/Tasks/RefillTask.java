package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

public class RefillTask extends BukkitRunnable {

    private SkyWars plugin;

    //Refill utils
    private String changeTo;

    public RefillTask(SkyWars plugin) {
        this.plugin = plugin;

        //Refill Utils
        ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Holograms.ChestRefill");

        changeTo = c(lang.getString("Name"));
    }

    @Override
    public void run() {
        if (!plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.INGAME)) {
            this.cancel();
        }

        /*
        plugin.getChestRefillManager().update(changeTo.replaceAll("%timer%", plugin.getSkyWarsArena().getParsedRefill()));
         */

        /*
        if (plugin.getSkyWarsArena().getRefillTimer() == 1) {

            Configuration arena = plugin.getConfig("Extra/Arena");
            plugin.getSkyWarsArena().setRefillTimer(arena.getInt("Timers.ChestRefill"));
            plugin.getSkyWarsArena().fillChests();
            plugin.getChestRefillManager().reset();

            Configuration lang = plugin.getConfig("Lang");

            PacketsAPI.broadcastTitle(plugin,
                    lang.getString("Chest Refilled.Title"),
                    lang.getString("Chest Refilled.SubTitle"),
                    lang.getInt("Chest Refilled.FadeIn"),
                    lang.getInt("Chest Refilled.Stay"),
                    lang.getInt("Chest Refilled.FadeOut")
            );
        } else {
            plugin.getSkyWarsArena().setRefillTimer(plugin.getSkyWarsArena().getRefillTimer() - 1);
        }*/

    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}