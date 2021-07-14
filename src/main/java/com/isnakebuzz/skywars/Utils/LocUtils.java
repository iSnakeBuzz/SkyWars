package com.isnakebuzz.skywars.Utils;

import com.isnakebuzz.skywars.Schematics.Utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocUtils {

    public static String locToString(Location l) {
        return l.getWorld().getName() + ":" + l.getBlockX()
                + ":" + l.getBlockY() + ":" + l.getBlockZ() + ":"
                + l.getYaw() + ":" + l.getPitch();
    }

    public static SnakeLocation stringToLoc(String s) {
        String world = s.split(":")[0];
        double x = Double.parseDouble(s.split(":")[1]);
        double y = Double.parseDouble(s.split(":")[2]);
        double z = Double.parseDouble(s.split(":")[3]);
        float p = Float.parseFloat(s.split(":")[4]);
        float y2 = Float.parseFloat(s.split(":")[5]);

        return new SnakeLocation(world, x + 0.5, y, z + 0.5, p, y2);
    }

    public static void teleport(Player player, Location spawnLocation, Location lobbyLocation) {
        Vector dirBetweenLocations = lobbyLocation.toVector().subtract(spawnLocation.toVector());

        Location newLocation = spawnLocation.clone();
        newLocation.setDirection(dirBetweenLocations);
        newLocation.setPitch(0);

        player.teleport(newLocation);
    }

    public static Location fixLocation(Location loc) {
        return new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5);
    }

    public static List<SnakeLocation> stringsToLocs(List<String> locs) {
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
