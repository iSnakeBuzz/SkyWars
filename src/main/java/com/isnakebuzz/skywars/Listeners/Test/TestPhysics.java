package com.isnakebuzz.skywars.Listeners.Test;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

public class TestPhysics implements Listener {

    private SkyWars plugin;

    public TestPhysics(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent e) {
        Material mat = e.getBlock().getType();

        String splitter = ",";

        StringBuilder toString = new StringBuilder("{");

        toString.append("eventName=").append(e.getEventName()).append(splitter);
        toString.append("isCancelled=").append(e.isCancelled()).append(splitter);
        toString.append("isAsync=").append(e.isAsynchronous()).append(splitter);
        toString.append("changedType=").append(e.getChangedType().toString()).append(splitter);
        toString.append("blockType=").append(e.getBlock().getType().toString());

        toString.append("}");

        plugin.debug(toString.toString());

        if (mat == Material.STATIONARY_WATER) {
            //do what you need here
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent e) {
        Block block = e.getToBlock();

        String splitter = ",";

        StringBuilder toString = new StringBuilder("{");

        toString.append("eventName=").append(e.getEventName()).append(splitter);
        toString.append("isCancelled=").append(e.isCancelled()).append(splitter);
        toString.append("isAsync=").append(e.isAsynchronous()).append(splitter);
        toString.append("changedType=").append(e.getToBlock().getType().toString()).append(splitter);
        toString.append("blockType=").append(e.getBlock().getType().toString());

        toString.append("}");

        plugin.debug(toString.toString());

        if (block.getType() == Material.WATER) {
            //do what you need here.
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
