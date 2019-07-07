package com.isnakebuzz.skywars.Player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PlayerUtils {

    public static void clean(Player p, PotionEffect... potionEffects) {
        p.setFoodLevel(20);
        p.setExhaustion(20);
        p.setHealth(p.getMaxHealth());
        p.setLevel(0);
        p.setExp(0);
        p.setFireTicks(0);
        p.getInventory().clear();
        p.setGameSpect(false);
        p.getInventory().setArmorContents(null);
        //p.setVelocity(new Vector(0, 0, 0).normalize());
        if (!p.getActivePotionEffects().isEmpty()) {
            p.getActivePotionEffects().forEach(potionEffect -> {
                p.removePotionEffect(potionEffect.getType());
            });
        }

        if (potionEffects.length != 0) {
            for (PotionEffect potionEffect : potionEffects) {
                p.addPotionEffect(potionEffect);
            }
        }
    }

}
