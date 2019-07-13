package com.isnakebuzz.skywars.Player;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    public static void addEffects(Player p, PotionEffect... potionEffects) {
        if (potionEffects.length != 0) {
            for (PotionEffect potionEffect : potionEffects) {
                p.addPotionEffect(potionEffect);
            }
        }
    }

    public static void removePotionEffect(Player p, PotionEffectType... potionEffects) {
        if (potionEffects.length != 0) {
            for (PotionEffectType potionEffect : potionEffects) {
                p.removePotionEffect(potionEffect);
            }
        }
    }

}
