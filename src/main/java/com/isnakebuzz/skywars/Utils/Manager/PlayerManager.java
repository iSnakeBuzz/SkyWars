package com.isnakebuzz.skywars.Utils.Manager;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Player.LobbyPlayer;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import lombok.Getter;

import java.util.*;

@Getter
public class PlayerManager {

    private SkyWars plugin;
    private Map<UUID, SkyPlayer> skyPlayerMap;
    private Map<UUID, LobbyPlayer> lobbyPlayerMap;
    private List<UUID> doubleJoinBug;

    public PlayerManager(SkyWars plugin) {
        this.plugin = plugin;
        this.skyPlayerMap = new HashMap<>();
        this.lobbyPlayerMap = new HashMap<>();
        this.doubleJoinBug = Lists.newArrayList();
    }

    public void addPlayer(UUID uuid, SkyPlayer skyPlayer) {
        if (!this.skyPlayerMap.containsKey(uuid)) {
            this.skyPlayerMap.put(uuid, skyPlayer);
            plugin.debug("Adding SkyPlayer(" + uuid.toString() + ") to the RAM section");
        } else {
            plugin.debug("Tried to add SkyPlayer(" + uuid.toString() + ") to the RAM Section but its already exist");
        }
    }

    public boolean containsPlayer(UUID uuid) {
        return this.skyPlayerMap.containsKey(uuid);
    }

    public SkyPlayer getPlayer(UUID uuid) {
        if (this.containsPlayer(uuid)) {
            return this.skyPlayerMap.get(uuid);
        } else return null;
    }

    public Collection<SkyPlayer> getPlayers() {
        return this.skyPlayerMap.values();
    }


    public void removePlayer(UUID uuid) {
        this.skyPlayerMap.remove(uuid);
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
        return this.lobbyPlayerMap.getOrDefault(uuid, null);
    }

    public Collection<LobbyPlayer> getLobbyPlayers() {
        return this.lobbyPlayerMap.values();
    }


    public void reset() {
        this.skyPlayerMap.clear();
        this.lobbyPlayerMap.clear();
    }

}
