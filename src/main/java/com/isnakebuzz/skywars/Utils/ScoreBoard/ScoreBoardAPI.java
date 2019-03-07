package com.isnakebuzz.skywars.Utils.ScoreBoard;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ScoreBoardAPI {

    private Main plugin;
    private HashMap<Player, Integer> scoretask;

    public ScoreBoardAPI(Main plugin) {
        this.plugin = plugin;
        this.scoretask = new HashMap<>();

    }

    public void setScoreBoard(Player p, ScoreboardType scoreboardType, boolean health, boolean spect, boolean gamePlayers) {
        removeScoreBoard(p);
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        ScoreBoardBuilder scoreboard = new ScoreBoardBuilder(randomString(8), health, spect, gamePlayers);
        int id = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/ScoreBoards");
            scoreboard.setName(chars(p, config.getString(scoreboardType.toString() + ".title")));

            int line = config.getStringList(scoreboardType.toString() + ".lines").size();
            for (final String s : config.getStringList(scoreboardType.toString() + ".lines")) {

                if (s.contains("<starting>")) {
                    if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.STARTING)) {
                        scoreboard.lines(line, chars(p, s));
                    } else {
                        scoreboard.dLine(line);
                    }
                } else if (s.contains("<waiting>")) {
                    if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.WAITING)) {
                        scoreboard.lines(line, chars(p, s));
                    } else {
                        scoreboard.dLine(line);
                    }
                } else if (s.contains("<cages>")) {
                    if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.CAGEOPENING)) {
                        scoreboard.lines(line, chars(p, s));
                    } else {
                        scoreboard.dLine(line);
                    }
                } else if (s.contains("<!isDead>")) {
                    SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
                    if (skyPlayer.isDead()) {
                        scoreboard.lines(line, chars(p, s));
                    } else {
                        scoreboard.dLine(line);
                    }
                } else {
                    scoreboard.lines(line, chars(p, s));
                }

                line--;
            }

            if (health) scoreboard.updatelife(plugin);
            if (spect) scoreboard.updatespect(p);
            if (gamePlayers) scoreboard.updategames(plugin, p);
        }, 0l, 20).getTaskId();
        p.setScoreboard(scoreboard.getScoreboard());
        this.scoretask.put(p, id);
    }

    public void removeScoreBoard(Player p) {
        p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
        if (this.scoretask.containsKey(p)) Bukkit.getScheduler().cancelTask(this.scoretask.get(p));
    }

    private String chars(Player p, String s) {
        int killStreak = 0;
        if (plugin.getPlayerManager().getPlayer(p) != null) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
            killStreak = skyPlayer.getKillStreak();
        }

        String transformed = (c(s)
                .replaceAll("%date%", getDate())
                .replaceAll("%online%", String.valueOf(plugin.getSkyWarsArena().getGamePlayers().size()))
                .replaceAll("%maxPlayers%", String.valueOf(plugin.getSkyWarsArena().getMaxPlayers()))
                .replaceAll("%map%", plugin.getSkyWarsArena().getMapName())
                .replaceAll("%startTime%", String.valueOf(plugin.getSkyWarsArena().getStartingTime()))
                .replaceAll("%cageOpening%", String.valueOf(plugin.getSkyWarsArena().getCageOpens()))

                //Player placeholders
                .replaceAll("%kills%", String.valueOf(killStreak))

                //Remove events
                .replaceAll("<waiting>", "")
                .replaceAll("<starting>", "")
                .replaceAll("<cages>", "")
                .replaceAll("<!isDead>", "")
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

    public enum ScoreboardType {
        PRELOBBY, INGAME
    }

}
