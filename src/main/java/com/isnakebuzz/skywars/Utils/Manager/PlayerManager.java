package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.LobbyPlayer;
import com.isnakebuzz.skywars.Player.SkyPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private Main plugin;
    private Map<UUID, SkyPlayer> skyPlayerMap;
    private Map<UUID, LobbyPlayer> lobbyPlayerMap;


    public PlayerManager(Main plugin) {
        this.plugin = plugin;
        this.skyPlayerMap = new HashMap<>();
        this.lobbyPlayerMap = new HashMap<>();
    }

    public void addPlayer(UUID uuid, SkyPlayer skyPlayer) {
        if (!this.skyPlayerMap.containsKey(uuid)) {
            this.skyPlayerMap.put(uuid, skyPlayer);
        }
    }

    public boolean containsPlayer(UUID uuid) {
        return this.skyPlayerMap.containsKey(uuid);
    }

    public SkyPlayer getPlayer(UUID uuid) {
        return this.skyPlayerMap.getOrDefault(uuid, new SkyPlayer(uuid));
    }

    public Collection<SkyPlayer> getPlayers() {
        return this.skyPlayerMap.values();
    }


    //Lobby
    public void addLbPlayer(UUID uuid, LobbyPlayer skyPlayer) {
        if (!this.lobbyPlayerMap.containsKey(uuid)) {
            this.lobbyPlayerMap.put(uuid, skyPlayer);
        }
    }

    public void removeLbPlayer(UUID uuid) {
        this.lobbyPlayerMap.remove(uuid);
    }

    public boolean containsLbPlayer(UUID uuid) {
        return this.lobbyPlayerMap.containsKey(uuid);
    }

    public LobbyPlayer getLbPlayer(UUID uuid) {
        return this.lobbyPlayerMap.getOrDefault(uuid, new LobbyPlayer(uuid));
    }

    public Collection<LobbyPlayer> getLobbyPlayers() {
        return this.lobbyPlayerMap.values();
    }


    public void reset() {
        this.skyPlayerMap.clear();
        this.lobbyPlayerMap.clear();
    }

}
