package com.isnakebuzz.skywars.Listeners.DeathMessages;

import com.isnakebuzz.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMsgEvent implements Listener {

    private Main plugin;

    public DeathMsgEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDie(final PlayerDeathEvent e) {
        e.setDeathMessage((String) null);
    }

    @EventHandler
    public void onEntityDie(final EntityDeathEvent e) {
        if (e.getEntity() != null) {
            createMessage(e.getEntity(), e.getEntity().getLastDamageCause().getCause());
            plugin.getSkyWarsArena().getLastDamager().remove(e.getEntity());
            plugin.getSkyWarsArena().getLastDmgTime().remove(e.getEntity());
        }
    }

    public void createMessage(final LivingEntity dead, final EntityDamageEvent.DamageCause cause) {
        final boolean enableAll = true;
        final boolean enableNamed = false;
        boolean customName = false;
        if (dead.getCustomName() != null) {
            customName = true;
        }
        if (dead instanceof Player || customName) {
            if (Tagging.getInflicted(dead)) {
                String message;
                if (Tagging.getWho(dead) instanceof Player) {
                    message = messageReplaceAndColor(dead, true, Tagging.getWho(dead), cause, true);
                } else {
                    message = messageReplaceAndColor(dead, true, Tagging.getWho(dead), cause, false);
                }
                print(message, dead.getWorld());
            } else {
                String message = messageReplaceAndColor(dead, false, null, cause, false);
                print(message, dead.getWorld());
            }
        }
    }

    private void print(final String message, final World w) {
        plugin.log("Death Message", message);
        for (final Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(message);
        }
    }

    private String messageReplaceAndColor(final LivingEntity dead, final Boolean inflicted, final LivingEntity attacker, final EntityDamageEvent.DamageCause cause, final Boolean playerAttack) {
        final String raw = getDeathMessageFromConfig(cause, inflicted, playerAttack);
        String raw2 = "";
        String returnRaw = "";
        if (dead instanceof Player) {
            Player p1 = (Player) dead;
            raw2 = raw.replace("%dead%", dead.getName());
        } else if (dead.getCustomName() == null) {
            raw2 = raw.replace("%dead%", dead.getType().name());
        } else {
            raw2 = raw.replace("%dead%", dead.getCustomName() + "(" + dead.getType().name() + ")");
        }
        if (attacker != null) {
            if (attacker instanceof Player) {
                Player p2 = (Player) attacker;
                returnRaw = raw2.replace("%killer%", attacker.getName());
            } else if (attacker.getCustomName() == null) {
                returnRaw = raw2.replace("%killer%", attacker.getType().toString());
            } else {
                returnRaw = raw2.replace("%killer%", attacker.getCustomName() + "(" + attacker.getType().name() + ")");
            }
        } else {
            returnRaw = raw2;
        }
        return c(returnRaw);
    }

    private String getDeathMessageFromConfig(final EntityDamageEvent.DamageCause cause, final Boolean inflicted, final Boolean playerAttack) {
        String path = "other";
        String messageType = null;
        if (inflicted) {
            messageType = "inflicted";
        }
        if (!inflicted) {
            messageType = "other";
        }
        String ver = Bukkit.getBukkitVersion();
        ver = ver.substring(0, ver.indexOf("-"));
        if (cause == EntityDamageEvent.DamageCause.FALL) {
            path = "fall";
        }
        if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            path = "explosion";
        }
        if (cause == EntityDamageEvent.DamageCause.CONTACT) {
            path = "cactus";
        }
        if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            path = "drown";
        }
        if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
            path = "fire";
        }
        if (cause == EntityDamageEvent.DamageCause.LAVA) {
            path = "lava";
        }
        if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
            path = "lightning";
        }
        if (cause == EntityDamageEvent.DamageCause.MELTING) {
            path = "melting";
        }
        if (cause == EntityDamageEvent.DamageCause.PROJECTILE) {
            path = "projectile";
        }
        if (cause == EntityDamageEvent.DamageCause.STARVATION) {
            path = "starvation";
        }
        if (cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            path = "suffocation";
        }
        if (cause == EntityDamageEvent.DamageCause.SUICIDE) {
            path = "suicide";
        }
        if (cause == EntityDamageEvent.DamageCause.THORNS) {
            path = "thorns";
        }
        if (cause == EntityDamageEvent.DamageCause.VOID) {
            path = "void";
        }
        if (cause == EntityDamageEvent.DamageCause.WITHER) {
            path = "wither";
        }
        if (cause == EntityDamageEvent.DamageCause.MAGIC) {
            path = "potion";
        }
        if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (playerAttack) {
                path = "player-attack";
            } else {
                path = "entity-attack";
            }
        }
        return plugin.getConfigUtils().getConfig(plugin, "Lang").getString("Death Messages." + path + "." + messageType);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
