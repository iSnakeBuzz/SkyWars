package com.isnakebuzz.skywars.Inventory.Shop;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Inventory.MenuManager.Menu;
import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.LobbyPlayer;
import com.isnakebuzz.skywars.Utils.PacketsAPI;
import com.isnakebuzz.snakeco.Utils.EcoAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ShopCreator extends Menu {

    private Main plugin;
    private int taskId;

    public ShopCreator(Player player, Main plugin, String _name) {
        super(plugin, _name, player);
        this.plugin = plugin;
        Configuration config = plugin.getConfig("Extra/MenuCreator");
        String path = "MenuCreator." + _name + ".";
        if (config.getBoolean(path + "update.enabled")) {
            taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                updateInv(player, _name);
            }, 0, config.getInt(path + "update.time")).getTaskId();
        } else {
            this.updateInv(player, _name);
        }
    }

    @Override
    public void onClick(Player p, ItemStack itemStack, String _name) {
        Configuration lang = plugin.getConfigUtils().getConfig(plugin, "Lang");
        Configuration config = plugin.getConfig("Extra/MenuCreator");
        String menu_path = "MenuCreator." + _name + ".";
        Set<String> key = config.getConfigurationSection(menu_path + "items").getKeys(false);
        for (String _item : key) {
            String path = menu_path + "items." + _item + ".";
            String item = config.getString(path + "item");
            int amount = config.getInt(path + "amount");
            String name = config.getString(path + "name");
            String permission = config.getString(path + "perms");
            String[] action = config.getString(path + "action").split(":");
            List<String> lore = Lists.newArrayList();

            plugin.debug("onClick - " + Arrays.toString(action) + ", " + action.length);
            plugin.debug("onClick2 - Use kits?: " + (action.length > 1 && action[1].equalsIgnoreCase("kit")));

            if (action.length > 1 && (action[1].contains("kit") || action[1].contains("cage"))) {
                String[] args = (action[1]).split("\\|");
                lore.addAll(chars_kits(p, args[1], config.getStringList(path + "lore")));

                plugin.debug("onClick3 - " + Arrays.toString(args) + ", " + args.length);
            } else {
                lore.addAll(chars(p, config.getStringList(path + "lore")));
            }


            ItemStack itemStack1 = null;

            if (String.valueOf(item.split(":")[0]).equalsIgnoreCase("head")) {
                itemStack1 = getHead(p, c(p, name), lore);
            } else {
                itemStack1 = ItemBuilder.crearItem1(Integer.valueOf(item.split(":")[0]), amount, Integer.valueOf(item.split(":")[1]), name, lore);
            }

            if (itemStack1.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                if (!p.hasPermission(permission) && !permission.equalsIgnoreCase("none")) {
                    p.sendMessage(c(p, lang.getString("NoPerms")));
                    return;
                }

                if (action[0].equalsIgnoreCase("open")) {
                    new ShopCreator(p, plugin, action[1]).open();
                } else if (action[0].equalsIgnoreCase("cmd")) {
                    String cmd = "/" + action[1];
                    p.chat(cmd);
                } else if (action[0].equalsIgnoreCase("close")) {
                    p.closeInventory();
                } else if (action[0].equalsIgnoreCase("msg")) {
                    String message = action[1];
                    p.sendMessage(c(p, message));
                    p.closeInventory();
                } else if (action[0].equalsIgnoreCase("buy")) {
                    String[] args = (action[1]).split("\\|");
                    getAction(p, args, lang);
                    break;
                }
            }

        }
    }

    @Override
    public void onClose(Player p) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    private void updateInv(Player p, String _name) {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/MenuCreator");
        String menu_path = "MenuCreator." + _name + ".";
        Set<String> key = config.getConfigurationSection(menu_path + "items").getKeys(false);
        for (String _item : key) {
            String path = menu_path + "items." + _item + ".";
            String item = config.getString(path + "item");
            int slot = config.getInt(path + "slot");
            int amount = config.getInt(path + "amount");
            String name = config.getString(path + "name");
            String[] action = config.getString(path + "action").split(":");
            List<String> lore = Lists.newArrayList();

            if (action.length > 1 && (action[1].contains("kit"))) {
                String[] args = (action[1]).split("\\|");

                lore.addAll(chars_kits(p, args[1], config.getStringList(path + "lore")));
            } else if (action.length > 1 && (action[1].contains("cage"))) {
                String[] args = (action[1]).split("\\|");

                lore.addAll(chars_cages(p, args[1], config.getStringList(path + "lore")));
            } else {
                lore.addAll(chars(p, config.getStringList(path + "lore")));
            }

            ItemStack itemStack1 = null;

            if (String.valueOf(item.split(":")[0]).equalsIgnoreCase("head")) {
                itemStack1 = getHead(p, c(p, name), lore);
            } else {
                itemStack1 = ItemBuilder.crearItem1(Integer.valueOf(item.split(":")[0]), amount, Integer.valueOf(item.split(":")[1]), name, lore);
            }
            this.s(slot, itemStack1);
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

    private List<String> chars(Player player, List<String> messages) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, messages);
        } else {
            return messages;
        }
    }

    private List<String> chars_kits(Player player, String kitName, List<String> messages) {
        List<String> newList = Lists.newArrayList();
        LobbyPlayer skyPlayer = plugin.getPlayerManager().getLbPlayer(player.getUniqueId());

        for (String msg : messages) {
            if (msg.contains("<purchased>")) {
                if (skyPlayer.getPurchKits().contains(kitName)) {
                    newList.add(c(player, msg.replaceAll("<purchased>", "")));
                }
            } else if (msg.contains("<!purchased>")) {
                if (!skyPlayer.getPurchKits().contains(kitName)) {
                    newList.add(c(player, msg.replaceAll("<!purchased>", "")));
                }
            } else {
                newList.add(c(player, msg));
            }
        }


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, newList);
        } else {
            return newList;
        }
    }

    private List<String> chars_cages(Player player, String kitName, List<String> messages) {
        List<String> newList = Lists.newArrayList();
        LobbyPlayer skyPlayer = plugin.getPlayerManager().getLbPlayer(player.getUniqueId());

        for (String msg : messages) {
            if (msg.contains("<purchased>")) {
                if (skyPlayer.getPurchCages().contains(kitName)) {
                    newList.add(c(player, msg.replaceAll("<purchased>", "")));
                }
            } else if (msg.contains("<!purchased>")) {
                if (!skyPlayer.getPurchCages().contains(kitName)) {
                    newList.add(c(player, msg.replaceAll("<!purchased>", "")));
                }
            } else {
                newList.add(c(player, msg));
            }
        }


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, newList);
        } else {
            return newList;
        }
    }

    public void getAction(Player p, String[] args, Configuration lang) {
        LobbyPlayer skyPlayer = plugin.getPlayerManager().getLbPlayer(p.getUniqueId());

        if (args[0].equalsIgnoreCase("kit")) {
            String kitName = args[1];
            int takeCoins = Integer.valueOf(args[2]);

            if (skyPlayer.getPurchKits().contains(kitName)) {
                skyPlayer.setSelectedKit(kitName);
                p.sendMessage(c(p, lang.getString("Shop.Kits.selected")
                        .replaceAll("%kit%", kitName)
                ));
            } else {
                int haveCoins = EcoAPI.getCoins(p);

                if (takeCoins <= haveCoins) {
                    EcoAPI.removeCoins(p, takeCoins);
                    p.sendMessage(c(p, lang.getString("Shop.Kits.buyed")
                            .replaceAll("%kit%", kitName)
                    ));
                    skyPlayer.getPurchKits().add(kitName);
                    skyPlayer.setSelectedKit(kitName);
                } else {
                    p.sendMessage(c(p, lang.getString("Shop.No have coins")));
                }
            }

            p.closeInventory();
        } else if (args[0].equalsIgnoreCase("cage")) {
            String cageName = args[1];
            int takeCoins = Integer.valueOf(args[2]);

            if (skyPlayer.getPurchCages().contains(cageName)) {
                skyPlayer.setCageName(cageName);
                p.sendMessage(c(p, lang.getString("Shop.Cages.selected")
                        .replaceAll("%cageName%", cageName)
                ));
            } else {
                int haveCoins = EcoAPI.getCoins(p);

                if (takeCoins <= haveCoins) {
                    EcoAPI.removeCoins(p, takeCoins);
                    p.sendMessage(c(p, lang.getString("Shop.Cages.buyed")
                            .replaceAll("%cageName%", cageName)
                    ));
                    skyPlayer.getPurchCages().add(cageName);
                } else {
                    p.sendMessage(c(p, lang.getString("Shop.No have coins")));
                }
            }

            p.closeInventory();
        }

    }

    private String c(Player player, String c) {
        return ChatColor.translateAlternateColorCodes('&', c.replaceAll("%player%", player.getDisplayName()));
    }

}
