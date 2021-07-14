package com.isnakebuzz.skywars.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@AllArgsConstructor
@Data
public class SnakeLocation implements Cloneable {

    private String world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public SnakeLocation(Location loc) {
        this(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    public SnakeLocation(String world, double x, double y, double z) {
        this(world, x, y, z, 90, 0);
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    @SneakyThrows
    @Override
    public SnakeLocation clone() {
        return (SnakeLocation) super.clone();
    }
}
