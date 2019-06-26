package com.isnakebuzz.skywars.Utils;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Calls.Callback;
import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Kits.Kit;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private Main plugin;

    public Utils(Main plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem(ConfigurationSection config) {

        String itemName = c(config.getString("name"));
        String[] itemFormatted = config.getString("item").split(":");
        int amount = config.getInt("amount");
        List<String> lore = c(config.getStringList("lore"));

        plugin.debug(config.getString("name"));

        int itemID = Integer.valueOf(itemFormatted[0]);
        int itemD = Integer.valueOf(itemFormatted[1]);

        return ItemBuilder.crearItem1(itemID, amount, itemD, itemName, lore);
    }

    public ItemStack createItem(Kit kit, boolean purchKit, boolean isSelected, boolean isDefault) {

        ConfigurationSection noPurchased = plugin.getConfig("Settings").getConfigurationSection("No Purchased Logo");
        ConfigurationSection purchased = plugin.getConfig("Settings").getConfigurationSection("Purchased Logo");
        ConfigurationSection selected = plugin.getConfig("Settings").getConfigurationSection("Selected Logo");

        String itemName;
        String[] itemFormatted;
        int amount = 1;
        List<String> lore = kit.getLogo().getItemMeta().getLore();

        if (purchKit || isDefault) {
            itemFormatted = new String[]{String.valueOf(kit.getLogo().getType().getId()), String.valueOf(kit.getLogo().getDurability())};

            if (isSelected) {
                itemName = selected.getString("name").replaceAll("%name%", strip(kit.getLogo().getItemMeta().getDisplayName()));
                lore = c(selected.getStringList("lore"));
            } else {
                itemName = purchased.getString("name").replaceAll("%name%", strip(kit.getLogo().getItemMeta().getDisplayName()));
                lore.addAll(c(purchased.getStringList("lore")));
            }

        } else {
            itemName = noPurchased.getString("name").replaceAll("%name%", strip(kit.getLogo().getItemMeta().getDisplayName()));
            itemFormatted = noPurchased.getString("item", "160:14").split(":");
            lore.addAll(c(noPurchased.getStringList("lore")));
        }

        int itemID = Integer.valueOf(itemFormatted[0]);
        int itemD = Integer.valueOf(itemFormatted[1]);

        return ItemBuilder.crearItem1(itemID, amount, itemD, itemName, lore);
    }

    public List<String> c(List<String> ls) {
        List<String> newList = Lists.newArrayList();
        ls.forEach(s -> newList.add(c(s)));
        return newList;
    }

    public void playSound(Player player, String sound) {
        String[] args = sound.split(":");

        String soundName = args[0];
        int volume = Integer.parseInt(args[1]);
        int pitch = Integer.parseInt(args[2]);

        player.playSound(player.getLocation(), Sound.valueOf(soundName), volume, pitch);
    }

    public void sortKits(SkyPlayer skyPlayer, Callback<List<Kit>> callback) {

        List<Kit> defaultKits = Lists.newArrayList();

        List<Kit> purchKits = Lists.newArrayList();
        List<Kit> normalKits = Lists.newArrayList();

        for (Kit kit : plugin.getKitLoader().getKits()) {
            if (skyPlayer.getPurchKits().contains(kit.getName())) {
                purchKits.add(kit);
            } else if (kit.isDefault()) {
                defaultKits.add(kit);
            } else {
                normalKits.add(kit);
            }
        }

        List<Kit> finalList = Lists.newArrayList();

        finalList.addAll(defaultKits);
        finalList.addAll(purchKits);
        finalList.addAll(normalKits);

        callback.done(finalList);

    }

    public String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String strip(String s) {
        return ChatColor.stripColor(s);
    }

}
