package com.isnakebuzz.skywars.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

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

}
