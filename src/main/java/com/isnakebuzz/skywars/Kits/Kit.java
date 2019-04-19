package com.isnakebuzz.skywars.Kits;

import com.isnakebuzz.skywars.Inventory.KitInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Kit {

    //Principal
    private String name;
    private ItemStack logo;

    //Perms
    private String permission;

    //Inventory
    private KitInventory kitInventory;

    public Kit(String name, ItemStack logo, String permission, KitInventory kitInventory) {
        this.name = name;
        this.logo = logo;
        this.permission = permission;
        this.kitInventory = kitInventory;
    }

    public String getName() {
        return name;
    }

    public ItemStack getLogo() {
        return logo;
    }

    public String getPermission() {
        return permission;
    }

    public KitInventory getInventory() {
        return kitInventory;
    }
}
