package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Database.IDatabase;
import com.isnakebuzz.skywars.Database.System.MySQL;
import com.isnakebuzz.skywars.Database.Utils.RoutePath;
import com.isnakebuzz.skywars.Database.Versions.MySQL.MyLobby;
import com.isnakebuzz.skywars.Database.Versions.MySQL.MySolo;
import com.isnakebuzz.skywars.Database.Versions.MySQL.MyTeam;
import com.isnakebuzz.skywars.Database.SnakeAPI.SLobby;
import com.isnakebuzz.skywars.Database.SnakeAPI.SnakeGame;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Console;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.configuration.ConfigurationSection;

public class DataManager {

    private SkyWars plugin;
    private IDatabase IDatabase;

    private MySQL mySQL;

    public DataManager(SkyWars plugin) {
        this.plugin = plugin;
    }

    public void loadDatabase() {
        plugin.log(Statics.logPrefix, "Loading database");

        String type = Statics.baseMode;

        if (type.equalsIgnoreCase("CUSTOM")) {
            this.loadSnakeAPI();
        } else {
            this.loadMySQL();
        }

        plugin.log(Statics.logPrefix, "Has been loaded MySQL database");
    }

    private void loadMySQL() {
        this.mySQL = new MySQL(plugin);
        mySQL.init();

        if (Statics.skyMode.equals(GameType.SOLO)) {
            this.IDatabase = new MySolo(plugin);
        } else if (Statics.skyMode.equals(GameType.TEAM)) {
            this.IDatabase = new MyTeam(plugin);
        } else if (Statics.skyMode.equals(GameType.LOBBY)) {
            this.IDatabase = new MyLobby(plugin);
        }

        ConfigurationSection db = plugin.getConfig("Extra/Database").getConfigurationSection("Tables");

        String table_player = db.getString("Player");
        String table_solo = db.getString("Solo");
        String table_team = db.getString("Team");

        mySQL.preparedUpdate("CREATE TABLE IF NOT EXISTS " + table_player + " (UUID VARCHAR(100), Kits Text, SelKit Text, Cages Text, SelCage Text)");
        mySQL.preparedUpdate("CREATE TABLE IF NOT EXISTS " + table_solo + " (UUID VARCHAR(100), Wins Integer, Kills Integer, Deaths Integer)");
        mySQL.preparedUpdate("CREATE TABLE IF NOT EXISTS " + table_team + " (UUID VARCHAR(100), Wins Integer, Kills Integer, Deaths Integer)");
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

    public MySQL getMySQL() {
        return mySQL;
    }

    public IDatabase getDatabase() {
        return IDatabase;
    }
}
