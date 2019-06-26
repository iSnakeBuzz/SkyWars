package com.isnakebuzz.skywars.Utils.Holograms.packetlistening;

import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologram;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologramAPI;
import com.isnakebuzz.skywars.Utils.Holograms.event.TruenoHologramClickEvent;
import com.isnakebuzz.skywars.Utils.Holograms.tinyprotocol.Reflection;
import com.isnakebuzz.skywars.Utils.Holograms.tinyprotocol.TinyProtocol;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import io.netty.channel.*;
import org.bukkit.*;
import org.bukkit.event.*;

import java.util.*;

public class HologramTinyProtocolListener implements HologramPacketListener {

    private static TinyProtocol protocol;
    private static Class<?> EntityInteractClass;
    private static Reflection.FieldAccessor<Integer> EntityID;
    private static ArrayList<Player> playerswhointeract;

    @Override
    public void startListening(final Plugin plugin) {
        if (HologramTinyProtocolListener.protocol == null) {
            HologramTinyProtocolListener.protocol = new TinyProtocol(plugin) {
                @Override
                public Object onPacketInAsync(final Player sender, final Channel channel, final Object packet) {
                    if (HologramTinyProtocolListener.EntityInteractClass.isInstance(packet) && !HologramTinyProtocolListener.playerswhointeract.contains(sender)) {
                        int clickedId = HologramTinyProtocolListener.EntityID.get(packet);
                        for (final TruenoHologram holo : TruenoHologramAPI.getHolograms()) {
                            if (holo.getEntitiesIds().contains(clickedId)) {
                                TruenoHologramClickEvent event = new TruenoHologramClickEvent(sender, holo);
                                Bukkit.getPluginManager().callEvent(event);
                                break;
                            }
                        }
                        HologramTinyProtocolListener.playerswhointeract.add(sender);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> HologramTinyProtocolListener.playerswhointeract.remove(sender), 2L);
                    }
                    return super.onPacketInAsync(sender, channel, packet);
                }
            };
        }
    }

    static {
        HologramTinyProtocolListener.protocol = null;
        HologramTinyProtocolListener.EntityInteractClass = Reflection.getClass("{nms}.PacketPlayInUseEntity");
        HologramTinyProtocolListener.EntityID = Reflection.getField(HologramTinyProtocolListener.EntityInteractClass, Integer.TYPE, 0);
        HologramTinyProtocolListener.playerswhointeract = new ArrayList<Player>();
    }
}
