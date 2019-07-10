package com.isnakebuzz.skywars.Schematics;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Schematics.Utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SchematicManager {

    private Inventory inv;
    private Main plugin;
    private HashMap<Player, ArrayList<String>> schemas;

    public SchematicManager(final Main plugin) {
        this.inv = null;
        this.plugin = plugin;
        this.schemas = new HashMap<Player, ArrayList<String>>();
    }

    public boolean load(final String name, final Player p) {
        if (new File(String.valueOf(this.plugin.getDataFolder().getAbsolutePath()) + "/schemas/", String.valueOf(name) + ".schema").exists()) {
            final ArrayList<String> list = this.getStringListFromFile(name);
            if (list != null && list.size() > 0) {
                this.schemas.put(p, list);
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getStringListFromFile(final String filename) {
        final File f = new File(String.valueOf(this.plugin.getDataFolder().getAbsolutePath()) + "/schemas/", String.valueOf(filename) + ".schema");
        FileReader fr = null;
        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final BufferedReader br = new BufferedReader(fr);
        final ArrayList<String> list = new ArrayList<String>();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return list;
    }

    public boolean delete(final String filename) {
        final File f = new File(String.valueOf(this.plugin.getDataFolder().getAbsolutePath()) + "/schemas/", String.valueOf(filename) + ".schema");
        if (f.exists()) {
            f.delete();
            return true;
        }
        return false;
    }

    public boolean list(final Player p) {
        final File folder = new File(String.valueOf(this.plugin.getDataFolder().getAbsolutePath()) + "/schemas/");
        final File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; ++i) {
            if (listOfFiles.length <= 0) {
                p.sendMessage("§cYou didn't save any files yet!");
            } else if (listOfFiles[i].isFile()) {
                p.sendMessage("§a- §b" + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                p.sendMessage("§7- §8" + listOfFiles[i].getName() + " §7(Dir)");
                return true;
            }
        }
        return false;
    }

    public boolean save(final String filename, final ArrayList<String> list) {
        final File f = new File(String.valueOf(this.plugin.getDataFolder().getAbsolutePath()) + "/schemas/", String.valueOf(filename) + ".schema");
        final String newLine = System.getProperty("line.separator");
        if (f.exists()) {
            return false;
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (final String s : list) {
                bw.write(s);
                bw.write(newLine);
            }
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> fromBlockListToStringList(final ArrayList<Block> blocks, final Location start) {
        final ArrayList<String> list = new ArrayList<String>();
        blocks.forEach(b -> list.add(this.blockToString(b, start)));
        return list;
    }

    public ArrayList<Block> getBlocks(final Location l1, final Location l2) {
        return new Cuboid(l1, l2).getBlocks();
    }

    public HashMap<Player, ArrayList<String>> getSchematics() {
        return this.schemas;
    }

    public String getDirection(final double rot) {
        if (0.0 <= rot && rot < 22.5) {
            return "North";
        }
        if (67.5 <= rot && rot < 112.5) {
            return "East";
        }
        if (157.5 <= rot && rot < 202.5) {
            return "South";
        }
        if (247.5 <= rot && rot < 292.5) {
            return "West";
        }
        if (337.5 <= rot && rot < 360.0) {
            return "North";
        }
        return null;
    }

    public boolean paste(final Player p) {
        if (this.schemas.containsKey(p)) {
            final ArrayList<String> list = this.schemas.get(p);
            if (this.plugin.getConfig().getBoolean("schema.targetblock")) {
                final Location loc0 = p.getTargetBlock((Set) null, 40).getLocation();
                for (final String str : list) {
                    final String[] s = str.split(":");
                    final int x = Integer.parseInt(s[0]);
                    final int y = Integer.parseInt(s[1]);
                    final int z = Integer.parseInt(s[2]);
                    final int id = Integer.parseInt(s[3]);
                    final byte data = Byte.parseByte(s[4]);
                    loc0.clone().add((double) x, (double) y, (double) z).getBlock().setTypeIdAndData(id, data, true);
                }
            } else if (!this.plugin.getConfig().getBoolean("schema.targetblock")) {
                final Location loc2 = p.getLocation();
                for (final String str : list) {
                    final String[] s = str.split(":");
                    final int x = Integer.parseInt(s[0]);
                    final int y = Integer.parseInt(s[1]);
                    final int z = Integer.parseInt(s[2]);
                    final int id = Integer.parseInt(s[3]);
                    final byte data = Byte.parseByte(s[4]);
                    loc2.clone().add((double) x, (double) y, (double) z).getBlock().setTypeIdAndData(id, data, true);
                }
            }
            return true;
        }
        return false;
    }

    public String blockToString(final Block b, final Location start) {
        final int diffx = b.getX() - start.getBlockX();
        final int diffy = b.getY() - start.getBlockY();
        final int diffz = b.getZ() - start.getBlockZ();
        return String.valueOf(diffx) + ":" + diffy + ":" + diffz + ":" + b.getTypeId() + ":" + b.getData();
    }
}
