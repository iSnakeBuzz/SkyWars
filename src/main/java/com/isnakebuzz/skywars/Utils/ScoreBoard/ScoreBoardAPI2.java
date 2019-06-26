package com.isnakebuzz.skywars.Utils.ScoreBoard;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Scoreboard.ScoreboardLib;
import com.isnakebuzz.skywars.Scoreboard.type.Scoreboard;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ScoreBoardAPI2 {

    private Main plugin;
    private HashMap<Player, Scoreboard> scoretask;

    public ScoreBoardAPI2(Main plugin) {
        this.plugin = plugin;
        this.scoretask = new HashMap<>();

    }

    public void setScoreBoard(Player p, ScoreboardType scoreboardType, boolean health, boolean spect, boolean gamePlayers) {
        removeScoreBoard(p);
        Scoreboard scoreboard = ScoreboardLib.createScoreboard(plugin, p).setHandler(new ScoreH(plugin, scoreboardType)).setUpdateInterval(20);
        scoreboard.activate();
        this.scoretask.put(p, scoreboard);
    }

    public void removeScoreBoard(Player p) {
        p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
        if (this.scoretask.containsKey(p)) this.scoretask.get(p).deactivate();
    }

    private String chars(Player p, String s) {
        int killStreak = 0;
        if (plugin.getPlayerManager().getPlayer(p) != null) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
            killStreak = skyPlayer.getKillStreak();
        }

        String transformed = (c(s)
                //Plugin holders
                .replaceAll("%version%", plugin.getDescription().getVersion())

                //Player and other things
                .replaceAll("%date%", getDate())
                .replaceAll("%online%", String.valueOf(plugin.getSkyWarsArena().getGamePlayers().size()))
                .replaceAll("%maxPlayers%", String.valueOf(plugin.getSkyWarsArena().getMaxPlayers()))
                .replaceAll("%map%", plugin.getSkyWarsArena().getMapName())
                .replaceAll("%startTime%", String.valueOf(plugin.getSkyWarsArena().getStartingTime()))
                .replaceAll("%cageOpening%", String.valueOf(plugin.getSkyWarsArena().getCageOpens()))
                .replaceAll("%refill_timer%", plugin.getSkyWarsArena().getParsedRefill())

                //Player placeholders
                .replaceAll("%kills%", String.valueOf(killStreak))

                //Remove events
                .replaceAll("<waiting>", "")
                .replaceAll("<starting>", "")
                .replaceAll("<cages>", "")
                .replaceAll("<refill>", "")
                .replaceAll("<ended>", "")
        );
        return transformed;
    }

    public String getDate() {
        return new SimpleDateFormat("MM/dd/yy").format(new Date());
    }

    private String randomString(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
