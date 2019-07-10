package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Teams.Team;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class WinEffect extends BukkitRunnable {

    private Main plugin;
    private Team team;

    public WinEffect(Main plugin, Team team) {
        this.plugin = plugin;
        this.team = team;
    }

    @Override
    public void run() {
        if (!plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.FINISH)) {
            this.cancel();
            return;
        } else if (this.team == null) {
            this.cancel();
            return;
        }

        spawnFireworks();
    }

    private void spawnFireworks() {
        this.team.getTeamPlayers().forEach(skyPlayer -> {
            if (skyPlayer.getPlayer() != null)
                spawnFireworks(skyPlayer.getPlayer().getLocation());
        });
    }

    private void spawnFireworks(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
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