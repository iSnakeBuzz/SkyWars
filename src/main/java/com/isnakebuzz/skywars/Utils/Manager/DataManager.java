package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Database.System.MySQL;
import com.isnakebuzz.skywars.Database.Versions.vLobby;
import com.isnakebuzz.skywars.Database.Versions.vSolo;
import com.isnakebuzz.skywars.Database.Versions.vTeam;
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
        this.mySQL = new MySQL(plugin);
        mySQL.init();

        if (Statics.skyMode.equals(GameType.SOLO)) {
            this.database = new vSolo(plugin);
        } else if (Statics.skyMode.equals(GameType.TEAM)) {
            this.database = new vTeam(plugin);
        } else if (Statics.skyMode.equals(GameType.LOBBY)) {
            this.database = new vLobby(plugin);
        }

        ConfigurationSection db = plugin.getConfig("Extra/Database").getConfigurationSection("Tables");

        String table_player = db.getString("Player");
        String table_solo = db.getString("Solo");
        String table_team = db.getString("Team");

        mySQL.preparedUpdate("CREATE TABLE IF NOT EXISTS " + table_player + " (UUID VARCHAR(100), Kits Text, SelKit Text, Cages Text, SelCage Text)");
        mySQL.preparedUpdate("CREATE TABLE IF NOT EXISTS " + table_solo + " (UUID VARCHAR(100), Wins Integer, Kills Integer, Deaths Integer)");
        mySQL.preparedUpdate("CREATE TABLE IF NOT EXISTS " + table_team + " (UUID VARCHAR(100), Wins Integer, Kills Integer, Deaths Integer)");

        plugin.log(Statics.logPrefix, "Has been loaded MySQL database");
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Database getDatabase() {
        return database;
    }
}
