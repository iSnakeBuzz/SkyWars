package com.isnakebuzz.skywars.Database;

import java.util.UUID;

public interface IDatabase {

    /**
     * Load player from database.
     *
     * @param uniqueId player identifier.
     * @param username player name.
     */
    void loadPlayer(UUID uniqueId, String username);

    /**
     * Update stats of the specified player.
     *
     * @param uniqueId player identifier
     */
    void updateStats(UUID uniqueId);

}
