package com.isnakebuzz.skywars.Utils.Holograms;

import com.isnakebuzz.skywars.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface TruenoHologram {

    /**
     *
     *
     * @author el_trueno
     *
     *
     **/


    /**
     * Set the new hologram visible to all the players
     *
     * @param loc   Hologram location
     * @param lines Lines that will be shown in the hologram. TOP-BUTTOM
     */
    public void setupWorldHologram(Location loc, List<String> lines, double linedistance);

    public void setupFloatingItem(Location loc, Material item);

    public void spawnItemFloating(Main plugin);

    /**
     * Set the new hologram visible only to a specific player
     *
     * @param player Player who will see the hologram
     * @param loc    Hologram location
     * @param lines  Lines that will be shown in the hologram. TOP-BUTTOM
     */
    public void setupPlayerHologram(Player player, Location loc, ArrayList<String> lines);

    /**
     * Return hologram's location
     */
    public Location getLocation();

    /**
     * Return hologram's player (only if is a player hologram)
     */
    public Player getPlayer();

    /**
     * Set the distance between the hologram lines. Default: 0.30
     *
     * @param distance the distance
     */
    public void setDistanceBetweenLines(Double distance);

    /**
     * Spawn the hologram
     */
    public void display();

    /**
     * Update the hologram lines. Only will be updated the lines that have changed.
     * If the original hologram has more lines, the rest of the lines will be eliminated.
     * If the original hologram has less lines than the new list, the new lines will be created.
     *
     * @param lines Text lines.
     */
    @Deprecated
    public void update(ArrayList<String> lines);

    /**
     * Update the hologram lines. Only will be updated the lines that have changed.
     * If the original hologram has more lines, the rest of the lines will be eliminated.
     * If the original hologram has less lines than the new list, the new lines will be created.
     *
     * @param lines Text lines.
     */
    public void newUpdate(List<String> lines);

    /**
     * Update specific line
     *
     * @param index index of the line to update
     * @param text  the new text
     */
    public void updateLine(int index, String text);

    /**
     * Remove a specific line
     *
     * @param index the index of the line to remove
     */
    public void removeLine(int index);

    /**
     * Delete the hologram
     */
    public void delete();

}
