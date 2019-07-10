package com.isnakebuzz.skywars.Utils.Schematics;

import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;


public class SchemUtils {

    public static Schematic loadSchematic(File file) {
        if (!file.exists())
            return null;
        try {
            FileInputStream stream = new FileInputStream(file);
            NBTTagCompound nbtdata = NBTCompressedStreamTools.a(stream);

            short width = nbtdata.getShort("Width");
            short height = nbtdata.getShort("Height");
            short length = nbtdata.getShort("Length");

            byte[] blocks = nbtdata.getByteArray("Blocks");
            byte[] data = nbtdata.getByteArray("Data");

            byte[] addId = new byte[0];

            if (nbtdata.hasKey("AddBlocks")) {
                addId = nbtdata.getByteArray("AddBlocks");
            }

            short[] sblocks = new short[blocks.length];
            for (int index = 0; index < blocks.length; index++) {
                if ((index >> 1) >= addId.length) {
                    sblocks[index] = (short) (blocks[index] & 0xFF);
                } else {
                    if ((index & 1) == 0) {
                        sblocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blocks[index] & 0xFF));
                    } else {
                        sblocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blocks[index] & 0xFF));
                    }
                }
            }

            stream.close();
            return new Schematic(sblocks, data, width, length, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
