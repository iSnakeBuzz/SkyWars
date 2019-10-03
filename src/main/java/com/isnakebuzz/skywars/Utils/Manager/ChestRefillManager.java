package com.isnakebuzz.skywars.Utils.Manager;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.QueueEvents.QueueEvent;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologram;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologramAPI;
import com.isnakebuzz.skywars.Utils.LocUtils;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChestRefillManager {

    private Main plugin;
    private HashMap<Location, TruenoHologram> hologramHashMap;
    private Boolean actived;

    public ChestRefillManager(Main plugin) {
        this.plugin = plugin;

        this.hologramHashMap = new HashMap<>();
        this.actived = false;
    }

    public void setActived(Boolean actived) {
        this.actived = actived;
    }

    public Boolean isActived() {
        return actived;
    }

    public void updateChest(Chest chest, boolean broken, boolean open) {
        if (chest == null) return;
        String locFixed = LocUtils.locToString(chest.getLocation());


        plugin.debug("Broken?: " + broken + " " + locFixed);
        if (plugin.getSkyWarsArena().getCenterChestLocs().contains(LocUtils.stringToLoc(locFixed))
                || plugin.getSkyWarsArena().getIslandChestLocs().contains(LocUtils.stringToLoc(locFixed))) {
            if (!this.isActived()) return;
            checkChest(chest, broken);
            playChestAction(chest, open);
            plugin.debug("Checking chest");
        }
    }

    private void checkChest(Chest chest, boolean broken) {
        Location locFixed = LocUtils.fixLocation(chest.getLocation());

        if (broken) {
            if (hologramHashMap.containsKey(locFixed)) hologramHashMap.get(locFixed).delete();
            hologramHashMap.remove(locFixed);
            this.removeChest(chest);
            plugin.debug("Removing hologram by breaked");
            return;
        }

        if (this.hologramHashMap.containsKey(locFixed)) {
            if (this.chestEmpty(chest.getInventory())) {
                ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Holograms.ChestRefill");

                TruenoHologram hologram = this.hologramHashMap.get(locFixed);
                ArrayList<String> hologramInfo = Lists.newArrayList();
                hologramInfo.add(c(lang.getString("Name")));
                hologramInfo.add(c(lang.getString("Empty")));
                plugin.debug(hologramInfo.toString());

                hologram.update(hologramInfo);
            } else {
                ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Holograms.ChestRefill");

                TruenoHologram hologram = this.hologramHashMap.get(locFixed);
                ArrayList<String> hologramInfo = Lists.newArrayList();

                hologramInfo.add(c(lang.getString("Name")));
                hologramInfo.add("");

                plugin.debug(hologramInfo.toString());

                hologram.update(hologramInfo);
            }
        } else {
            ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Holograms.ChestRefill");

            TruenoHologram hologram = TruenoHologramAPI.getNewHologram();
            List<String> hologramInfo = Lists.newArrayList();

            hologramInfo.add(c(lang.getString("Name")));
            hologramInfo.add("");

            Location loc = locFixed.clone();
            loc.setY(loc.getY() - 0.1);

            assert hologram != null;
            hologram.setupWorldHologram(loc, new ArrayList<>(hologramInfo));
            hologram.setDistanceBetweenLines(0.3);
            plugin.debug("Hologram created: " + hologram.toString());
            hologram.display();

            this.hologramHashMap.put(locFixed, hologram);
        }
    }

    private void playChestAction(Chest chest, boolean open) {
        Location locFixed = LocUtils.fixLocation(chest.getLocation());

        World world = ((CraftWorld) locFixed.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(locFixed.getX(), locFixed.getY(), locFixed.getZ());
        TileEntityChest tileChest = (TileEntityChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.w(), 1, open ? 1 : 0);
    }

    public void update(String name) {
        if (!this.hologramHashMap.isEmpty())
            this.hologramHashMap.values().forEach(holo -> holo.updateLine(1, name));
    }

    private boolean chestEmpty(Inventory inv) {
        boolean empty = true;
        for (ItemStack s : inv) {
            if (s != null) {
                empty = !true;
                break;
            }
        }
        return empty;
    }

    public void reset() {
        for (TruenoHologram hologram : this.hologramHashMap.values()) {
            hologram.delete();
        }

        this.hologramHashMap.clear();
    }

    private void removeChest(Chest chest) {
        Location locFixed = LocUtils.fixLocation(chest.getLocation());

        plugin.getSkyWarsArena().getIslandChestLocs().remove(locFixed);
        plugin.getSkyWarsArena().getCenterChestLocs().remove(locFixed);
    }

    private String c(String s) {
        String timer = "";

        if (plugin.getEventsManager().getActualQueue() != null) {
            QueueEvent queueEvent = plugin.getEventsManager().getActualQueue();
            timer = plugin.getTimerManager().transformToDate(queueEvent.getEventTime());
        }
        return ChatColor.translateAlternateColorCodes('&', s.replaceAll("%timer%", timer));
    }

}
