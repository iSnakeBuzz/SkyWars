package com.isnakebuzz.skywars.Player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PlayerUtils {

    public static void clean(Player p, GameMode gameMode, boolean allowflight, boolean flying, PotionEffect... potionEffects) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setGameMode(gameMode);
        p.setFoodLevel(20);
        p.setExhaustion(20);
        p.setHealth(p.getMaxHealth());
        p.setAllowFlight(allowflight);
        p.setFlying(flying);
        p.setLevel(0);
        p.setExp(0);
        //p.setVelocity(new Vector(0, 0, 0).normalize());
        p.getInventory().setHeldItemSlot(0);
        p.getActivePotionEffects().forEach(potionEffect -> {
            p.removePotionEffect(potionEffect.getType());
        });

        if (potionEffects.length >= 1) {
            for (PotionEffect potionEffect : potionEffects) {
                p.addPotionEffect(potionEffect);
            }
        }
    }

}
