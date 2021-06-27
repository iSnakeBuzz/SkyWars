package com.isnakebuzz.skywars.Listeners.Lobby;

import com.isnakebuzz.skywars.Inventory.MenuManager.MenuCreator;
import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class SpectatorItems implements Listener {

    private SkyWars plugin;

    public SpectatorItems(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void InventoryInteract(InventoryClickEvent e) {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Spectator").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        if (key == null | key.size() < 1) return;
        Player p = (Player) e.getWhoClicked();
        for (String item : key) {
            String path = "Spectator." + item + ".";
            String _item = config.getString(path + "item");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            ItemStack itemStack = ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore);

            if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;

            if (e.getCurrentItem().equals(itemStack)) {
                if (_action.split(":")[0].equalsIgnoreCase("menu")) {
                    new MenuCreator(p, plugin, _action.split(":")[1]).open();
                } else if (_action.split(":")[0].equalsIgnoreCase("cmd")) {
                    String cmd = "/" + _action.split(":")[1];
                    p.chat(cmd);
                } else if (_action.split(":")[0].equalsIgnoreCase("leave")) {

                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryInteract(PlayerInteractEvent e) {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Spectator").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        if (key == null | key.size() < 1) return;
        for (String item : key) {
            String path = "Spectator." + item + ".";
            String _item = config.getString(path + "item");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            ItemStack itemStack = ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore);

            if (e.getItem() == null || e.getItem().getItemMeta().equals(null)) return;

            if (e.getItem().equals(itemStack)) {
                if (_action.split(":")[0].equalsIgnoreCase("menu")) {
                    new MenuCreator(e.getPlayer(), plugin, _action.split(":")[1]).open();
                } else if (_action.split(":")[0].equalsIgnoreCase("cmd")) {
                    String cmd = "/" + _action.split(":")[1];
                    e.getPlayer().chat(cmd);
                } else if (_action.split(":")[0].equalsIgnoreCase("leave")) {

                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryDrag(InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(e.getWhoClicked().getUniqueId());
            if (skyPlayer.isSpectator()) {
                e.setCancelled(true);
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
