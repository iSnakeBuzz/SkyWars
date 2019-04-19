package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologram;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologramAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    private Main plugin;
    private ArrayList<TruenoHologram> diamonds_holograms;
    private ArrayList<TruenoHologram> emerald_holograms;

    public HologramManager(Main plugin) {
        this.plugin = plugin;
        this.diamonds_holograms = new ArrayList<>();
        this.emerald_holograms = new ArrayList<>();
    }

    public void createHologram(Location loc, ArrayList<String> lines) {
        TruenoHologram hologram = TruenoHologramAPI.getNewHologram();
        assert hologram != null;
        hologram.setupWorldHologram(loc, parseList(lines), 0.26);
        hologram.display();
    }

    public boolean createItemFloating(Type type, Location loc) {
        TruenoHologram hologram = TruenoHologramAPI.getNewHologram();
        if (type.equals(Type.DIAMOND)) {
            assert hologram != null;
            hologram.setupFloatingItem(loc, Material.DIAMOND_BLOCK);
            hologram.spawnItemFloating(plugin);
        } else if (type.equals(Type.EMERALD)) {
            assert hologram != null;
            hologram.setupFloatingItem(loc, Material.EMERALD_BLOCK);
            hologram.spawnItemFloating(plugin);
        }
        return true;
    }

    public boolean createHologram(Type type, Location loc, List<String> lines, double linedistance) {
        TruenoHologram hologram = TruenoHologramAPI.getNewHologram();
        if (type.equals(Type.DIAMOND)) {
            if (!diamonds_holograms.contains(hologram)) {
                hologram.setupWorldHologram(loc, parseList(lines), linedistance);
                hologram.display();
                this.diamonds_holograms.add(hologram);
                return true;
            } else {
                return false;
            }
        } else if (type.equals(Type.EMERALD)) {
            if (!emerald_holograms.contains(hologram)) {
                assert hologram != null;
                hologram.setupWorldHologram(loc, parseList(lines), linedistance);
                hologram.display();
                this.emerald_holograms.add(hologram);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public ArrayList<TruenoHologram> getDHolograms() {
        return diamonds_holograms;
    }

    public ArrayList<TruenoHologram> getEHolograms() {
        return emerald_holograms;
    }

    public void updateLines(Type type, List<String> text) {
        if (type.equals(Type.DIAMOND)) {
            for (TruenoHologram truenoHologram : this.diamonds_holograms) {
                truenoHologram.update(parseList(text));
            }
        } else if (type.equals(Type.EMERALD)) {
            for (TruenoHologram truenoHologram : this.emerald_holograms) {
                truenoHologram.update(parseList(text));
            }
        }
    }

    public void updateLine(Type type, int index, String text) {
        if (type.equals(Type.DIAMOND)) {
            for (TruenoHologram truenoHologram : this.diamonds_holograms) {
                truenoHologram.updateLine(index, c(text));
            }
        } else if (type.equals(Type.EMERALD)) {
            for (TruenoHologram truenoHologram : this.emerald_holograms) {
                truenoHologram.updateLine(index, c(text));
            }
        }
    }

    private ArrayList<String> parseList(List<String> list) {
        ArrayList<String> list2 = new ArrayList<>();
        for (String string : list) {
            list2.add(c(string));
        }
        return list2;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

    public enum Type {
        EMERALD, DIAMOND
    }
}
