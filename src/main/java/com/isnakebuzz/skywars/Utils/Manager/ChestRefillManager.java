package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Enums.ChestType;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologram;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologramAPI;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
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

    public ChestRefillManager(Main plugin) {
        this.plugin = plugin;

        this.hologramHashMap = new HashMap<>();
    }

    public void updateChest(Chest chest, boolean broken, boolean open) {
        if (plugin.getSkyWarsArena().getCenterChestLocs().contains(chest.getLocation())
                || plugin.getSkyWarsArena().getIslandChestLocs().contains(chest.getLocation())) {
            checkChest(chest, broken);
            playChestAction(chest, open);
        }
    }

    private void checkChest(Chest chest, boolean broken) {
        if (broken && hologramHashMap.containsKey(chest.getLocation())) {
            hologramHashMap.remove(chest.getLocation());
            this.removeChest(chest);
            return;
        }

        if (this.hologramHashMap.containsKey(chest.getLocation())) {
            if (this.chestEmpty(chest.getInventory())) {
                ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Holograms.ChestRefill");

                TruenoHologram hologram = this.hologramHashMap.get(chest.getLocation());
                List<String> hologramInfo = transformTo(lang.getStringList("Empty"));

                hologram.newUpdate(hologramInfo);
            } else {
                ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Holograms.ChestRefill");

                TruenoHologram hologram = this.hologramHashMap.get(chest.getLocation());
                List<String> hologramInfo = transformTo(lang.getStringList("Normal"));

                hologram.newUpdate(hologramInfo);
            }
        } else {
            ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Holograms.ChestRefill");

            TruenoHologram hologram = TruenoHologramAPI.getNewHologram();
            List<String> hologramInfo = transformTo(lang.getStringList("Normal"));
            Location loc = chest.getLocation().clone();
            loc.setY(loc.getY() + .5);

            assert hologram != null;
            hologram.setupWorldHologram(loc, hologramInfo, 0.1);

            this.hologramHashMap.put(chest.getLocation(), hologram);
        }
    }

    private void playChestAction(Chest chest, boolean open) {
        Location location = chest.getLocation();
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
        TileEntityChest tileChest = (TileEntityChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.w(), 1, open ? 1 : 0);
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

    private List<String> transformTo(List<String> chars) {
        List<String> transformed = new ArrayList<>();
        for (String msg : chars) {
            String timer = plugin.getTimerManager().transformToDate(plugin.getSkyWarsArena().getRefillTimer());
            transformed.add(c(msg.replaceAll("%timer%", timer)));
        }
        return transformed;
    }

    public void reset() {
        for (TruenoHologram hologram : this.hologramHashMap.values()) {
            hologram.delete();
        }
    }

    private void removeChest(Chest chest) {
        plugin.getSkyWarsArena().getIslandChestLocs().remove(chest.getLocation());
        plugin.getSkyWarsArena().getCenterChestLocs().remove(chest.getLocation());
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
