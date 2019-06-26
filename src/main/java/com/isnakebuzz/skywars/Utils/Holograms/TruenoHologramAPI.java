package com.isnakebuzz.skywars.Utils.Holograms;

import java.util.*;

import com.isnakebuzz.skywars.Utils.Holograms.nms.TruenoHologram_v1_8_r3;
import org.bukkit.*;

import java.util.logging.*;

import org.bukkit.ChatColor;

public class TruenoHologramAPI {
    private static ArrayList<TruenoHologram> holograms;
    private static String version;

    public static ArrayList<TruenoHologram> getHolograms() {
        return TruenoHologramAPI.holograms;
    }

    private static void setupVersion() {
        try {
            TruenoHologramAPI.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public static TruenoHologram getNewHologram() {
        if (TruenoHologramAPI.version == null) {
            setupVersion();
        }
        if (TruenoHologramAPI.version.equals("v1_8_R3")) {
            final TruenoHologram holo = new TruenoHologram_v1_8_r3();
            TruenoHologramAPI.holograms.add(holo);
            return holo;
        }
        Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Unsopported server version.");
        return null;
    }

    static {
        TruenoHologramAPI.holograms = new ArrayList<TruenoHologram>();
    }
}
