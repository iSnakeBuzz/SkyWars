package com.isnakebuzz.skywars.Database.Versions;

import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Calls.Callback;
import com.isnakebuzz.skywars.Utils.Base64Utils;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class vMySQL implements Database {

    private Main plugin;

    public vMySQL(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createPlayer(Player p) {

        playerExist(p, playerExist -> {
            if (!playerExist) {
                cdbPlayer(p);
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
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
        String uuid = p.getUniqueId().toString();

        playerExist(p, playerExist -> {
            if (!playerExist) return;

            String kitsTodb = Base64Utils.toBase64(skyPlayer.getPurchKits());
            String cagesTodb = Base64Utils.toBase64(skyPlayer.getPurchCages());

            plugin.getDataManager().getMySQL().preparedUpdate("UPDATE SkyWars SET " +
                    "Kits='" + kitsTodb + "', " +
                    "SelKit='" + skyPlayer.getSelectedKit() + "', " +
                    "Cages='" + cagesTodb + "', " +
                    "SelCage='" + skyPlayer.getCageName() + "', " +
                    "Wins='" + skyPlayer.getWins() + "', " +
                    "Kills='" + skyPlayer.getKills() + "', " +
                    "Deaths='" + skyPlayer.getDeaths() + "' " +
                    "WHERE UUID='" + uuid + "'"
            );

            plugin.getPlayerManager().removePlayer(p);
        });


    }

    @Override
    public void closeConnection() {
        plugin.getDataManager().getMySQL().close();
    }

    private void cdbPlayer(Player p) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
        plugin.getPlayerManager().addPlayer(p, skyPlayer);

        List<String> kits = new ArrayList<>();
        kits.add("default");

        List<String> cages = new ArrayList<>();
        cages.add("default");

        String kitsTodb = Base64Utils.toBase64(kits);
        String cagesTodb = Base64Utils.toBase64(cages);


        plugin.getDataManager().getMySQL().preparedUpdate(
                "INSERT INTO SkyWars (UUID, Kits, SelKit, Cages, SelCage, Wins, Kills, Deaths) VALUES " +
                        "('" + p.getUniqueId().toString() + "', " +
                        "'" + kitsTodb + "', " +
                        "'" + skyPlayer.getSelectedKit() + "', " +
                        "'" + cagesTodb + "', " +
                        "'" + skyPlayer.getCageName() + "', " +
                        "'0', " +
                        "'0', " +
                        "'0');"
        );
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
