package com.isnakebuzz.skywars.Kits;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Inventory.KitInventory;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.apache.commons.collections4.CollectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
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
        for (File kit : getKitsFiles()) {
            plugin.debug("Loading kit from file [" + kit.getName() + "]");

            //Creating config file
            String fileName = kit.getName().split(Pattern.quote("."))[0];
            Configuration config = plugin.getConfig("Kits/" + fileName);

            //Kit configurations
            String name = config.getString("Name", "Default");
            String perm = config.getString("Perm", "none");

            boolean isDefault = config.getBoolean("Default", false);

            //Loading inventory configuration
            ItemStack[] armorCont = (ItemStack[]) ((List) config.get("Armor")).toArray(new ItemStack[0]);
            ItemStack[] invCont = (ItemStack[]) ((List) config.get("Inventory")).toArray(new ItemStack[0]);


            // Parsing data to kit format
            KitInventory kitInventory = new KitInventory(armorCont, invCont);
            plugin.debug(config.getConfigurationSection("Logo").toString());
            ItemStack iLogo = plugin.getUtils().createItem(config.getConfigurationSection("Logo"));


            // Creating kit
            Kit pKit = new Kit(name, iLogo, perm, kitInventory, isDefault);

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

    public void giveKit(SkyPlayer skyPlayer) {
        Kit kit = getKit(skyPlayer.getSelectedKit());
        kit.getInventory().setup(skyPlayer.getPlayer());
    }

    public Collection<Kit> getKits() {
        return this.kitMap.values();
    }

    public Collection<Kit> getDefaultKits() {
        Collection<Kit> kits = Lists.newArrayList(this.kitMap.values());
        CollectionUtils.filter(kits, Kit::isDefault);
        return kits;
    }

    private File[] getKitsFiles() {
        File dir = new File(plugin.getDataFolder() + "/Kits/");
        if (!dir.exists()) dir.mkdir();
        return dir.listFiles();
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
