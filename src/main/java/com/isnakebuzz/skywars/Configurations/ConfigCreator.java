package com.isnakebuzz.skywars.Configurations;

import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigCreator {

    private static ConfigCreator instance;
    private File pluginDir;
    private File configFile;

    public static ConfigCreator get() {
        if (instance == null) {
            instance = new ConfigCreator();
        }
        return instance;
    }

    public void setup(Plugin p, String configname) {
        this.pluginDir = p.getDataFolder();
        this.configFile = new File(this.pluginDir, configname + ".yml");
        if (!this.configFile.exists()) {
            p.saveResource(configname + ".yml", false);
        }
    }

    public void setupOther(Plugin p, String configname) {
        this.pluginDir = p.getDataFolder();
        this.configFile = new File(this.pluginDir, configname);
        if (!this.configFile.exists()) {
            p.saveResource(configname, false);
        }
    }
}