package com.isnakebuzz.skywars.Utils;

import com.isnakebuzz.skywars.Schematics.Utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocUtils {

    public static String locToString(Location l) {
        return String
                .valueOf(new StringBuilder(String.valueOf(l.getWorld().getName())).append(":").append(l.getBlockX())
                        .toString())
                + ":" + String.valueOf(l.getBlockY()) + ":" + String.valueOf(l.getBlockZ()) + ":"
                + String.valueOf(l.getYaw()) + ":" + String.valueOf(l.getPitch());
    }

    public static Location stringToLoc(String s) {
        Location l = null;
        try {
            World world = Bukkit.getWorld(s.split(":")[0]);
            Double x = Double.parseDouble(s.split(":")[1]);
            Double y = Double.parseDouble(s.split(":")[2]);
            Double z = Double.parseDouble(s.split(":")[3]);
            Float p = Float.parseFloat(s.split(":")[4]);
            Float y2 = Float.parseFloat(s.split(":")[5]);

            return l = new Location(world, x + 0.5, y, z + 0.5, p, y2);
        } catch (Exception ex) {
        }
        return l;
    }

    public static Location fixLocation(Location loc) {
        return new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5);
    }

    public static List<Location> stringsToLocs(List<String> locs) {
        return locs.stream().map(LocUtils::stringToLoc).collect(Collectors.toList());
    }

    public static ArrayList<Block> getBlocks(Location loc1, Location loc2) {
        return new Cuboid(loc1, loc2).getBlocks();
    }

    public static List<Location> getLocsFromBlocks(List<Block> blocks) {
        return blocks.stream().map(Block::getLocation).collect(Collectors.toList());
    }

    public static List<Integer> getBlocksIDS(List<Block> blocks) {
        return blocks.stream().map(Block::getTypeId).collect(Collectors.toList());
    }

    public static List<Byte> getBlocksDATA(List<Block> blocks) {
        return blocks.stream().map(Block::getData).collect(Collectors.toList());
    }

}
