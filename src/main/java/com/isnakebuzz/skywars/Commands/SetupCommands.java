package com.isnakebuzz.skywars.Commands;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Cuboids.Cage;
import com.isnakebuzz.skywars.Utils.LocUtils;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SetupCommands implements CommandExecutor {

    private Main plugin;

    public SetupCommands(Main plugin) {
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

            addSpawn(p, cmd, args);
            removeSpawn(p, cmd, args);
            setLobby(p, cmd, args);
            setLobbyArea(p, cmd, args);
            setChests(p, cmd, args);
            setMap(p, cmd, args);
            setMin(p, cmd, args);
            setMax(p, cmd, args);
            setStarting(p, cmd, args);
            setCages(p, cmd, args);
            setEnd(p, cmd, args);
            setRefillTime(p, cmd, args);
            setCenterSchem(p, cmd, args);
            kitCommands(p, cmd, args);
            cagesCmds(p, cmd, args);

        }

        return false;
    }


    void addSpawn(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("addSpawn")) return;
        p.sendMessage(c("&aPlace a beacon in the location to spawn"));
        /*plugin.getArenaSetup().addSpawn(p);*/
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

    void setLobbyArea(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setLobbyArea")) return;
        giveLobbyWand(p);
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

    void setRefillTime(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("setRefillTime")) return;
        if (args.length < 2) {
            return;
        }

        String args2 = args[1];

        try {
            plugin.getArenaSetup().setSettings(p, "Timers.ChestRefill", Integer.valueOf(args2));
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
        if (args.length < 2) {
            p.sendMessage(c("&cCorrect usage: /sw kit {add, list, get, update, remove}"));
            return;
        }

        String cmdType = args[1];

        if (cmdType.equalsIgnoreCase("add")) {
            if (args.length < 6) {
                p.sendMessage(c("&cCorrect usage: /sw kit add {name} {perm/none} {default: true/false} {Glow: true/false}"));
                return;
            }

            //Command kit settings
            String name = args[2];
            String permission = args[3];
            Boolean isDef = Boolean.valueOf(args[4]);
            Boolean glow = Boolean.valueOf(args[5]);

            //Inventory kit settings
            ItemStack[] armorCont = p.getInventory().getArmorContents();
            ItemStack[] invCont = p.getInventory().getContents();

            File kitFile = new File(plugin.getDataFolder() + "/Kits/" + name + ".yml");
            if (kitFile.exists()) {
                p.sendMessage(c("&cKit already exist, use &b/sw kit update {name} {perm/none} {default: true/false} {Glow: true/false}&c to update kit"));
                return;
            }
            try {
                boolean newFile = kitFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Error: " + e.getMessage() + ", see console for more details");
                return;
            }
            YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);

            kitConfig.set("Name", name);
            kitConfig.set("Perm", permission);
            kitConfig.set("Default", isDef);
            kitConfig.set("Selected Glow", glow);

            //Adding default kit logo
            kitConfig.set("Logo.name", "&e" + name);
            kitConfig.set("Logo.item", "1:0");
            kitConfig.set("Logo.amount", 1);
            kitConfig.set("Logo.lore", new String[]{"This is a example kit", "Please change this in kit " + name + ".yml"});

            //Inventory
            kitConfig.set("Armor", armorCont);
            kitConfig.set("Inventory", invCont);

            try {
                kitConfig.save(kitFile);
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Error: " + e.getMessage() + ", see console for more details");
                return;
            } finally {
                p.sendMessage(c("&eHas been created kit &a" + name));
            }
        } else if (cmdType.equalsIgnoreCase("update")) {
            if (args.length < 6) {
                p.sendMessage(c("&cCorrect usage: /sw kit update {name} {perm/none} {default: true/false} {Glow: true/false}"));
                return;
            }

            //Command kit settings
            String name = args[2];
            String permission = args[3];
            Boolean isDef = Boolean.valueOf(args[4]);
            Boolean glow = Boolean.valueOf(args[5]);

            //Inventory kit settings
            ItemStack[] armorCont = p.getInventory().getArmorContents();
            ItemStack[] invCont = p.getInventory().getContents();

            File kitFile = new File(plugin.getDataFolder() + "/Kits/" + name + ".yml");
            try {
                boolean newFile = kitFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Error: " + e.getMessage() + ", see console for more details");
                return;
            }
            YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);

            kitConfig.set("Name", name);
            kitConfig.set("Perm", permission);
            kitConfig.set("Default", isDef);
            kitConfig.set("Selected Glow", glow);

            //Inventory
            kitConfig.set("Armor", armorCont);
            kitConfig.set("Inventory", invCont);

            try {
                kitConfig.save(kitFile);
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Error: " + e.getMessage() + ", see console for more details");
                return;
            } finally {
                p.sendMessage(c("&eHas been updated kit &a" + name));
            }
        } else if (cmdType.equalsIgnoreCase("get")) {
            if (args.length < 3) {
                p.sendMessage(c("&cCorrect usage: /sw kit get {name}"));
                return;
            }
            String name = args[2];
            File kitFile = new File(plugin.getDataFolder() + "/Kits/" + name + ".yml");
            if (!kitFile.exists()) {
                p.sendMessage(c("&cKit does't exist"));
                return;
            }

            YamlConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);

            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.updateInventory();

            ItemStack[] armorCont = (ItemStack[]) ((List) kitConfig.get("Armor")).toArray(new ItemStack[0]);
            ItemStack[] invCont = (ItemStack[]) ((List) kitConfig.get("Inventory")).toArray(new ItemStack[0]);

            p.getInventory().setArmorContents(armorCont);
            p.getInventory().setContents(invCont);

            p.updateInventory();
        } else if (cmdType.equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                p.sendMessage(c("&cCorrect usage: /sw kit remove {name}"));
                return;
            }
            String name = args[2];
            File kitFile = new File(plugin.getDataFolder() + "/Kits/" + name + ".yml");
            if (kitFile.exists()) {
                boolean delete = kitFile.delete();
                p.sendMessage(c("&eKit &a" + name + " &edeleted successfully"));
            } else {
                p.sendMessage(c("&cKit does't exist"));
            }
        }
    }

    void cagesCmds(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("cage")) return;
        if (args.length < 2) {
            p.sendMessage(c("&cCorrect usage: /sw cage {wand, save, load, remove}"));
            return;
        }

        String cmdType = args[1];

        if (cmdType.equalsIgnoreCase("save")) {
            if (args.length < 4) {
                p.sendMessage(c("&cCorrect usage: /sw cage save {name} {default: true/false}"));
                return;
            }

            //Command kit settings
            String name = args[2];
            Boolean isDef = Boolean.valueOf(args[3]);

            if (Statics.cage_loc1 == null && Statics.cage_loc2 == null) {
                p.sendMessage(c("&cPlease select both locations with &6Cagetor &5Wand&c."));
                return;
            } else if (Statics.cage_loc1 == null) {
                p.sendMessage(c("&cPlease select first location with &6Cagetor &5Wand&c."));
                return;
            } else if (Statics.cage_loc2 == null) {
                p.sendMessage(c("&cPlease select second location with &6Cagetor &5Wand&c."));
                return;
            }

            // Verifing if isDef == true. if this true, set default name to cage :)
            if (isDef) name = "default";

            File cageFile = new File(plugin.getDataFolder() + "/Cages/" + name + ".snakeschem");
            if (cageFile.exists()) {
                p.sendMessage(c("&cCage already exist, use &b/sw cage update {name} {default: true/false}&c to update kit"));
                return;
            }

            try {
                boolean newFile = cageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Error: " + e.getMessage() + ", see console for more details");
                return;
            }

            YamlConfiguration cageConfig = YamlConfiguration.loadConfiguration(cageFile);

            List<Block> blocks = LocUtils.getBlocks(Statics.cage_loc1, Statics.cage_loc2);
            List<Integer> blockIDS = LocUtils.getBlocksIDS(blocks);
            List<Vector> blockLocs = Lists.newArrayList();
            List<Byte> blockDATA = LocUtils.getBlocksDATA(blocks);
            Location start = p.getLocation();

            for (Block b : blocks) {
                Vector vector = new Vector(b.getX() - start.getBlockX(), b.getY() - start.getBlockY(), b.getZ() - start.getBlockZ());
                blockLocs.add(vector);
            }

            cageConfig.set("Blocks", blockIDS);
            cageConfig.set("Block IDS", blockDATA);
            cageConfig.set("Locations", blockLocs);

            try {
                cageConfig.save(cageFile);
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Error: " + e.getMessage() + ", see console for more details");
            } finally {
                p.sendMessage(c("&eHas been created Cage &a" + name));
            }
        } else if (cmdType.equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                p.sendMessage(c("&cCorrect usage: /sw cage remove {name}"));
                return;
            }
            String name = args[2];
            File kitFile = new File(plugin.getDataFolder() + "/Cages/" + name + ".snakeschem");
            if (kitFile.exists()) {
                boolean delete = kitFile.delete();
                p.sendMessage(c("&eKit &a" + name + " &edeleted successfully"));
            } else {
                p.sendMessage(c("&cKit does't exist"));
            }
        } else if (cmdType.equalsIgnoreCase("load")) {
            if (args.length < 3) {
                return;
            }
            String name = args[2];
            Cage cage = new Cage(plugin, p.getLocation(), name);
            cage.paste();

            p.sendMessage(c("&eHas been loaded " + name + " cage"));
        } else if (cmdType.equalsIgnoreCase("wand")) {
            giveCagenator(p);
        }
    }

    void example(Player p, String cmd, String[] args) {
        if (!cmd.equalsIgnoreCase("")) return;
    }

    private void giveWandChests(Player p) {
        p.getInventory().addItem(ItemBuilder.crearItem1(369, 1, 0, "&aCenter Chests", "Left Click to select chest"));
        p.getInventory().addItem(ItemBuilder.crearItem1(280, 1, 0, "&aIsland Chests", "Left Click to select chest"));
    }

    private void giveLobbyWand(Player p) {
        p.getInventory().addItem(ItemBuilder.crearItem1(286, 1, 0, "&aLobby Area", "&7RightClick to select one position", "&7Left Click to select two position"));
    }

    private void giveCagenator(Player p) {
        p.getInventory().addItem(ItemBuilder.crearItem1(286, 1, 0, "&6Cagenator", "&6Cagenator is a wand to convert cages into SnakeSchem format.", "", "&7RightClick to select one position", "&7Left Click to select two position"));
    }


    //Send help message
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(c("&aSkyWars &8&l|&e Version: " + plugin.getDescription().getVersion() + ", made by iSnakeBuzz_"));
        sender.sendMessage(c("&6/sw setLobby"));
        sender.sendMessage(c("&6/sw setLobbyArea"));
        sender.sendMessage(c("&6/sw addSpawn"));
        sender.sendMessage(c("&6/sw removeSpawn"));
        sender.sendMessage(c("&6/sw setChests"));
        sender.sendMessage(c("&6/sw setMin {min}"));
        sender.sendMessage(c("&6/sw setMax {max}"));
        sender.sendMessage(c("&6/sw setMap {map name}"));
        sender.sendMessage(c("&6/sw setStarting {starting time}"));
        sender.sendMessage(c("&6/sw setCages {set cage time}"));
        sender.sendMessage(c("&6/sw setEnd {set ending time}"));
        sender.sendMessage(c("&6/sw setRefillTime {set refill time}"));
        sender.sendMessage(c("&6/sw kit"));
        sender.sendMessage(c("&6/sw cages"));
        sender.sendMessage(c(""));
        sender.sendMessage(c("&cEste formato de configuracion es &lBETA&c, ya estoy trabajando en un mejor formato."));
        sender.sendMessage(c(""));
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
