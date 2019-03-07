package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;

import java.io.File;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CagesManager {

    private Main plugin;
    private HashMap<String, File> cageMap;

    public CagesManager(Main plugin) {
        this.plugin = plugin;
        this.cageMap = new HashMap<>();
    }

    public void loadCages() {
        File dir = new File(plugin.getDataFolder() + "/Cages/");
        if (!dir.exists()) dir.mkdir();
        File[] files = dir.listFiles();

        for (File cage : files) {
            String cageName = cage.getName().split(Pattern.quote("."))[0];
            if (!this.cageMap.containsKey(cageName)) {
                this.cageMap.put(cageName, cage);
            }
        }

    }

    public File getDefault() {
        return this.cageMap.getOrDefault("default", null);
    }

}
