package com.isnakebuzz.skywars.Schematics;

import com.isnakebuzz.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public class SnakeSchem implements ISnakeSchem {

    private Main plugin;
    private Location pasteLocation;

    //Utils
    private List<Vector> locations;
    private List<Integer> blocks;
    private List<Byte> blockIDs;

    public SnakeSchem(Main plugin, Location pasteLoc) {
        this.plugin = plugin;
        this.pasteLocation = pasteLoc;
    }

    protected void setBlockIDs(List<Byte> blockIDs) {
        this.blockIDs = blockIDs;
    }

    protected void setBlocks(List<Integer> blocks) {
        this.blocks = blocks;
    }

    protected void setLocations(List<Vector> locations) {
        this.locations = locations;
    }

    @Override
    public void paste() {
        int i = 0;
        for (Vector vector : this.locations) {
            Block b = pasteLocation.clone().add(vector).getBlock();
            int finalI = i;
            Bukkit.getScheduler().runTask(plugin, () -> b.setTypeIdAndData(blocks.get(finalI), blockIDs.get(finalI), true));
            if (i < this.locations.size()) i++;
        }
    }

    @Override
    public void undo() {
        this.locations.forEach(vector -> Bukkit.getScheduler().runTask(plugin, () -> pasteLocation.clone().add(vector).getBlock().setType(Material.AIR)));
    }
}
