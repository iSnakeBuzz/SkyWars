package com.isnakebuzz.skywars.Scoreboard;

import com.isnakebuzz.skywars.Scoreboard.type.Scoreboard;
import com.isnakebuzz.skywars.Scoreboard.type.SimpleScoreboard;
import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScoreboardLib extends JavaPlugin {

    public static Scoreboard createScoreboard(SkyWars plugin, Player holder) {
        return new SimpleScoreboard(plugin, holder);
    }

}