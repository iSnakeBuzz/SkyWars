package com.isnakebuzz.skywars.Utils.ScoreBoard;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.QueueEvents.QueueEvent;
import com.isnakebuzz.skywars.Scoreboard.common.EntryBuilder;
import com.isnakebuzz.skywars.Scoreboard.type.Entry;
import com.isnakebuzz.skywars.Scoreboard.type.ScoreboardHandler;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScoreH implements ScoreboardHandler {

    private Main plugin;
    private Configuration config;
    private ScoreboardType scoreboardType;

    //Scoreboard information
    private String title;

    public ScoreH(Main plugin, ScoreboardType scoreboardType) {
        this.plugin = plugin;
        this.scoreboardType = scoreboardType;

        this.config = plugin.getConfigUtils().getConfig(plugin, "Extra/ScoreBoards");

        //Scoreboard information
        this.title = c(this.config.getString(scoreboardType.toString() + ".title"));
    }

    @Override
    public String getTitle(Player player) {
        return title;
    }

    @Override
    public List<Entry> getEntries(Player p) {
        EntryBuilder entries = new EntryBuilder();

        for (final String s : config.getStringList(scoreboardType.toString() + ".lines")) {
            String s2 = chars(p, s);

            if (s.contains("<starting>")) {
                if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.STARTING)) {
                    if (ChatColor.stripColor(s2).isEmpty()) {
                        entries.blank();
                    } else {
                        entries.next(s2);
                    }
                }
            } else if (s.contains("<waiting>")) {
                if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.WAITING)) {
                    if (ChatColor.stripColor(s2).isEmpty()) {
                        entries.blank();
                    } else {
                        entries.next(s2);
                    }
                }
            } else if (s.contains("<cages>")) {
                if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.CAGEOPENING)) {
                    if (ChatColor.stripColor(s2).isEmpty()) {
                        entries.blank();
                    } else {
                        entries.next(s2);
                    }
                }
            } else if (s.contains("<queue>")) {
                if (plugin.getEventsManager().isActived() && plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.INGAME)) {
                    if (ChatColor.stripColor(s2).isEmpty()) {
                        entries.blank();
                    } else {
                        entries.next(s2);
                    }
                }
            } else if (s.contains("<ended>")) {
                if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.FINISH)) {
                    if (ChatColor.stripColor(s2).isEmpty()) {
                        entries.blank();
                    } else {
                        entries.next(s2);
                    }
                }
            } else {
                if (ChatColor.stripColor(s2).isEmpty()) {
                    entries.blank();
                } else {
                    entries.next(s2);
                }
            }

        }

        return entries.build();
    }

    private String chars(Player p, String s) {
        int killStreak = 0;
        if (plugin.getPlayerManager().getPlayer(p) != null) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
            killStreak = skyPlayer.getKillStreak();
        }

        String eventHolder = "Loading..";

        if (plugin.getEventsManager().getActualQueue() != null) {
            QueueEvent queueEvent = plugin.getEventsManager().getActualQueue();
            String parsed_Time = plugin.getTimerManager().transformToDate(queueEvent.getEventTime());
            eventHolder = queueEvent.getPlaceholder().replaceAll("%queue_time%", parsed_Time);
        }

        return (c(s)
                //Plugin holders
                .replaceAll("%version%", plugin.getDescription().getVersion())

                //Player and other things
                .replaceAll("%date%", getDate())
                .replaceAll("%online%", String.valueOf(plugin.getSkyWarsArena().getGamePlayers().size()))
                .replaceAll("%maxPlayers%", String.valueOf(plugin.getSkyWarsArena().getMaxPlayers()))
                .replaceAll("%map%", plugin.getSkyWarsArena().getMapName())
                .replaceAll("%startTime%", String.valueOf(plugin.getSkyWarsArena().getStartingTime()))
                .replaceAll("%cageOpening%", String.valueOf(plugin.getSkyWarsArena().getCageOpens()))
                .replaceAll("%queue_holder%", eventHolder)

                //Player placeholders
                .replaceAll("%kills%", String.valueOf(killStreak))

                //Remove events
                .replaceAll("<waiting>", "")
                .replaceAll("<starting>", "")
                .replaceAll("<cages>", "")
                .replaceAll("<queue>", "")
                .replaceAll("<ended>", "")
        );
    }

    private String getDate() {
        return new SimpleDateFormat("MM/dd/yy").format(new Date());
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
