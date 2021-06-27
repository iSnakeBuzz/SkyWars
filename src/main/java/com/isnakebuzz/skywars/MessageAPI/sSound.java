package com.isnakebuzz.skywars.MessageAPI;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class sSound {

    private final Sound sound;
    private final Float volume;
    private final Float pitch;

    public sSound(Sound sound, Float volume, Float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    void play() {
        Bukkit.getOnlinePlayers().forEach(o -> o.playSound(o.getLocation(), sound, volume, pitch));
    }

}
