package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Database.System.MySQL;
import com.isnakebuzz.skywars.Database.Versions.MySQL.MyLobby;
import com.isnakebuzz.skywars.Database.Versions.MySQL.MySolo;
import com.isnakebuzz.skywars.Database.Versions.MySQL.MyTeam;
import com.isnakebuzz.skywars.Database.Versions.SnakeAPI.SLobby;
import com.isnakebuzz.skywars.Database.Versions.SnakeAPI.SSolo;
import com.isnakebuzz.skywars.Database.Versions.SnakeAPI.STeam;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.Statics;
import org.bukkit.configuration.ConfigurationSection;

public class DataManager {

    private Main plugin;
    private Database database;

    private MySQL mySQL;

    public DataManager(Main plugin) {
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
            this.database = new MySolo(plugin);
        } else if (Statics.skyMode.equals(GameType.TEAM)) {
            this.database = new MyTeam(plugin);
        } else if (Statics.skyMode.equals(GameType.LOBBY)) {
            this.database = new MyLobby(plugin);
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
            plugin.debug("Has been selected SnakeAPI SOLO");
            this.database = new SSolo(plugin);
        } else if (Statics.skyMode.equals(GameType.TEAM)) {
            plugin.debug("Has been selected SnakeAPI TEAM");
            this.database = new STeam(plugin);
        } else if (Statics.skyMode.equals(GameType.LOBBY)) {
            plugin.debug("Has been selected SnakeAPI Lobby");
            this.database = new SLobby(plugin);
        }
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Database getDatabase() {
        return database;
    }
}
