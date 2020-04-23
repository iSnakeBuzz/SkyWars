package com.isnakebuzz.skywars.Listeners.Setup;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.LocUtils;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class SetupInteract implements Listener {

    int islandschest = 1;
    int centerchest = 1;
    private Main plugin;

    public SetupInteract(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e) throws IOException {
        if (e.getClickedBlock() != null && e.getItem() != null) {
            ItemStack itemStack = e.getItem();
            Block b = e.getClickedBlock();

            if (itemStack.getItemMeta().hasDisplayName()) {
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(c("&aLobby Area"))) {
                    Player p = e.getPlayer();

                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        plugin.getArenaSetup().setSettings(p, "LobbyArea.1", LocUtils.locToString(b.getLocation()));
                        e.setCancelled(true);
                    } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        plugin.getArenaSetup().setSettings(p, "LobbyArea.2", LocUtils.locToString(b.getLocation()));
                        e.setCancelled(true);
                    }
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(c("&6Cagenator"))) {
                    Player p = e.getPlayer();

                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        Statics.cage_loc2 = b.getLocation();
                        p.sendMessage(c("&a&lSkyWars &8| &eHas been selected pos#2"));
                        e.setCancelled(true);
                    } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        Statics.cage_loc1 = b.getLocation();
                        p.sendMessage(c("&a&lSkyWars &8| &eHas been selected pos#1"));
                        e.setCancelled(true);
                    }
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(c("&aCenter Chests"))) {
                    Player p = e.getPlayer();
                    if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && b.getType().equals(Material.CHEST)) {
                        plugin.getArenaSetup().setSettings(p, "CenterChest." + centerchest, LocUtils.locToString(b.getLocation()));
                        centerchest++;
                    }
                } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(c("&aIsland Chests"))) {
                    Player p = e.getPlayer();
                    if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && b.getType().equals(Material.CHEST)) {
                        plugin.getArenaSetup().setSettings(p, "IslandChest." + islandschest, LocUtils.locToString(b.getLocation()));
                        islandschest++;
                    }
                }
            }

        }
    }

    /*Placing beacons for spawns - Commands sucks*/
    @EventHandler
    public void spawnPlaced(BlockPlaceEvent e) {
        /*Accepts only beacons*/
        if (e.getBlock().getType() != Material.BEACON) return;

        try {
            plugin.getArenaSetup().addSpawn(e.getPlayer(), e.getBlock().getLocation());
        } catch (IOException ex) {
            ex.printStackTrace();
            e.getPlayer().sendMessage(c("&eNo wacho, rompiste todo la concha de tu madre &c(Error: " + ex.getMessage() + ")"));
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
