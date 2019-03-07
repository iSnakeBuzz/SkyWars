package com.isnakebuzz.skywars.Database.Versions;

import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Calls.Callback;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class vMySQL implements Database {

    private Main plugin;

    public vMySQL(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createPlayer(Player p) {

        playerExist(p, playerExist -> {
            if (!playerExist) {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
                plugin.getPlayerManager().addPlayer(p, skyPlayer);
                plugin.getDataManager().getMySQL().preparedUpdate("INSERT INTO SkyWars (UUID, Wins, Kills, Deaths) VALUES " +
                        "('" + p.getUniqueId().toString() + "', " +
                        "'0', " +
                        "'0', " +
                        "'0');");
            } else {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
                plugin.getDataManager().getMySQL().preparedQuery("SELECT * FROM SkyWars WHERE UUID='" + p.getUniqueId().toString() + "'", rs -> {
                    try {
                        if (rs.next()) {
                            skyPlayer.setWins(rs.getInt("Wins"));
                            skyPlayer.setKills(rs.getInt("Kills"));
                            skyPlayer.setDeaths(rs.getInt("Deaths"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    plugin.getPlayerManager().addPlayer(p, skyPlayer);
                });
            }
        });


    }

    @Override
    public void savePlayer(Player p) {
        SkyPlayer gamePlayer = plugin.getPlayerManager().getPlayer(p);
        String uuid = p.getUniqueId().toString();

        playerExist(p, playerExist -> {
            if (!playerExist) return;


            plugin.getDataManager().getMySQL().preparedUpdate("UPDATE SkyWars SET " +
                    "Wins='" + gamePlayer.getWins() + "', " +
                    "Kills='" + gamePlayer.getKills() + "', " +
                    "Deaths='" + gamePlayer.getDeaths() + "' " +
                    "WHERE UUID='" + uuid + "'"
            );

            plugin.getPlayerManager().removePlayer(p);
        });


    }

    @Override
    public void closeConnection() {
        plugin.getDataManager().getMySQL().close();
    }

    private void playerExist(Player p, Callback<Boolean> callback) {
        plugin.getDataManager().getMySQL().preparedQuery("SELECT * FROM SkyWars WHERE UUID='" + p.getUniqueId().toString() + "'", rs -> {
            Boolean exist = false;
            try {
                exist = (rs != null && rs.next()) && rs.getString("UUID") != null;
            } catch (SQLException e) {
                e.printStackTrace();
                callback.done(exist);
                return;
            } finally {
                callback.done(exist);
            }
        });

    }

}
