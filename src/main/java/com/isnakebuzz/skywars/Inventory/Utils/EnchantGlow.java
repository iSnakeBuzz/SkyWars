package com.isnakebuzz.skywars.Inventory.Utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class EnchantGlow extends EnchantmentWrapper {
    private static Enchantment glow;

    public EnchantGlow(final int n) {
        super(n);
    }

    public static Enchantment getGlow() {
        if (EnchantGlow.glow != null) {
            return EnchantGlow.glow;
        }
        try {
            final Field declaredField = Enchantment.class.getDeclaredField("acceptingNew");
            declaredField.setAccessible(true);
            declaredField.set(null, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Enchantment.registerEnchantment(EnchantGlow.glow = (Enchantment) new EnchantGlow(255));
        return EnchantGlow.glow;
    }

    public static void addGlow(final ItemStack itemStack) {
        itemStack.addEnchantment(getGlow(), 1);
    }

    public boolean canEnchantItem(final ItemStack itemStack) {
        return true;
    }

    public boolean conflictsWith(final Enchantment enchantment) {
        return false;
    }

    public EnchantmentTarget getItemTarget() {
        return null;
    }

    public int getMaxLevel() {
        return 10;
    }

    public String getName() {
        return "Glow";
    }

    public int getStartLevel() {
        return 1;
    }
}
