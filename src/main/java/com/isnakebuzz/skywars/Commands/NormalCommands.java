package com.isnakebuzz.skywars.Commands;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NormalCommands implements CommandExecutor {

    private SkyWars plugin;

    public NormalCommands(SkyWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("skywars") && sender instanceof Player) {
            sender.sendMessage(c("&aSkyWars &8&l|&e Version: " + plugin.getDescription().getVersion() + ", made by iSnakeBuzz_"));
        }

        return false;
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
