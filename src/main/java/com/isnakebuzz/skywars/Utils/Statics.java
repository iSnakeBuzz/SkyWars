package com.isnakebuzz.skywars.Utils;

import com.isnakebuzz.skywars.Utils.Enums.GameType;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;

public class Statics {

    public static String logPrefix = "SkyWars";
    public static GameType skyMode;
    public static String baseMode = "MYSQL";
    public static Boolean isFawe = false;
    public static boolean SnakeGameQueue = false;
    public static String BungeeID = "none";
    public static String mapName = "none";
    public static String API_URL = "http://localhost:3000/games/";
    public static int toRestart = 0;
    public static List<String> lobbies;

    //For setup
    public static String BOT_NAME = "Billy";

    public static Location cage_loc1;
    public static Location cage_loc2;

    public static String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
