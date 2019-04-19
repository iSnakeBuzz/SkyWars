package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

public class ChestUtils implements Listener {

    private Main plugin;

    public ChestUtils(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void chestBreak(BlockBreakEvent e) {
        if (e.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) e.getBlock().getState();
            plugin.getChestRefillManager().updateChest(chest, true, false);
        }
    }

    @EventHandler
    public void interactWithChest(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        Block block = e.getClickedBlock();
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            plugin.getChestRefillManager().updateChest(chest, false, true);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void InventoryOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getPlayer().getInventory().getType().equals(InventoryType.CHEST)) {
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 0);
        }
    }

    @EventHandler
    public void InventoryCLose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getPlayer().getInventory().getType().equals(InventoryType.CHEST)) {
            player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 0);
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
