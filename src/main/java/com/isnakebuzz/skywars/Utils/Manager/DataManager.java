package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Database.IDatabase;
import com.isnakebuzz.skywars.Database.SnakeAPI.SLobby;
import com.isnakebuzz.skywars.Database.SnakeAPI.SnakeGame;
import com.isnakebuzz.skywars.Database.Utils.RoutePath;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Console;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.Statics;

public class DataManager {

    private final SkyWars plugin;
    private IDatabase IDatabase;

    public DataManager(SkyWars plugin) {
        this.plugin = plugin;
    }

    public void loadDatabase() {
        Console.log(Statics.logPrefix, "Loading database");
        this.loadSnakeAPI();
        Console.log(Statics.logPrefix, "Has been loaded MySQL database");
    }

    private void loadSnakeAPI() {
        if (Statics.skyMode.equals(GameType.SOLO)) {
            Console.debug("Has been selected SnakeAPI SOLO");
            this.IDatabase = new SnakeGame(plugin, RoutePath.SOLO);
        } else if (Statics.skyMode.equals(GameType.TEAM)) {
            Console.debug("Has been selected SnakeAPI TEAM");
            this.IDatabase = new SnakeGame(plugin, RoutePath.TEAM);
        } else if (Statics.skyMode.equals(GameType.LOBBY)) {
            Console.debug("Has been selected SnakeAPI Lobby");
            this.IDatabase = new SLobby(plugin);
        }
    }

    public IDatabase getDatabase() {
        return IDatabase;
    }
}
