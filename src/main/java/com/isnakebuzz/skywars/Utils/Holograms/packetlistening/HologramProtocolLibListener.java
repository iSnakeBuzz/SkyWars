package com.isnakebuzz.skywars.Utils.Holograms.packetlistening;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologram;
import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologramAPI;
import com.isnakebuzz.skywars.Utils.Holograms.event.TruenoHologramClickEvent;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.*;

import java.util.*;

public class HologramProtocolLibListener implements HologramPacketListener {

    private static ProtocolManager protocolManager;
    private static ArrayList<Player> playerswhointeract;

    public static void setup() {
        HologramProtocolLibListener.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void startListening(final Plugin plugin) {
        HologramProtocolLibListener.protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
                    final Player p = event.getPlayer();
                    try {
                        final PacketContainer packet = event.getPacket();
                        final int id = (int) packet.getIntegers().read(0);
                        if (!HologramProtocolLibListener.playerswhointeract.contains(p)) {
                            for (final TruenoHologram holo : TruenoHologramAPI.getHolograms()) {
                                if (holo.getEntitiesIds().contains(id)) {
                                    final TruenoHologramClickEvent clickevent = new TruenoHologramClickEvent(p, holo);
                                    Bukkit.getPluginManager().callEvent(clickevent);
                                    break;
                                }
                            }
                            HologramProtocolLibListener.playerswhointeract.add(p);
                            Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> HologramProtocolLibListener.playerswhointeract.remove(p), 2L);
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        });
    }

    static {
        HologramProtocolLibListener.playerswhointeract = new ArrayList<>();
    }
}
