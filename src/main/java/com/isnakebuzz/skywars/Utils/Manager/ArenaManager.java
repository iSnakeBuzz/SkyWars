package com.isnakebuzz.skywars.Utils.Manager;

import com.google.common.collect.Sets;
import com.isnakebuzz.skywars.Arena.SkyWarsArena;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Console;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class ArenaManager {

    private final SkyWars skywars;
    private final Set<SkyWarsArena> arenas;

    public ArenaManager(SkyWars skywars) {
        this.skywars = skywars;
        this.arenas = Sets.newConcurrentHashSet();
    }

    public void addArena(SkyWarsArena skyWarsArena) {
        this.arenas.add(skyWarsArena);
    }

    public void removeArena(SkyWarsArena skyWarsArena) {
        this.arenas.remove(skyWarsArena);
    }

    public void loadArenas() {
        this.arenas.clear();

        File folder = new File(skywars.getDataFolder(), "Arenas");
        if (folder.mkdir()) Console.info("Created Arenas Folder");
        if (folder.listFiles() == null) {
            Console.info("No Arena files loaded.");
            return;
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            this.addArena(new SkyWarsArena(skywars, config));
        }

    }

    public Set<SkyWarsArena> getArenas() {
        return arenas;
    }

    public SkyWarsArena getRandomArena() {
        int size = arenas.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (SkyWarsArena obj : arenas)
            if (i == item)
                return obj;

        return null;
    }

}
