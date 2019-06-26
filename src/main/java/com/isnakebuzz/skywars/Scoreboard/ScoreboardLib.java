package com.isnakebuzz.skywars.Scoreboard;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Scoreboard.type.Scoreboard;
import com.isnakebuzz.skywars.Scoreboard.type.SimpleScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScoreboardLib extends JavaPlugin {

    public static Scoreboard createScoreboard(Main plugin, Player holder) {
        return new SimpleScoreboard(plugin, holder);
    }

}