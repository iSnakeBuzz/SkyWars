package com.isnakebuzz.skywars.Database.Versions;

import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Database.System.MySQL;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class vMySQL implements Database {

    private Main plugin;

    public vMySQL(Main plugin) {
        this.plugin = plugin;
        this.loadMySQL();
    }

    @Override
    public void createPlayer(Player p) throws SQLException {
        if (!playerExist(p)) {
            SkyPlayer skyPlayer = new SkyPlayer(p.getUniqueId());
            plugin.getPlayerManager().addPlayer(p, skyPlayer);
            MySQL.update("INSERT INTO SkyWars (UUID, Wins, Kills, Deaths) VALUES " +
                    "('" + p.getUniqueId() + "', " +
                    "'0', " +
                    "'0', " +
                    "'0');");
        } else {
            SkyPlayer skyPlayer = new SkyPlayer(p.getUniqueId());
            ResultSet snakeSgResult = MySQL.query("SELECT * FROM SkyWars WHERE UUID='" + p.getUniqueId() + "'");
            if (snakeSgResult.next()) {
                skyPlayer.setWins(snakeSgResult.getInt("Wins"));
                skyPlayer.setKills(snakeSgResult.getInt("Kills"));
                skyPlayer.setDeaths(snakeSgResult.getInt("Deaths"));
            }
            plugin.getPlayerManager().addPlayer(p, skyPlayer);
        }
    }

    @Override
    public void savePlayer(Player p) {
        if (playerExist(p)) {
            SkyPlayer gamePlayer = plugin.getPlayerManager().getPlayer(p);

            MySQL.update("UPDATE SkyWars SET " +
                    "Wins='" + gamePlayer.getWins() + "', " +
                    "Kills='" + gamePlayer.getKills() + "', " +
                    "Deaths='" + gamePlayer.getDeaths() + "' " +
                    "WHERE UUID='" + p.getUniqueId() + "'"
            );

            plugin.getPlayerManager().removePlayer(p);
        }
    }

    @Override
    public void closeConnection() {
        try {
            MySQL.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadMySQL() {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Database");
        MySQL.host = config.getString("MySQL.hostname");
        MySQL.port = config.getInt("MySQL.port");
        MySQL.database = config.getString("MySQL.database");
        MySQL.username = config.getString("MySQL.username");
        MySQL.password = config.getString("MySQL.password");
        MySQL.isEnabled = config.getBoolean("MySQL.enabled");

        MySQL.connect(plugin);
        MySQL.update("CREATE TABLE IF NOT EXISTS SkyWars (UUID VARCHAR(100), Wins Integer, Kills Integer, Deaths Integer)");
    }

    private boolean playerExist(Player p) {
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM SkyWars WHERE UUID='" + p.getUniqueId() + "'");
            return (rs != null && rs.next()) && rs.getString("UUID") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
