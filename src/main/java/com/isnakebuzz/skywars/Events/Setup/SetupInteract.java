package com.isnakebuzz.skywars.Events.Setup;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.LocUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class SetupInteract implements Listener {

    private Main plugin;

    public SetupInteract(Main plugin) {
        this.plugin = plugin;
    }

    int islandschest = 1;
    int centerchest = 1;

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e) throws IOException {
        if (e.getClickedBlock() != null && e.getItem() != null) {
            ItemStack itemStack = e.getItem();
            Block b = e.getClickedBlock();

            if (itemStack.getItemMeta().hasDisplayName()) {
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(c("&aWand"))) {
                    Player p = e.getPlayer();

                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        plugin.getArenaSetup().setSettings(p, "LobbyArea.1", LocUtils.locToString(b.getLocation()));
                    } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        plugin.getArenaSetup().setSettings(p, "LobbyArea.2", LocUtils.locToString(b.getLocation()));
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

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
