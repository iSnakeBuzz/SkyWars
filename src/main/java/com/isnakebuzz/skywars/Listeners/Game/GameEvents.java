package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Calls.Events.SkyWinEvent;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Tasks.EndTask;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameEvents implements Listener {

    private Main plugin;

    public GameEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void SkyWinEvent(SkyWinEvent e) {
        Configuration lang = plugin.getConfig("Lang");

        plugin.getListenerManager().unloadIngame();
        plugin.getSkyWarsArena().setGameStatus(GameStatus.FINISH);
        plugin.getListenerManager().loadEnd();
        new EndTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20);

        for (Player all : Bukkit.getOnlinePlayers()) {
            PacketsAPI.sendTitle(
                    plugin,
                    all,
                    lang.getString("End Game.Title"),
                    lang.getString("End Game.SubTitle"),
                    lang.getInt("End Game.FadeIn"),
                    lang.getInt("End Game.Stay"),
                    lang.getInt("End Game.FadeOut")
            );
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
