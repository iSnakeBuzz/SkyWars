package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

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
            plugin.debug("Breaked chest :)");
        }
    }

    @EventHandler
    public void chestExplode(BlockExplodeEvent e) {
        if (e.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) e.getBlock().getState();
            plugin.getChestRefillManager().updateChest(chest, true, false);
            plugin.debug("Exploded chest :)");
        }
    }

    /*@EventHandler
    public void interactWithChest(PlayerInteractEvent e) {
       /* Player player = e.getPlayer();
        if (e.getClickedBlock() == null) {
            return;
        }

        Block block = e.getClickedBlock();
        if (block.getState() instanceof Chest && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Chest chest = (Chest) block.getState();
            plugin.getChestRefillManager().updateChest(chest, false, true);
        } OLD CODE
    } REMOVED FOR MORE OPTIMIZED METHODE*/

    @EventHandler
    public void InventoryOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        plugin.debug("Player opened: " + e.getInventory().getName());
        if (e.getInventory().getName().equalsIgnoreCase("container.chest")) {
            if (e.getInventory().getType().equals(InventoryType.CHEST)) {
                Chest chest = (Chest) e.getInventory().getHolder();

                //player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 0);
                plugin.getChestRefillManager().updateChest(chest, false, true);
            }
        }
    }

    @EventHandler
    public void InventoryCLose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        plugin.debug("Player closed: " + e.getInventory().getName());
        if (e.getInventory().getName().equalsIgnoreCase("container.chest")) {
            if (e.getInventory().getType().equals(InventoryType.CHEST)) {
                Chest chest = (Chest) e.getInventory().getHolder();

                //player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 0);
                plugin.getChestRefillManager().updateChest(chest, false, false);
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
