package com.isnakebuzz.skywars.Listeners.DeathMessages;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.ArrayList;

public class Tagging implements Listener {


    private static SkyWars plugin;

    public Tagging(SkyWars plugin) {
        this.plugin = plugin;
    }

    public static boolean getInflicted(final LivingEntity e) {
        return plugin.getSkyWarsArena().getLastDamager().containsKey(e) && plugin.getSkyWarsArena().getLastDmgTime().get(e) <= 10;
    }

    public static LivingEntity getWho(final LivingEntity e) {
        return plugin.getSkyWarsArena().getLastDamager().getOrDefault(e, null);
    }

    public static void increaseTimers() {
        if (plugin.getSkyWarsArena().getLastDmgTime().size() >= 1) {
            final ArrayList<LivingEntity> entities = new ArrayList<>(plugin.getSkyWarsArena().getLastDmgTime().keySet());
            for (final LivingEntity e : entities) {
                if (e.isDead() || !e.isValid()) {
                    plugin.getSkyWarsArena().getLastDmgTime().remove(e);
                    plugin.getSkyWarsArena().getLastDamager().remove(e);
                } else if (plugin.getSkyWarsArena().getLastDmgTime().get(e) <= 10) {
                    plugin.getSkyWarsArena().getLastDmgTime().put(e, plugin.getSkyWarsArena().getLastDmgTime().get(e) - 1);
                } else {
                    plugin.getSkyWarsArena().getLastDmgTime().remove(e);
                    plugin.getSkyWarsArena().getLastDamager().remove(e);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent e) {
        if (e.getEntity() != null && e.getDamager() != null && e.getEntity() instanceof LivingEntity && e.getDamager() instanceof LivingEntity) {
            final LivingEntity attacker = (LivingEntity) e.getDamager();
            final LivingEntity damaged = (LivingEntity) e.getEntity();
            plugin.getSkyWarsArena().getLastDamager().put(damaged, attacker);
            plugin.getSkyWarsArena().getLastDmgTime().put(damaged, 0);
        } else if (e.getEntity() != null && e.getDamager() != null && e.getDamager() instanceof Projectile && e.getDamager().getCustomName() != null && e.getEntity() instanceof LivingEntity) {
            for (LivingEntity en : e.getDamager().getWorld().getLivingEntities()) {
                if (en.getEntityId() == Integer.parseInt(e.getDamager().getCustomName())) {
                    LivingEntity damaged2 = (LivingEntity) e.getEntity();
                    plugin.getSkyWarsArena().getLastDamager().put(damaged2, en);
                    plugin.getSkyWarsArena().getLastDmgTime().put(damaged2, 0);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onShoot(final EntityShootBowEvent e) {
        e.getProjectile().setCustomName(Integer.toString(e.getEntity().getEntityId()));
    }

    @EventHandler
    public void onLaunch(final ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof LivingEntity) {
            final LivingEntity shooter = (LivingEntity) e.getEntity().getShooter();
            e.getEntity().setCustomName(Integer.toString(shooter.getEntityId()));
        }
    }

}
