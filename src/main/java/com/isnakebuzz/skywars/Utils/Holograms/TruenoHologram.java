package com.isnakebuzz.skywars.Utils.Holograms;

import org.bukkit.*;
import java.util.*;
import org.bukkit.entity.*;

public interface TruenoHologram
{
    void setupWorldHologram(final Location p0, final ArrayList<String> p1);
    
    void setupPlayerHologram(final Player p0, final Location p1, final ArrayList<String> p2);
    
    Location getLocation();
    
    Player getPlayer();
    
    ArrayList<Integer> getEntitiesIds();
    
    void setDistanceBetweenLines(final Double p0);
    
    void display();
    
    void update(final ArrayList<String> p0);
    
    void updateLine(final int p0, final String p1);
    
    void removeLine(final int p0);
    
    void delete();
}
