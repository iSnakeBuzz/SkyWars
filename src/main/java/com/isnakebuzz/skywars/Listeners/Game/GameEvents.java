package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Calls.Events.SkyEndEvent;
import com.isnakebuzz.skywars.Calls.Events.SkyWinEffectEvent;
import com.isnakebuzz.skywars.Calls.Events.SkyWinEvent;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Tasks.EndTask;
import com.isnakebuzz.skywars.Tasks.WinEffect;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.isnakebuzz.skywars.Utils.Strings.StringUtils.centerText;

public class GameEvents implements Listener {

    private SkyWars plugin;

    public GameEvents(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void SkyWinEvent(SkyWinEvent e) {
        Configuration lang = plugin.getConfig("Lang");

        plugin.getListenerManager().unloadIngame();
        plugin.getSkyWarsArena().setGameStatus(GameStatus.FINISH);
        plugin.getListenerManager().loadEnd();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new SkyEndEvent(e.getTeam())));

        new EndTask(plugin).runTaskTimerAsynchronously(plugin, 0, 20);

        if (e.getTeam() != null) {
            e.getTeam().addWin();
            e.getTeam().getTeamPlayers().stream().filter(SkyPlayer::isOnline).forEach(skyPlayer -> skyPlayer.addCoins(plugin.getConfig("Settings").getInt("Coins.wins")));

            //Win effect event
            SkyWinEffectEvent effectEvent = new SkyWinEffectEvent();

            if (!effectEvent.isCancelled()) {
                new WinEffect(plugin, e.getTeam()).runTaskTimer(plugin, 0, 20);
            }

            Bukkit.getPluginManager().callEvent(effectEvent);

            broadCastWin(lang, e);
        }
    }

    public void broadCastWin(Configuration lang, SkyWinEvent e) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < e.getTeam().getTeamPlayers().size(); i++) {
            if (i == 0) {
                stringBuilder.append(e.getTeam().getTeamPlayers().get(i).getName());
            } else {
                String toAdd = ", " + e.getTeam().getTeamPlayers().get(i).getName();
                stringBuilder.append(toAdd);
            }
        }

        List<SkyPlayer> list = new ArrayList<>(plugin.getPlayerManager().getPlayers());
        Collections.sort(list);

        plugin.debug("TOP Kills: " + list.toString());

        SkyPlayer top1 = (SkyPlayer) getSafeList(list, 0);
        SkyPlayer top2 = (SkyPlayer) getSafeList(list, 1);
        SkyPlayer top3 = (SkyPlayer) getSafeList(list, 2);

        for (String msgs : lang.getStringList("Winner")) {
            String message = c(msgs);
            String s = message.replaceAll("%winner%", stringBuilder.toString())
                    .replaceAll("%top-name-1%", getName(top1)).replaceAll("%top-kills-1%", getKills(top1))
                    .replaceAll("%top-name-2%", getName(top2)).replaceAll("%top-kills-2%", getKills(top2))
                    .replaceAll("%top-name-3%", getName(top3)).replaceAll("%top-kills-3%", getKills(top3));


            if (s.contains("%center%")) {
                s = centerText(s.replaceAll("%center%", ""), 150);
            }
            plugin.broadcast(s);
        }

    }

    public String getName(SkyPlayer skyPlayer) {
        if (skyPlayer != null) {
            return skyPlayer.getName();
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