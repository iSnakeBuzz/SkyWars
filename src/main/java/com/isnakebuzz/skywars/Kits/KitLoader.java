package com.isnakebuzz.skywars.Kits;

import com.isnakebuzz.skywars.Inventory.KitInventory;
import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class KitLoader {

    private Main plugin;

    private Map<String, Kit> kitMap;
    private List<Kit> defaultKits;

    public KitLoader(Main plugin) {
        this.plugin = plugin;
        this.kitMap = new HashMap<>();
        this.defaultKits = new ArrayList<>();
    }

    public void loadKits() {
        plugin.debug("Loading kits..");
        for (File kit : getKits()) {
            plugin.debug("Loading kit from file [" + kit.getName() + "]");

            //Creating config file
            String fileName = kit.getName().split(Pattern.quote("."))[0];
            Configuration config = plugin.getConfig("Kits/" + fileName);
            YamlConfiguration configInv = YamlConfiguration.loadConfiguration(kit);

            //Kit configurations
            String name = config.getString("Name", "Default");
            String logo = config.getString("Logo", "166:0");

            String perm = config.getString("Perm", "none");

            Boolean isDefault = config.getBoolean("Default", false);

            //Loading inventory configuration
            ItemStack[] armorCont = (ItemStack[]) ((List) configInv.get("Armor")).toArray(new ItemStack[0]);
            ItemStack[] invCont = (ItemStack[]) ((List) configInv.get("Inventory")).toArray(new ItemStack[0]);


            // Parsing data to kit format
            KitInventory kitInventory = new KitInventory(armorCont, invCont);
            ItemStack iLogo = ItemBuilder.crearItem(Integer.valueOf(logo.split(":")[0]), 1, Integer.valueOf(logo.split(":")[1]));


            // Creating kit
            Kit pKit = new Kit(name, iLogo, perm, kitInventory);


            //Adding kit to the kit map
            this.kitMap.put(name, pKit);
            if (isDefault && !this.defaultKits.contains(pKit)) {
                this.defaultKits.add(pKit);
                plugin.debug("Added kit (" + kit.getName() + ") to default kit list");
            }
        }
        plugin.debug("Has been loaded all kits :)");
    }

    public Kit getKit(String kitName) {
        return this.kitMap.getOrDefault(kitName, this.defaultKits.get(new Random().nextInt(this.defaultKits.size())));
    }

    private File[] getKits() {
        File dir = new File(plugin.getDataFolder() + "/Kits/");
        File[] files = dir.listFiles();
        return files;
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
