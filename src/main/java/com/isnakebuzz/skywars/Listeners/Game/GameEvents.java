package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Calls.Events.SkyWinEvent;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Tasks.EndTask;
import com.isnakebuzz.skywars.Tasks.WinEffect;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.isnakebuzz.skywars.Utils.Strings.StringUtils.centerText;

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

        if (e.getSkyPlayer() != null) {
            e.getSkyPlayer().addWins(1);
            new WinEffect(plugin, e.getSkyPlayer().getPlayer()).runTaskTimer(plugin, 0, 20);

            List<SkyPlayer> list = new ArrayList<>(plugin.getPlayerManager().getPlayers());
            Collections.sort(list);

            plugin.debug("TOP Kills: " + list.toString());

            SkyPlayer top1 = (SkyPlayer) getSafeList(list, 0);
            SkyPlayer top2 = (SkyPlayer) getSafeList(list, 1);
            SkyPlayer top3 = (SkyPlayer) getSafeList(list, 2);

            for (String msgs : lang.getStringList("Winner")) {
                String message = c(msgs);
                String s = message.replaceAll("%winner%", e.getSkyPlayer().getPlayer().getDisplayName())
                        .replaceAll("%top-name-1%", getName(top1)).replaceAll("%top-kills-1%", getKills(top1))
                        .replaceAll("%top-name-2%", getName(top2)).replaceAll("%top-kills-2%", getKills(top2))
                        .replaceAll("%top-name-3%", getName(top3)).replaceAll("%top-kills-3%", getKills(top3));


                if (s.contains("%center%")) {
                    s = centerText(s.replaceAll("%center%", ""), 150);
                }
                plugin.broadcast(s);
            }


        }

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

    public String getName(SkyPlayer skyPlayer) {
        if (skyPlayer != null) {
            return skyPlayer.getPlayer().getName();
        } else {
            return "none";
        }
    }

    public String getKills(SkyPlayer skyPlayer) {
        if (skyPlayer != null) {
            return String.valueOf(skyPlayer.getKillStreak());
        } else {
            return "none";
        }
    }

    private <T> Object getSafeList(List<T> list, int i) {
        if (i < list.size()) {
            return list.get(i);
        } else {
            return null;
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}