package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.boydti.fawe.bukkit.chat.Reflection.getNMSClass;

public class Schematic {

    private Main plugin;
    private Class<?> NBTCompressedStreamTools;
    
    public Schematic(Main plugin) {
        this.plugin = plugin;
    }

    public void setupNBT() {
        try {
            NBTCompressedStreamTools = getNMSClass("NBTCompressedStreamTools");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void pasteSchematic(File file, Location schematicLocation, boolean delete) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
        FileInputStream fis = new FileInputStream(file);
        Object nbtData = NBTCompressedStreamTools.getMethod("a", InputStream.class).invoke(null, fis);
        Method getShort = nbtData.getClass().getMethod("getShort", String.class);
        Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);

        short width = ((short) getShort.invoke(nbtData, "Width"));
        short height = ((short) getShort.invoke(nbtData, "Height"));
        short length = ((short) getShort.invoke(nbtData, "Length"));

        byte[] blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));
        byte[] data = ((byte[]) getByteArray.invoke(nbtData, "Data"));

        fis.close();
        boolean first = true;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    int b = blocks[index] & 0xFF;
                    //Material m = Material.getMaterial(b);
                    if (!delete) {
                        Block block = schematicLocation.getWorld().getBlockAt(x, y, z);
                        block.setTypeIdAndData(b, data[index], true);
                    } else {
                        schematicLocation.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }
            }
        }
    }

    private Class<?> getNMSClass(String string) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + string);
    }

}
