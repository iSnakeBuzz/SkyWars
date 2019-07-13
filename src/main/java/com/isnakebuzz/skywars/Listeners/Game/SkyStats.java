package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Calls.Events.SkyStatsEvent;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.StatType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SkyStats implements Listener {

    private Main plugin;

    public SkyStats(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void killerStats(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getEntity().getKiller());
            SkyStatsEvent statsEvent = new SkyStatsEvent(skyPlayer, StatType.KILL);
            Bukkit.getPluginManager().callEvent(statsEvent);

            if (!statsEvent.isCancelled()) {
                skyPlayer.addKillStreak();
                skyPlayer.addKills(1);
            }
        } else if (e.getEntity().getPlayer() != null) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getEntity().getPlayer());
            SkyStatsEvent statsEvent = new SkyStatsEvent(skyPlayer, StatType.DEATH);
            Bukkit.getPluginManager().callEvent(statsEvent);

            if (!statsEvent.isCancelled()){
                skyPlayer.addDeaths(1);
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
