package com.isnakebuzz.skywars.Utils.Holograms.event;

import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologram;
import org.bukkit.event.*;
import org.bukkit.entity.*;

public class TruenoHologramClickEvent extends TruenoHologramEvent {
    private static final HandlerList handlers;

    public HandlerList getHandlers() {
        return TruenoHologramClickEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return TruenoHologramClickEvent.handlers;
    }

    public TruenoHologramClickEvent(final Player player, final TruenoHologram hologram) {
        super(player, hologram);
    }

    static {
        handlers = new HandlerList();
    }
}
