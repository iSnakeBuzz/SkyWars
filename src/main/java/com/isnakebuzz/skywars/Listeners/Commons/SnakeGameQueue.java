package com.isnakebuzz.skywars.Listeners.Commons;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.snakegq.API.GameQueueAPI;
import com.isnakebuzz.snakegq.Enums.GameQueueStatus;
import com.isnakebuzz.snakegq.Enums.GameQueueType;
import com.isnakebuzz.snakegq.Utils.ReCreateGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SnakeGameQueue implements Listener {

    private Main plugin;

    public SnakeGameQueue(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void recreate(ReCreateGameEvent e) {
        if (!plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.WAITING)) return;

        int playerOnline = Bukkit.getOnlinePlayers().size();
        int maxPlayer = plugin.getSkyWarsArena().getMaxPlayers();

        if (Statics.skyMode.equals(GameType.TEAM)) {
            GameQueueAPI.createGame(Statics.BungeeID, Statics.mapName, GameQueueType.TEAM, GameQueueStatus.WAITING, playerOnline, maxPlayer);
        } else if (Statics.skyMode.equals(GameType.SOLO)) {
            GameQueueAPI.createGame(Statics.BungeeID, Statics.mapName, GameQueueType.SOLO, GameQueueStatus.WAITING, playerOnline, maxPlayer);
        }

    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
