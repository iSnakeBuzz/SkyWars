package com.isnakebuzz.skywars.Utils;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Kits.Kit;
import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

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
            itemFormatted = purchased.getString("item").split(":");

            if (isSelected) {
                itemName = noPurchased.getString("name").replaceAll("%name%", strip(kit.getLogo().getItemMeta().getDisplayName()));
                lore = c(selected.getStringList("lore"));
            } else {
                itemName = noPurchased.getString("name").replaceAll("%name%", strip(kit.getLogo().getItemMeta().getDisplayName()));
                lore.addAll(c(purchased.getStringList("lore")));
            }

        } else {
            itemName = noPurchased.getString("name").replaceAll("%name%", strip(kit.getLogo().getItemMeta().getDisplayName()));
            itemFormatted = new String[]{String.valueOf(kit.getLogo().getType().getId()), String.valueOf(kit.getLogo().getDurability())};
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


    public String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String strip(String s) {
        return ChatColor.stripColor(s);
    }

}
