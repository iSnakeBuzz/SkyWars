package com.isnakebuzz.skywars.Utils.Cuboids;

import com.isnakebuzz.skywars.Schematics.SnakeSchem;
import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.util.List;

public class Cage extends SnakeSchem {

    public Cage(SkyWars plugin, Location location, String schematicName) {
        super(plugin, location);
        FileConfiguration config = YamlConfiguration.loadConfiguration(plugin.getCagesManager().getCage(schematicName));
        this.setBlocks(config.getIntegerList("Blocks"));
        this.setBlockIDs(config.getByteList("Block IDS"));
        this.setLocations(((List<Vector>) config.get("Locations")));
    }


}