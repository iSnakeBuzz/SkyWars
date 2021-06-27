package com.isnakebuzz.skywars.Utils.Manager;

import com.google.common.collect.Maps;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Cuboids.Cage;

import java.io.File;
import java.util.HashMap;

public class CagesManager {

    private SkyWars plugin;
    private HashMap<Integer, Cage> cages;

    public CagesManager(SkyWars plugin) {
        this.plugin = plugin;
        this.cages = Maps.newHashMap();
    }

    public void addCage(int spawnID, Cage cage) {
        if (!this.cages.containsKey(spawnID)) {
            this.cages.put(spawnID, cage);
        }
    }

    public void deleteCage(int spawnID) {
        if (this.cages.containsKey(spawnID))
            this.cages.get(spawnID).undo();
    }

    public void deleteAllCages() {
        for (Cage cage : this.cages.values()) {
            cage.undo();
        }
    }

    public File getCage(String cageName) {
        File cage = new File(plugin.getDataFolder(), "Cages/" + cageName + ".snakeschem");
        return getOrDefault(cage);
    }

    private File getOrDefault(File file) {
        if (!file.exists()) {
            return new File(plugin.getDataFolder(), "Cages/default.snakeschem");
        }
        return file;
    }

}
