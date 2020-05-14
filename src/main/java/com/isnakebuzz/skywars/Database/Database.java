package com.isnakebuzz.skywars.Database;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface Database {

    void createPlayer(UUID p);

    void savePlayer(Player p);

    void closeConnection();
}
