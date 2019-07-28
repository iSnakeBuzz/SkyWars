package com.isnakebuzz.skywars.Database;

import java.util.UUID;

public interface Database {

    void createPlayer(UUID p);

    void savePlayer(UUID p);

    void closeConnection();


}
