package com.isnakebuzz.skywars.Utils.World;

import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.LocUtils;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FaweUtils {

    private SkyWars plugin;

    public FaweUtils(SkyWars plugin) {
        this.plugin = plugin;
    }

    public void restartWorld() {
        File schematic = new File(plugin.getDataFolder(), "Arena.schematic");
        World world = plugin.getSkyWarsArena().getLobbyLocation().getWorld();
        Location location = LocUtils.stringToLoc(plugin.getConfig("Extra/Arena").getString("Schematic Center"));
        assert location != null;
        Vector position = BukkitUtil.toVector(location);

        try {
            EditSession editSession = Objects.requireNonNull(ClipboardFormat.findByFile(schematic)).load(schematic).paste(new BukkitWorld(world), position, false, true, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
