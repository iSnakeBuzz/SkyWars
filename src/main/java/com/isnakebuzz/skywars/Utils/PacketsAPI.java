package com.isnakebuzz.skywars.Utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.isnakebuzz.skywars.SkyWars;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketsAPI {

    public static void setSpectating(Player player, Player pSpect, boolean spectating) throws NoSuchFieldException, IllegalAccessException {
        CraftPlayer craftPlayer = (CraftPlayer) player;

        PacketPlayOutCamera packet = new PacketPlayOutCamera();

        if (spectating) {
            Field field = packet.getClass().getDeclaredField("a");
            field.setAccessible(true);
            field.set(packet, pSpect.getEntityId());
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
        } else {
            Field field = packet.getClass().getDeclaredField("a");
            field.setAccessible(true);
            field.set(packet, player.getEntityId());
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void sendTitle(SkyWars plugin, Player p, String title, String subtitle, int fadein, int stay, int fadeout) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            CraftPlayer craftplayer = (CraftPlayer) p;
            PlayerConnection connection = craftplayer.getHandle().playerConnection;
            IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer
                    .a("{'text': '" + ChatColor.translateAlternateColorCodes('&', title) + "'}");
            IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer
                    .a("{'text': '" + ChatColor.translateAlternateColorCodes('&', subtitle) + "'}");
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON, 0,
                    0, 0);
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                    subtitleJSON, 0, 0, 0);

            PacketPlayOutTitle time = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, subtitleJSON, fadein, stay,
                    fadeout);
            connection.sendPacket(time);
            connection.sendPacket(titlePacket);
            connection.sendPacket(subtitlePacket);
        });
    }

    public static void broadcastTitle(SkyWars plugin, String title, String subtitle, int fadein, int stay, int fadeout) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            IChatBaseComponent titleJSON = IChatBaseComponent.ChatSerializer
                    .a("{'text': '" + ChatColor.translateAlternateColorCodes('&', title) + "'}");
            IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer
                    .a("{'text': '" + ChatColor.translateAlternateColorCodes('&', subtitle) + "'}");
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJSON, 0,
                    0, 0);
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                    subtitleJSON, 0, 0, 0);

            PacketPlayOutTitle time = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, subtitleJSON, fadein, stay,
                    fadeout);


            Bukkit.getOnlinePlayers().forEach(player -> {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                connection.sendPacket(titlePacket);
                connection.sendPacket(subtitlePacket);
                connection.sendPacket(time);
            });

        });
    }

    public static void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer
                .a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', msg) + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }

    public static void sendClean(Player p) {
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(false);
        p.setFlying(false);
    }

    public static void setField(String name, Object accessible, Object inject) {
        Field field = null;
        try {
            field = accessible.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(accessible, inject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void connect(SkyWars plugin, Player p, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

}
