package com.isnakebuzz.skywars.Listeners.Game;

import com.isnakebuzz.skywars.Inventory.MenuManager.MenuCreator;
import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameItems implements Listener {

    private Main plugin;

    public GameItems(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void InventoryInteract(InventoryClickEvent e) {
        if (!plugin.getPlayerManager().getPlayer(((Player) e.getWhoClicked())).isSpectator()) return;

        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Lobby").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        if (key == null | key.size() < 1) return;
        Player p = (Player) e.getWhoClicked();
        for (String item : key) {
            String path = "Lobby." + item + ".";
            String _item = config.getString(path + "item");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            ItemStack itemStack = ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore);

            if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;

            if (e.getCurrentItem().equals(itemStack)) {
                ACTIONS(p, (_action.split(":")[0] != null) ? _action.split(":")[0] : "", (_action.split(":").length > 1) ? _action.split(":")[1] : "");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryInteract(PlayerInteractEvent e) {
        if (!plugin.getPlayerManager().getPlayer(e.getPlayer()).isSpectator()) return;

        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Lobby").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        if (key == null | key.size() < 1) return;
        for (String item : key) {
            String path = "Lobby." + item + ".";
            String _item = config.getString(path + "item");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            ItemStack itemStack = ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore);

            if (e.getItem() == null || e.getItem().getItemMeta().equals(null)) return;

            if (e.getItem().equals(itemStack)) {
                ACTIONS(e.getPlayer(), (_action.split(":")[0] != null) ? _action.split(":")[0] : "", (_action.split(":").length > 1) ? _action.split(":")[1] : "");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryDrag(InventoryDragEvent e) {
        if (!plugin.getPlayerManager().getPlayer(((Player) e.getWhoClicked())).isSpectator()) return;
        e.setCancelled(true);
    }


    private void ACTIONS(Player player, String action, String args) {
        if (action.equalsIgnoreCase("menu")) {
            new MenuCreator(player, plugin, args).open();
        } else if (action.equalsIgnoreCase("cmd")) {
            String cmd = "/" + args;
            player.chat(cmd);
        } else if (action.equalsIgnoreCase("leave")) {
            String lobby = Statics.lobbies.get(new Random().nextInt(Statics.lobbies.size()));
            PacketsAPI.connect(plugin, player, lobby);
        } else if (action.equalsIgnoreCase("playAgain")) {
            player.sendMessage(c("&cThat function is under development, please wait for new updates :)"));
        } else {
            player.sendMessage(c("&cThat action does't exist, please contact with administrator :)"));
        }
    }

    @EventHandler
    public void onTrack(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType() == Material.COMPASS && !player.getGameMode().equals(GameMode.ADVENTURE)) {
            e.setCancelled(true);
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player);

            //Setup lang.yml
            ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Compass Tracker");

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                // Setting up target distance
                double distance = Double.POSITIVE_INFINITY;

                // Setup target player
                Player trackedPlayer = null;

                // Checking nearby entities
                for (Entity entity : player.getNearbyEntities(700, 700, 700)) {

                    // Check if entity instance of player
                    if (entity instanceof Player) {

                        // Getting player from an entity
                        Player entityPlayer = (Player) entity;
                        SkyPlayer skyEntity = plugin.getPlayerManager().getPlayer(entityPlayer);

                        // If is the same team, continue searching :)
                        if (skyPlayer.getTeam() == skyEntity.getTeam()) {
                            continue;
                        }

                        // Checking if entity is a normal player and his is alive
                        if (!skyEntity.isSpectator() || !skyEntity.isStaff()) {

                            // If entity is he self, continue searching
                            if (entity == player) {
                                continue;
                            }

                            // Checking distance between entities
                            double distancing = player.getLocation().distance(entity.getLocation());
                            if (distancing > distance || distancing < 10.0) {
                                continue;
                            }

                            // Distance checkend and all its good
                            distance = distancing;
                            trackedPlayer = entityPlayer;
                        }
                    }
                }
                if (trackedPlayer == null) {
                    player.sendMessage(c(lang.getString("No players")));
                } else {
                    player.setCompassTarget(trackedPlayer.getLocation());
                    player.sendMessage(c(
                            lang.getString("Tracked")
                                    .replaceAll("%distance%", String.format("%.1f", distance).replaceAll(",", "."))
                                    .replaceAll("%tracked%", trackedPlayer.getDisplayName())
                    ));
                }
            });
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
