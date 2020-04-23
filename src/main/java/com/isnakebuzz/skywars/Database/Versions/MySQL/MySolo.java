package com.isnakebuzz.skywars.Database.Versions.MySQL;

import com.isnakebuzz.skywars.Calls.Callback;
import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Base64Utils;
import com.isnakebuzz.skywars.Utils.Cuboids.Cage;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySolo implements Database {

    private Main plugin;

    //Tables
    private String player_table;
    private String stats_table;

    public MySolo(Main plugin) {
        this.plugin = plugin;

        ConfigurationSection db = plugin.getConfig("Extra/Database").getConfigurationSection("Tables");

        this.player_table = db.getString("Player");
        this.stats_table = db.getString("Solo");
    }

    @Override
    public void createPlayer(UUID playerUUID) {
        plugin.debug("Creating player by UUID " + playerUUID.toString());

        existPlayer(playerUUID, playerExist -> {
            if (!playerExist) {
                cdbPlayer(playerUUID);
            } else {

                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(playerUUID);
                plugin.getPlayerManager().addPlayer(playerUUID, skyPlayer);

                plugin.debug("Fetching data: " + playerUUID.toString());

                plugin.getDataManager().getMySQL().preparedQuery("SELECT * FROM " + stats_table + " WHERE UUID='" + playerUUID.toString() + "'", rs -> {
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

                plugin.getDataManager().getMySQL().preparedQuery("SELECT * FROM " + player_table + " WHERE UUID='" + playerUUID.toString() + "'", rs -> {
                    try {
                        if (rs.next()) {
                            String kitName = rs.getString("SelKit");
                            String cageName = rs.getString("SelCage");

                            skyPlayer.setSelectedKit(kitName);
                            skyPlayer.setCageName(cageName);
                            List<String> cages = (List<String>) Base64Utils.fromBase64(rs.getString("Cages"));
                            List<String> kits = (List<String>) Base64Utils.fromBase64(rs.getString("Kits"));

                            Location spawnLocation = plugin.getSkyWarsArena().getSpawnLocations().get(skyPlayer.getTeam().getSpawnID());

                            if (!cageName.equalsIgnoreCase("default")) {
                                Cage cage = new Cage(plugin, spawnLocation, cageName);
                                cage.paste();
                            }

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
    public void savePlayer(Player playerUUID) {
        if (playerUUID == null) return;
        if (!plugin.getPlayerManager().containsPlayer(playerUUID.getUniqueId())) return;

        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(playerUUID.getUniqueId());

        // If staff don't save stats
        if (skyPlayer.isStaff()) return;

        String uuid = playerUUID.getUniqueId().toString();

        existPlayer(playerUUID.getUniqueId(), playerExist -> {
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

            if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.WAITING)) {
                plugin.getPlayerManager().removePlayer(playerUUID.getUniqueId());
                plugin.debug("Removing player(" + playerUUID.getUniqueId().toString() + ") (WAITING status)");
            } else if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.FINISH)) {
                plugin.debug("Removing player(" + playerUUID.getUniqueId().toString() + ") (FINISH status)");
            } else {
                plugin.debug("Saving player(" + playerUUID.getUniqueId().toString() + ") (" + plugin.getSkyWarsArena().getGameStatus().toString() + " status)");
            }
        });

    }

    @Override
    public void closeConnection() {
        plugin.getDataManager().getMySQL().close();
    }

    private void cdbPlayer(UUID uuid) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(uuid);
        //plugin.getPlayerManager().addPlayer(uuid, skyPlayer);

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
            try {
                callback.done((rs != null && rs.next()) && rs.getString("UUID") != null);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.done(false);
                return;
            }
        });

    }

}
