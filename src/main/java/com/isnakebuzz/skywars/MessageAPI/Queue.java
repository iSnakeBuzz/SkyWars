package com.isnakebuzz.skywars.MessageAPI;

import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class Queue implements Messages {

    private final String message;

    @Nullable
    private sSound sound;

    public Queue(String message) {
        this.message = message;
    }

    public Queue(String message, @Nullable sSound sound) {
        this.message = message;
        this.sound = sound;
    }

    @Override
    public void send(TimeUnit unit, long delay) {

        String talkMessage = ("&e[BOT] {botName}&f: " + message)
                .replaceAll("\\{botName}", Statics.BOT_NAME);

        try {
            Bukkit.broadcastMessage(c(talkMessage));
            if (sound != null) sound.play();
            unit.sleep(delay);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
