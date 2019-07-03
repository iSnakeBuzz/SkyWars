package com.isnakebuzz.skywars.Teams;

import com.isnakebuzz.skywars.Main;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class TeamManager {

    private Main plugin;

    public TeamManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadTeams() {
        ConfigurationSection arena = plugin.getConfig("Extra/Arena").getConfigurationSection("Teams");

        boolean enabledTeams = arena.getBoolean("enabled", false);
        boolean alphabeticNames = arena.getBoolean("alphabetic names", true);
        int teamSize = arena.getInt("size", 2);

        if (enabledTeams) {

        }

    }

}
