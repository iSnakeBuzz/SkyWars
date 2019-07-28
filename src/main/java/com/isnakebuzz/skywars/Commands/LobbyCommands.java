package com.isnakebuzz.skywars.Commands;

import com.isnakebuzz.skywars.Inventory.Shop.ShopCreator;
import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommands implements CommandExecutor {

    private Main plugin;

    public LobbyCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("skywars") && sender instanceof Player) {
            if (args.length < 1) {
                sendHelp(sender);
                return true;
            }

            Player p = (Player) sender;
            String cmd = args[0];

            if (cmd.equalsIgnoreCase("shop")) {
                new ShopCreator(p, plugin, "Shop").open();
            }
        }

        return false;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(c("&aSkyWars &8&l|&e Version: " + plugin.getDescription().getVersion() + ", made by iSnakeBuzz_"));
        sender.sendMessage(c("&e/sw shop"));
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
