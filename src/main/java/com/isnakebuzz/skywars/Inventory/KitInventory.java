package com.isnakebuzz.skywars.Inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitInventory {

    private ItemStack[] armorCont;
    private ItemStack[] invCont;

    public KitInventory(ItemStack[] armorCont, ItemStack[] invCont) {
        this.armorCont = armorCont;
        this.invCont = invCont;
    }

    public ItemStack[] getArmorCont() {
        return armorCont;
    }

    public ItemStack[] getInvCont() {
        return invCont;
    }

    public void setArmorCont(ItemStack[] armorCont) {
        this.armorCont = armorCont;
    }

    public void setInvCont(ItemStack[] invCont) {
        this.invCont = invCont;
    }

    public void setup(Player player) {
        player.getInventory().clear();
        player.updateInventory();
        player.getInventory().setArmorContents(armorCont);
        player.getInventory().setContents(invCont);
        player.updateInventory();
    }

}
