package com.isnakebuzz.skywars.Listeners.VoteEvents;

import com.isnakebuzz.skywars.Main;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class SoftBlocks implements Listener {

    private Main plugin;

    public SoftBlocks(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void Explode(ProjectileHitEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Snowball || entity instanceof Egg || entity instanceof Arrow) {
            Location loc = entity.getLocation();
            Vector vec = entity.getVelocity();
            Location loc2 = new Location(loc.getWorld(), loc.getX() + vec.getX(), loc.getY() + vec.getY(), loc.getZ() + vec.getZ());
            loc2.getBlock().setType(Material.AIR);
            Bukkit.getWorld("world").playSound(loc2, Sound.CHICKEN_EGG_POP, 1, 1);
            if (entity instanceof Arrow){
                entity.remove();
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
