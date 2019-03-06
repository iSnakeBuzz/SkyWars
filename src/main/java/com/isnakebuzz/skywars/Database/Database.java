package com.isnakebuzz.skywars.Database;

import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;

public interface Database {

    void createPlayer(Player p) throws SQLException, IOException;

    void savePlayer(Player p);

    void closeConnection();


}
