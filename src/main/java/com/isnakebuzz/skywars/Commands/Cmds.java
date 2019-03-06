package com.isnakebuzz.skywars.Commands;

import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Cmds implements CommandExecutor {

    private Main plugin;

    public Cmds(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("skywars") && sender instanceof Player) {
            if (args.length < 1) {
                return true;
            }
            Player p = (Player) sender;
            String subCmds = args[0];

            if (subCmds.equalsIgnoreCase("addSpawn")) {
                try {
                    plugin.getArenaSetup().addSpawn(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (subCmds.equalsIgnoreCase("removeSpawn")) {
                try {
                    plugin.getArenaSetup().removeSpawn(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (subCmds.equalsIgnoreCase("setLobby")) {
                try {
                    plugin.getArenaSetup().setLobbyAndSpect(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (subCmds.equalsIgnoreCase("setArea")) {
                giveWand(p);
            } else if (subCmds.equalsIgnoreCase("setChests")) {
                giveWandChests(p);
            } else if (subCmds.equalsIgnoreCase("setMap")) {
                if (args.length < 2) {
                    return true;
                }

                String args2 = args[1];

                try {
                    plugin.getArenaSetup().setSettings(p, "MapName", args2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (subCmds.equalsIgnoreCase("setMin")) {
                if (args.length < 2) {
                    return true;
                }

                String args2 = args[1];

                try {
                    plugin.getArenaSetup().setSettings(p, "Players.min", Integer.valueOf(args2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (subCmds.equalsIgnoreCase("setMax")) {
                if (args.length < 2) {
                    return true;
                }

                String args2 = args[1];

                try {
                    plugin.getArenaSetup().setSettings(p, "Players.max", Integer.valueOf(args2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (subCmds.equalsIgnoreCase("setStarting")) {
                if (args.length < 2) {
                    return true;
                }

                String args2 = args[1];

                try {
                    plugin.getArenaSetup().setSettings(p, "Timers.Starting", Integer.valueOf(args2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (subCmds.equalsIgnoreCase("setCages")) {
                if (args.length < 2) {
                    return true;
                }

                String args2 = args[1];

                try {
                    plugin.getArenaSetup().setSettings(p, "Timers.CageOpens", Integer.valueOf(args2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (subCmds.equalsIgnoreCase("setEnd")) {
                if (args.length < 2) {
                    return true;
                }

                String args2 = args[1];

                try {
                    plugin.getArenaSetup().setSettings(p, "Timers.End", Integer.valueOf(args2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private void giveWandChests(Player p) {
        p.getInventory().addItem(ItemBuilder.crearItem1(369, 1, 0, "&aCenter Chests", "Left Click to select chest"));
        p.getInventory().addItem(ItemBuilder.crearItem1(280, 1, 0, "&aIsland Chests", "Left Click to select chest"));
    }

    private void giveWand(Player p) {
        p.getInventory().addItem(ItemBuilder.crearItem1(286, 1, 0, "&aWand", "RightClick to select one position", "Left Click to select two position"));
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
