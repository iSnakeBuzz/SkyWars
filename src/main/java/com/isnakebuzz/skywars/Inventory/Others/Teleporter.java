package com.isnakebuzz.skywars.Inventory.Others;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Inventory.MenuManager.Menu;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;


public class Teleporter extends Menu {

    private Main plugin;

    private HashMap<String, Player> itemPlayer;

    public Teleporter(Main plugin, String menu_name, Player player) {
        super(plugin, menu_name, player);
        this.plugin = plugin;
        this.itemPlayer = new HashMap<>();

        this.loadKits();
    }

    @Override
    public void onClick(Player p, ItemStack item, String name) {
        if (!plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.INGAME)) return;

        if (this.itemPlayer.containsKey(item.getItemMeta().getDisplayName())) {
            Player toTp = this.itemPlayer.get(item.getItemMeta().getDisplayName());
            p.closeInventory();
            p.teleport(toTp);
        }


        plugin.debug("ClickEvent AT Teleporter CLASS");
    }

    @Override
    public void onClose(Player p) {

    }

    private void loadKits() {
        this.inventory().clear();
        ConfigurationSection settings = plugin.getConfig("Settings").getConfigurationSection("Spectator Item");

        for (Player player : plugin.getSkyWarsArena().getGamePlayers()) {
            SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());

            if (skyPlayer.isSpectator()) {
                continue;
            }

            String itemName = c(settings.getString("name").replaceAll("%player%", player.getDisplayName()));
            List<String> itemLore = c(player, settings.getStringList("lore"));


            this.itemPlayer.put(itemName, player);
            this.inventory().addItem(getHead(player, itemName, itemLore));
        }
    }

    private ItemStack getHead(Player player, String itemName, List<String> itemLore) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName(itemName);
        meta.setLore(itemLore);
        PacketsAPI.setField("profile", meta, ((CraftPlayer) player).getProfile());
        head.setItemMeta(meta);
        return head;
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private List<String> c(Player player, List<String> s) {
        List<String> lore = Lists.newArrayList();
        for (String text : s) {
            lore.add(c(text
                    .replaceAll("%player_health%", String.valueOf(getPorcentage(player.getMaxHealth(), player.getHealth())))
                    .replaceAll("%player_food%", String.valueOf(getPorcentage(20, player.getFoodLevel())))
            ));
        }

        return lore;
    }

    public double getPorcentage(double total, double obtained) {
        return (obtained * 100) / total;
    }

    private String strip(String s) {
        return ChatColor.stripColor(s);
    }
}
