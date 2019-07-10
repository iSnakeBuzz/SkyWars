package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Cuboids.Cage;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class CagesManager {

    private Main plugin;
    private List<Cage> cages;

    public CagesManager(Main plugin) {
        this.plugin = plugin;
        this.cages = new ArrayList<>();
    }

    public void addCage(Cage cage) {
        if (!this.cages.contains(cage)) {
            this.cages.add(cage);
        }
    }

    public void deleteAllCages() {
        for (Cage cage : this.cages) {
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
