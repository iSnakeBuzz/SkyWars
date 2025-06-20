package com.isnakebuzz.skywars.Listeners.Lobby;

import com.isnakebuzz.skywars.Inventory.MenuManager.MenuCreator;
import com.isnakebuzz.skywars.Inventory.Others.KitsMenu;
import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.skywars.Utils.Statics;
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
import java.util.Random;
import java.util.Set;

public class LobbyItems implements Listener {

    private SkyWars plugin;

    public LobbyItems(SkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void InventoryInteract(InventoryClickEvent e) {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Lobby").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        if (key == null | key.size() < 1) return;
        Player p = (Player) e.getWhoClicked();
        for (String item : key) {
            String path = "Lobby." + item + ".";
            String _item = config.getString(path + "item");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            ItemStack itemStack = ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore);

            if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;

            if (e.getCurrentItem().equals(itemStack)) {
                ACTIONS(p, (_action.split(":")[0] != null) ? _action.split(":")[0] : "", (_action.split(":").length > 1) ? _action.split(":")[1] : "");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryInteract(PlayerInteractEvent e) {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Lobby").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        assert key != null;
        if (key.size() < 1) return;
        for (String item : key) {
            String path = "Lobby." + item + ".";
            String _item = config.getString(path + "item");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            ItemStack itemStack = ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore);

            if (e.getItem() == null || e.getItem().getItemMeta() == null) return;

            if (e.getItem().equals(itemStack)) {
                ACTIONS(e.getPlayer(), (_action.split(":")[0] != null) ? _action.split(":")[0] : "", (_action.split(":").length > 1) ? _action.split(":")[1] : "");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }

    private void ACTIONS(Player player, String action, String args) {
        if (action.equalsIgnoreCase("menu")) {
            if (args.equalsIgnoreCase("kits")) {
                new KitsMenu(plugin, args, player).open();
            } else {
                new MenuCreator(player, plugin, args).open();
            }
        } else if (action.equalsIgnoreCase("cmd")) {
            String cmd = "/" + args;
            player.chat(cmd);
        } else if (action.equalsIgnoreCase("leave")) {
            String lobby = Statics.lobbies.get(new Random().nextInt(Statics.lobbies.size()));
            PacketsAPI.connect(plugin, player, lobby);
        } else if (action.equalsIgnoreCase("playAgain")) {
            player.sendMessage(c("&cThat function is under development, please wait for new updates :)"));
        } else {
            player.sendMessage(c("&cThat action does't exist, please contact with administrator :)"));
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
