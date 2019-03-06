package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Database.Versions.vMongoDB;
import com.isnakebuzz.skywars.Database.Versions.vMySQL;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Statics;

public class DataManager {

    private Main plugin;
    private Database database;

    public DataManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadDatabase() {
        plugin.log(Statics.logPrefix, "Loading database");
        if (Statics.baseMode.equalsIgnoreCase("MONGODB")) {
            this.database = new vMongoDB(plugin);
            plugin.log(Statics.logPrefix, "Has been loaded MongoDB database");
        } else if (Statics.baseMode.equalsIgnoreCase("MYSQL")) {
            this.database = new vMySQL(plugin);
            plugin.log(Statics.logPrefix, "Has been loaded MySQL database");
        }
    }

    public Database getDatabase() {
        return database;
    }
}
