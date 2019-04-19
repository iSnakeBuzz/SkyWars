package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;
import org.bukkit.ChatColor;

public class TimerManager {

    private Main plugin;

    public TimerManager(Main plugin) {
        this.plugin = plugin;
    }

    public String transformToDate(int seconds) {
        return getDurationString(seconds);
    }

    private String getDurationString(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return twoDigitString2(minutes) + ":" + twoDigitString(seconds);
    }

    public String toSecMs(Long l) {
        double seconds = l / 1000.0;
        return String.format("%.1f", seconds).replaceAll(",", ".");
    }

    private String twoDigitString2(int number) {
        if (number == 0) {
            return "0";
        }
        if (number / 10 == 0) {
            return String.valueOf(number);
        }
        return String.valueOf(number);
    }

    private String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }
        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

    public String getProgressBar(int current, int max, int totalBars, String symbol, String completedColor, String notCompletedColor) {

        float percent = (float) current / max;

        int progressBars = (int) (totalBars * percent);

        int leftOver = (totalBars - progressBars);

        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.translateAlternateColorCodes('&', completedColor));
        for (int i = 0; i < progressBars; i++) {
            sb.append(symbol);
        }
        sb.append(ChatColor.translateAlternateColorCodes('&', notCompletedColor));
        for (int i = 0; i < leftOver; i++) {
            sb.append(symbol);
        }
        return sb.toString();
    }

}
