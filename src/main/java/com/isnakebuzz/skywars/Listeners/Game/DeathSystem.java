package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Calls.Events.SkyWinEvent;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.ScoreboardType;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.skywars.Utils.ScoreBoard.ScoreBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DeathSystem implements Listener {

    private Main plugin;

    public DeathSystem(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void DeathCheckWin(PlayerDeathEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (plugin.getSkyWarsArena().checkWin()) {
                if (plugin.getSkyWarsArena().getGamePlayers().isEmpty()) {
                    Bukkit.getPluginManager().callEvent(new SkyWinEvent(null));
                    return;
                }
                Player winner = plugin.getSkyWarsArena().getGamePlayers().get(0);
                Bukkit.getPluginManager().callEvent(new SkyWinEvent(plugin.getPlayerManager().getPlayer(winner)));
            }
        });
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e) {
        Configuration lang = plugin.getConfig("Lang");
        Player p = e.getEntity().getPlayer();
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);

        if (p.getHealth() < 0.4) {
            skyPlayer.setSpectator(true);
        } else {
            skyPlayer.setSpectator(true);
        }

        skyPlayer.setDead(true);
        plugin.getScoreBoardAPI2().setScoreBoard(p, ScoreboardType.INGAME, true, true, true);
        plugin.getInventories().setSpectInventory(p);
        plugin.getSkyWarsArena().getGamePlayers().remove(p);
        
        PacketsAPI.sendTitle(
                plugin,
                p,
                lang.getString("You Died.Title"),
                lang.getString("You Died.SubTitle"),
                lang.getInt("You Died.FadeIn"),
                lang.getInt("You Died.Stay"),
                lang.getInt("You Died.FadeOut")
        );
    }

    @EventHandler
    public void VoidKill(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (skyPlayer.isSpectator()) {
                    p.teleport(plugin.getSkyWarsArena().getLobbyLocation());
                    return;
                }
                p.setLastDamageCause(e);
                e.setCancelled(true);

                List<ItemStack> list = new ArrayList<>();
                PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(p, list, 1, "none");
                Bukkit.getPluginManager().callEvent(playerDeathEvent);
            }
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
