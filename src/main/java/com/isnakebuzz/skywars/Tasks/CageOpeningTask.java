package com.isnakebuzz.skywars.Tasks;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.DefaultFontInfo;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class CageOpeningTask extends BukkitRunnable {

    private Main plugin;

    public CageOpeningTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Lang");
        Set<String> keys = config.getConfigurationSection("Count Messages.Cages Open").getKeys(false);
        for (String time_config : keys) {
            if (plugin.getSkyWarsArena().getCageOpens() == Integer.valueOf(time_config)) {
                plugin.broadcast(config.getString("Count Messages.Cages Open." + time_config)
                        .replaceAll("%seconds%", String.valueOf(plugin.getSkyWarsArena().getCageOpens()))
                );
            }
        }

        if (plugin.getSkyWarsArena().getCageOpens() <= 1) {
            /*for (int id = 0; id <= plugin.getSkyWarsArena().getGamePlayers().size(); id++) {
                int finalId = id;
                if (id > plugin.getSkyWarsArena().getGamePlayers().size()) return;
                Location location = plugin.getSkyWarsArena().getSpawnLocations().get(finalId);

                plugin.getSkyWarsArena().deleteCage(location);
            } OLD CODE */
            plugin.getCagesManager().deleteAllCages();
            plugin.getSkyWarsArena().setGameStatus(GameStatus.INGAME);
            plugin.getListenerManager().unloadPrelobby();
            plugin.getSkyWarsArena().fillChests();

            for (String msgs : config.getStringList("Banner")) {
                String message = c(msgs);
                if (message.contains("%center%")) {
                    message = centerText(message.replaceAll("%center%", ""), 150);
                }

                plugin.broadcast(message);
            }


            new InGame(plugin).runTaskTimerAsynchronously(plugin, 0, 20);

            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> plugin.getListenerManager().unloadCageOpens(), 20 * 5);
            this.cancel();
            return;
        }

        if (plugin.getSkyWarsArena().getGamePlayers().size() < plugin.getSkyWarsArena().getMinPlayers() && !plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.FINISH)) {
            this.cancel();
            plugin.resetArena();
            return;
        }

        plugin.getSkyWarsArena().setCageOpens(plugin.getSkyWarsArena().getCageOpens() - 1);
    }

    private String centerText(String message, int CENTER_PX) {
        if (message == null || message.equals("")) return "";
        message = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode == true) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb.toString() + message;
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}