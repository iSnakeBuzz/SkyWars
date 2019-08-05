package com.isnakebuzz.skywars.Kits;

import com.isnakebuzz.skywars.Inventory.KitInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Kit {

    //Principal
    private String name;
    private ItemStack logo;
    private Boolean isDefault;

    //Perms
    private String permission;

    //Inventory
    private KitInventory kitInventory;

    public Kit(String name, ItemStack logo, String permission, KitInventory kitInventory, boolean isDefault) {
        this.name = name;
        this.logo = logo;
        this.isDefault = isDefault;
        this.permission = permission;
        this.kitInventory = kitInventory;
    }

    public String getName() {
        return name;
    }

    public ItemStack getLogo() {
        return logo;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getPermission() {
        return permission;
    }

    public KitInventory getInventory() {
        return kitInventory;
    }

}
