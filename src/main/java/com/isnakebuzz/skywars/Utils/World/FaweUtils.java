package com.isnakebuzz.skywars.Utils.World;

import com.boydti.fawe.object.schematic.Schematic;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.LocUtils;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.Transform;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class FaweUtils {

    private Main plugin;

    public FaweUtils(Main plugin) {
        this.plugin = plugin;
    }

    public void restartWorld() {
        File schematic = new File(plugin.getDataFolder(), "Arena.schematic");
        World world = plugin.getSkyWarsArena().getLobbyLocation().getWorld();
        Location location = LocUtils.stringToLoc(plugin.getConfig("Extra/Arena").getString("Schematic Center"));
        Vector position = BukkitUtil.toVector(location);

        try {
            EditSession editSession = ClipboardFormat.findByFile(schematic).load(schematic).paste(new BukkitWorld(world), position, false, true, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
