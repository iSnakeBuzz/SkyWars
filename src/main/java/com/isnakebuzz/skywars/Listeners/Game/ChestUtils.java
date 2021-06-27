package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Calls.Events.ChestCloseEvent;
import com.isnakebuzz.skywars.Calls.Events.ChestOpenEvent;
import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class ChestUtils implements Listener {

    private SkyWars plugin;

    public ChestUtils(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void chestBreak(BlockBreakEvent e) {
        if (e.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) e.getBlock().getState();
            plugin.getChestRefillManager().updateChest(chest, true, false);
            plugin.debug("Breaked chest :)");
        }
    }

    @EventHandler
    public void chestExplode(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            if (block.getState() instanceof Chest) {
                Chest chest = (Chest) block.getState();
                plugin.getChestRefillManager().updateChest(chest, true, false);
                plugin.debug("Exploded chest :)");
                break;
            }
        }
    }

    @EventHandler
    public void InventoryOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        // plugin.debug("Player opened: " + e.getInventory().getName());
        if (e.getInventory().getName().equalsIgnoreCase("container.chest")) {
            if (e.getInventory().getType().equals(InventoryType.CHEST)) {
                Chest chest = (Chest) e.getInventory().getHolder();

                //player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 0);
                plugin.getChestRefillManager().updateChest(chest, false, true);
            }
        }
    }

    @EventHandler
    public void InventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        // plugin.debug("Player closed: " + e.getInventory().getName());
        if (e.getInventory().getName().equalsIgnoreCase("container.chest")) {
            if (e.getInventory().getType().equals(InventoryType.CHEST)) {
                Chest chest = (Chest) e.getInventory().getHolder();

                //player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 0);
                plugin.getChestRefillManager().updateChest(chest, false, false);
            }
        }
    }


    @EventHandler
    public void InventoryOpen_CALL(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getInventory().getType().equals(InventoryType.CHEST)) {
            Chest chest = (Chest) e.getInventory().getHolder();
            if (chest != null) {
                //Calling ChestOpenEvent
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new ChestOpenEvent(player, chest)));
            }
        }
    }


    @EventHandler
    public void InventoryClose_CALL(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getInventory().getType().equals(InventoryType.CHEST)) {
            Chest chest = (Chest) e.getInventory().getHolder();
            if (chest != null) {
                //Calling ChestOpenEvent
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> Bukkit.getPluginManager().callEvent(new ChestCloseEvent(player, chest)));
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
