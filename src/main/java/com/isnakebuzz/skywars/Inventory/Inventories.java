package com.isnakebuzz.skywars.Inventory;

import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

public class Inventories {

    private Main plugin;

    public Inventories(Main plugin) {
        this.plugin = plugin;
    }

    public void setLobbyInventory(Player p) {
        PlayerUtils.clean(p, GameMode.ADVENTURE, false, false);
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Lobby").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        if (key == null | key.size() < 1) return;

        for (String item : key) {
            String path = "Lobby." + item + ".";
            String _item = config.getString(path + "item");
            int _slot = config.getInt(path + "slot");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            p.getInventory().setItem(_slot, ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore));
        }
        p.updateInventory();
    }

    public void setSpectInventory(Player p) {
        PlayerUtils.clean(p, GameMode.ADVENTURE, true, true, new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
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
            int _slot = config.getInt(path + "slot");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            p.getInventory().setItem(_slot, ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore));
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}