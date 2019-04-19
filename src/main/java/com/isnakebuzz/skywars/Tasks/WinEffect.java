package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class WinEffect extends BukkitRunnable {

    private Main plugin;
    private Player player;

    public WinEffect(Main plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        if (!plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.FINISH)) {
            this.cancel();
            return;
        } else if (this.player == null) {
            this.cancel();
            return;
        }

        this.spawnFireworks(player.getLocation());
    }

    private void spawnFireworks(Location location) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(1);
        fwm.addEffect(FireworkEffect.builder()
                .withColor(Color.LIME)
                .withColor(Color.FUCHSIA)
                .withColor(Color.RED)
                .flicker(true)
                .trail(true)
                .withFade(Color.GREEN)
                .build()
        );
        fw.setFireworkMeta(fwm);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, fw::detonate, 13);
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}