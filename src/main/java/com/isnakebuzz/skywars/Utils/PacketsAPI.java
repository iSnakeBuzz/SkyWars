package com.isnakebuzz.skywars.Utils;

import com.isnakebuzz.skywars.Main;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PacketsAPI {

    public static void sendTitle(Main plugin, Player p, String title, String subtitle, int fadein, int stay, int fadeout) {
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

    public static void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer
                .a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', msg) + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }

    public static void sendClean(Player p) {
        p.setGameMode(GameMode.SURVIVAL);
        p.setAllowFlight(false);
        p.setFlying(false);
    }

}
