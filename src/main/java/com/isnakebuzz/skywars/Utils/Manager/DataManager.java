package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Database.System.MySQL;
import com.isnakebuzz.skywars.Database.Versions.vMySQL;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Statics;

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
        this.database = new vMySQL(plugin);

        mySQL.preparedUpdate("CREATE TABLE IF NOT EXISTS SkyWars (UUID VARCHAR(100), Wins Integer, Kills Integer, Deaths Integer)");
        plugin.log(Statics.logPrefix, "Has been loaded MySQL database");
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Database getDatabase() {
        return database;
    }
}
