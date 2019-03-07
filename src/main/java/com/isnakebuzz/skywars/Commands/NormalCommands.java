package com.isnakebuzz.skywars.Commands;

import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class NormalCommands implements CommandExecutor {

    private Main plugin;

    public NormalCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("skywars") && sender instanceof Player) {
            sender.sendMessage(c("&aSnake SkyWars &8&l|&e Version and Author: " + plugin.getDescription().getVersion() + ", iSnakeBuzz_"));
        }

        return false;
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
