package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private Main plugin;
    private Map<UUID, SkyPlayer> skyPlayerMap;

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
        this.skyPlayerMap = new HashMap<>();
    }

    public void addPlayer(Player p, SkyPlayer skyPlayer) {
        if (!this.skyPlayerMap.containsKey(p.getUniqueId())) {
            this.skyPlayerMap.put(p.getUniqueId(), skyPlayer);
        }
    }

    /*
     * Removed for good reason, please can't add this :)
     *
     * public void removePlayer(Player p) {
     *   if (this.skyPlayerMap.containsKey(p.getUniqueId())) {
     *      this.skyPlayerMap.remove(p.getUniqueId());
     *   }
     * }
     */

    public boolean containsPlayer(Player p) {
        return this.skyPlayerMap.containsKey(p.getUniqueId());
    }

    public SkyPlayer getPlayer(Player p) {
        return this.skyPlayerMap.getOrDefault(p.getUniqueId(), new SkyPlayer(p.getUniqueId()));
    }

    public Collection<SkyPlayer> getPlayers() {
        return this.skyPlayerMap.values();
    }

}
