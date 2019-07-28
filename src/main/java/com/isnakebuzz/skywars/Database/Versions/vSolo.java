package com.isnakebuzz.skywars.Database.Versions;

import com.isnakebuzz.skywars.Calls.Callback;
import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Base64Utils;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class vSolo implements Database {

    private Main plugin;

    //Tables
    private String player_table;
    private String stats_table;

    public vSolo(Main plugin) {
        this.plugin = plugin;

        ConfigurationSection db = plugin.getConfig("Extra/Database").getConfigurationSection("Tables");

        this.player_table = db.getString("Player");
        this.stats_table = db.getString("Solo");
    }

    @Override
    public void createPlayer(UUID UUID) {

        existPlayer(UUID, playerExist -> {
            if (!playerExist) {
                cdbPlayer(UUID);
            } else {

                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(UUID);
                plugin.getPlayerManager().addPlayer(UUID, skyPlayer);

                plugin.getDataManager().getMySQL().preparedQuery("SELECT * FROM " + stats_table + " WHERE UUID='" + UUID.toString() + "'", rs -> {
                    try {
                        if (rs.next()) {
                            skyPlayer.setWins(rs.getInt("Wins"));
                            skyPlayer.setKills(rs.getInt("Kills"));
                            skyPlayer.setDeaths(rs.getInt("Deaths"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                plugin.getDataManager().getMySQL().preparedQuery("SELECT * FROM " + player_table + " WHERE UUID='" + UUID.toString() + "'", rs -> {
                    try {
                        if (rs.next()) {
                            skyPlayer.setSelectedKit(rs.getString("SelKit"));
                            skyPlayer.setCageName(rs.getString("SelCage"));
                            List<String> cages = (List<String>) Base64Utils.fromBase64(rs.getString("Cages"));
                            List<String> kits = (List<String>) Base64Utils.fromBase64(rs.getString("Kits"));

                            skyPlayer.setPurchCages(cages);
                            skyPlayer.setPurchKits(kits);
                        }

                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }


                });
            }
        });


    }

    @Override
    public void savePlayer(UUID playerUUID) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(playerUUID);
        String uuid = playerUUID.toString();

        existPlayer(playerUUID, playerExist -> {
            if (!playerExist) return;

            String kitsTodb = Base64Utils.toBase64(skyPlayer.getPurchKits());
            String cagesTodb = Base64Utils.toBase64(skyPlayer.getPurchCages());

            plugin.getDataManager().getMySQL().preparedUpdate("UPDATE " + player_table + " SET " +
                    "Kits='" + kitsTodb + "', " +
                    "SelKit='" + skyPlayer.getSelectedKit() + "', " +
                    "Cages='" + cagesTodb + "', " +
                    "SelCage='" + skyPlayer.getCageName() + "' " +
                    "WHERE UUID='" + uuid + "'"
            );

            plugin.getDataManager().getMySQL().preparedUpdate("UPDATE " + stats_table + " SET " +
                    "Wins='" + skyPlayer.getWins() + "', " +
                    "Kills='" + skyPlayer.getKills() + "', " +
                    "Deaths='" + skyPlayer.getDeaths() + "' " +
                    "WHERE UUID='" + uuid + "'"
            );

            //plugin.getPlayerManager().removePlayer(p);
        });

    }

    @Override
    public void closeConnection() {
        plugin.getDataManager().getMySQL().close();
    }

    private void cdbPlayer(UUID uuid) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(uuid);
        plugin.getPlayerManager().addPlayer(uuid, skyPlayer);

        List<String> kits = new ArrayList<>();
        kits.add("default");

        List<String> cages = new ArrayList<>();
        cages.add("default");

        String kitsTodb = Base64Utils.toBase64(kits);
        String cagesTodb = Base64Utils.toBase64(cages);


        plugin.getDataManager().getMySQL().preparedUpdate(
                "INSERT INTO " + this.player_table + " (UUID, Kits, SelKit, Cages, SelCage) VALUES " +
                        "('" + uuid.toString() + "', " +
                        "'" + kitsTodb + "', " +
                        "'" + skyPlayer.getSelectedKit() + "', " +
                        "'" + cagesTodb + "', " +
                        "'" + skyPlayer.getCageName() + "');"
        );

        plugin.getDataManager().getMySQL().preparedUpdate(
                "INSERT INTO " + this.stats_table + " (UUID, Wins, Kills, Deaths) VALUES " +
                        "('" + uuid.toString() + "', " +
                        "'0', " +
                        "'0', " +
                        "'0');"
        );

    }

    private void existPlayer(UUID uuid, Callback<Boolean> callback) {
        plugin.getDataManager().getMySQL().preparedQuery("SELECT * FROM " + player_table + " WHERE UUID='" + uuid.toString() + "'", rs -> {
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
