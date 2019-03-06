package com.isnakebuzz.skywars.Inventory.MenuManager;

import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MenuCreator extends Menu {

    private Main plugin;
    private int taskId;

    public MenuCreator(Player player, Main plugin, String _name) {
        super(plugin, _name);
        this.plugin = plugin;
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Utils/MenuCreator");
        String path = "MenuCreator." + _name + ".";
        if (config.getBoolean(path + "update.enabled")) {
            taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
                updateInv(player, _name);
            }, 0, config.getInt(path + "update.time"));
        } else {
            this.updateInv(player, _name);
        }
    }

    @Override
    public void onClick(final Player p, final ItemStack itemStack, String _name) {
        Configuration lang = plugin.getConfigUtils().getConfig(plugin, "Lang");
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Utils/MenuCreator");
        String menu_path = "MenuCreator." + _name + ".";
        Set<String> key = config.getConfigurationSection(menu_path + "items").getKeys(false);
        for (String _item : key) {
            String path = menu_path + "items." + _item + ".";
            String item = config.getString(path + "item");
            int amount = config.getInt(path + "amount");
            String name = config.getString(path + "name");
            List<String> lore = chars(config.getStringList(path + "lore"));
            String permission = config.getString(path + "perms");
            String action = config.getString(path + "action");
            ItemStack itemStack1 = ItemBuilder.crearItem1(Integer.valueOf(item.split(":")[0]), amount, Integer.valueOf(item.split(":")[1]), name, lore);
            if (itemStack.equals(itemStack1)) {
                if (!p.hasPermission(permission) && !permission.equalsIgnoreCase("none")) {
                    p.sendMessage(c(lang.getString("NoPerms")));
                    return;
                }
                if (action.split(":")[0].equalsIgnoreCase("open")) {
                    new MenuCreator(p, plugin, action.split(":")[1]).o(p);
                } else if (action.split(":")[0].equalsIgnoreCase("cmd")) {
                    String cmd = "/" + action.split(":")[1];
                    p.chat(cmd);
                } else if (action.split(":")[0].equalsIgnoreCase("velocity")) {
                    float speed = Float.valueOf(action.split(":")[1]);
                    p.setWalkSpeed(speed);
                } else if (action.split(":")[0].equalsIgnoreCase("flyspeed")) {
                    float speed = Float.valueOf(action.split(":")[1]);
                    p.setFlySpeed(speed);
                }

            }
        }

    }

    @Override
    public void onClose(Player p) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    private void updateInv(Player p, String _name) {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Utils/MenuCreator");
        String menu_path = "MenuCreator." + _name + ".";
        Set<String> key = config.getConfigurationSection(menu_path + "items").getKeys(false);
        for (String _item : key) {
            String path = menu_path + "items." + _item + ".";
            String item = config.getString(path + "item");
            int slot = config.getInt(path + "slot");
            int amount = config.getInt(path + "amount");
            String name = config.getString(path + "name");
            List<String> lore = chars(config.getStringList(path + "lore"));
            String action = config.getString(path + "action");
            ItemStack itemStack = ItemBuilder.crearItem1(Integer.valueOf(item.split(":")[0]), amount, Integer.valueOf(item.split(":")[1]), name, lore);
            this.s(slot, itemStack);
        }
    }

    private List<String> chars(List<String> messages) {
        List<String> newMessages = new ArrayList<>();
        for (String msg : messages) {
            newMessages.add(msg
            );
        }
        return newMessages;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
