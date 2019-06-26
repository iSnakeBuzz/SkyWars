package com.isnakebuzz.skywars.Utils.Holograms.event;

import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologram;
import org.bukkit.event.*;
import org.bukkit.entity.*;

public abstract class TruenoHologramEvent extends Event {
    private Player player;
    private TruenoHologram hologram;

    public TruenoHologramEvent(final Player player, final TruenoHologram hologram) {
        this.player = player;
        this.hologram = hologram;
    }

    public Player getPlayer() {
        return this.player;
    }

    public TruenoHologram getHologram() {
        return this.hologram;
    }
}
