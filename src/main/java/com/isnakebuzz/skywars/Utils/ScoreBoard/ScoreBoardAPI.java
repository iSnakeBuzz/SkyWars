package com.isnakebuzz.skywars.Utils.ScoreBoard;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Scoreboard.ScoreboardLib;
import com.isnakebuzz.skywars.Scoreboard.type.Scoreboard;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ScoreBoardAPI {

    private Main plugin;
    private HashMap<Player, Scoreboard> scoretask;
    private HashMap<Player, Integer> scoreUtilsTask;

    public ScoreBoardAPI(Main plugin) {
        this.plugin = plugin;
        this.scoretask = new HashMap<>();
        this.scoreUtilsTask = new HashMap<>();
    }

    public void setScoreBoard(Player p, ScoreboardType scoreboardType, boolean health, boolean spect, boolean gamePlayers) {
        ConfigurationSection arena = plugin.getConfig("Extra/Arena").getConfigurationSection("Teams");
        boolean enabledTeams = arena.getBoolean("enabled");


        removeScoreBoard(p);
        Scoreboard scoreboard = ScoreboardLib.createScoreboard(plugin, p).setHandler(new ScoreH(plugin, scoreboardType)).setUpdateInterval(20);
        scoreboard.activate();

        ScoreUtils scoreUtils = new ScoreUtils(plugin, p, health, spect, gamePlayers);
        int taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (health) scoreUtils.updatelife(plugin);
            if (spect) scoreUtils.updatespect(p);
            if (gamePlayers) {
                if (enabledTeams) scoreUtils.updateGameTAG(p);
                else scoreUtils.updategames(p);
            }
        }, 0, 20).getTaskId();

        scoreUtils.build(p);
        this.scoreUtilsTask.put(p, taskID);
        this.scoretask.put(p, scoreboard);
    }

    public void removeScoreBoard(Player p) {
        p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
        if (this.scoretask.containsKey(p)) this.scoretask.get(p).deactivate();
        if (this.scoreUtilsTask.containsKey(p)) Bukkit.getScheduler().cancelTask(this.scoreUtilsTask.get(p));
    }

}
