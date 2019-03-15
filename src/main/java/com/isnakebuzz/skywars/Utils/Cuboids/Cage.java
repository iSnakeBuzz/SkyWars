package com.isnakebuzz.skywars.Utils.Cuboids;

import com.google.common.base.Preconditions;
import com.isnakebuzz.skywars.Main;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class Cage {

    //Public assets
    private Main plugin;
    private String schematicName;
    private Location location;
    private Vector position;

    //Internal usages
    private EditSession editSession;

    public Cage(Main plugin, Location location, String schematicName) {
        this.plugin = plugin;
        this.location = location;
        this.schematicName = schematicName;
    }

    public void paste() {
        this.position = BukkitUtil.toVector(location);
        File schematic = plugin.getCagesManager().getCage(this.schematicName);
        try {
            Preconditions.checkNotNull(location, "Location is null, please contact with developer");
            Preconditions.checkNotNull(schematic, "A cage is null, please contact with developer");
            editSession = ClipboardFormat.findByFile(schematic)
                    .load(schematic)
                    .paste(
                            new BukkitWorld(this.location.getWorld())
                            , position
                            , true
                            , true
                            , null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void undo() {
        this.editSession.undo(editSession);
    }

}
