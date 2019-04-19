package com.isnakebuzz.skywars.Commands;

import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetupCommands implements CommandExecutor {

    private Main plugin;

    public SetupCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("skywars") && sender instanceof Player) {
            if (args.length < 1) {
                return true;
            }
            Player p = (Player) sender;
            String cmd = args[0];

            addSpawn(p, cmd, args);
            removeSpawn(p, cmd, args);
            setLobby(p, cmd, args);
            setArea(p, cmd, args);
            setChests(p, cmd, args);
            setMap(p, cmd, args);
            setMin(p, cmd, args);
            setMax(p, cmd, args);
            setStarting(p, cmd, args);
            setCages(p, cmd, args);
            setEnd(p, cmd, args);
            setCenterSchem(p, cmd, args);

        }

        return false;
    }


    void addSpawn(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("addSpawn")) return;
        try {
            plugin.getArenaSetup().addSpawn(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void removeSpawn(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("removeSpawn")) return;
        try {
            plugin.getArenaSetup().removeSpawn(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setLobby(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setLobby")) return;
        try {
            plugin.getArenaSetup().setLobbyAndSpect(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setArea(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setArea")) return;
        giveWand(p);
    }

    void setChests(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setChests")) return;
        giveWandChests(p);
    }

    void setMap(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setMap")) return;
        if (args.length < 2) {
            return;
        }

        String args2 = args[1];

        try {
            plugin.getArenaSetup().setSettings(p, "MapName", args2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setMin(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setMin")) return;
        if (args.length < 2) {
            return;
        }

        String args2 = args[1];

        try {
            plugin.getArenaSetup().setSettings(p, "Players.min", Integer.valueOf(args2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setMax(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setMax")) return;
        if (args.length < 2) {
            return;
        }

        String args2 = args[1];

        try {
            plugin.getArenaSetup().setSettings(p, "Players.max", Integer.valueOf(args2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setStarting(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setStarting")) return;
        if (args.length < 2) {
            return;
        }

        String args2 = args[1];

        try {
            plugin.getArenaSetup().setSettings(p, "Timers.Starting", Integer.valueOf(args2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setCages(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setCages")) return;
        if (args.length < 2) {
            return;
        }

        String args2 = args[1];

        try {
            plugin.getArenaSetup().setSettings(p, "Timers.CageOpens", Integer.valueOf(args2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setEnd(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setEnd")) return;
        if (args.length < 2) {
            return;
        }

        String args2 = args[1];

        try {
            plugin.getArenaSetup().setSettings(p, "Timers.End", Integer.valueOf(args2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setCenterSchem(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setCenterSchem")) return;
        try {
            plugin.getArenaSetup().setCenterSchematic(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void kitCommands(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("kit")) return;
        String subCmds = args[1];

    }

    void example(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("")) return;
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
