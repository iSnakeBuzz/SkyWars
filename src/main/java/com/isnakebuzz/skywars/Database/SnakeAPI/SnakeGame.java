package com.isnakebuzz.skywars.Database.SnakeAPI;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isnakebuzz.netcore.Utils.Enums.MethodType;
import com.isnakebuzz.netcore.Utils.Network.Request;
import com.isnakebuzz.skywars.Database.IDatabase;
import com.isnakebuzz.skywars.Database.Utils.RoutePath;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Console;
import com.isnakebuzz.skywars.Utils.Statics;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class SnakeGame implements IDatabase {

    private final SkyWars plugin;
    private final RoutePath modeType;
    private final Gson gson;

    public SnakeGame(SkyWars plugin, RoutePath modeType) {
        this.plugin = plugin;
        this.modeType = modeType;
        this.gson = new Gson();
    }

    @SuppressWarnings("UnstableApiUsage")
    @SneakyThrows
    @Override
    public void loadPlayer(UUID uniqueId, String username) {
        SkyPlayer skyPlayer = new SkyPlayer(uniqueId, username);
        plugin.getPlayerManager().addPlayer(uniqueId, skyPlayer);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", uniqueId.toString());

        Request playerReq = new Request().body(jsonObject).methodType(MethodType.GET).url(String.format("%s%s", Statics.API_URL, RoutePath.PLAYER.getPath()));
        JsonObject playerRes = playerReq.execute();

        if (playerRes == null || playerRes.has("status") ) {
            Console.debug(String.format("Player(%s) do not exist.", username));
            return;
        }

        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
        };

        skyPlayer.setPurchKits(gson.fromJson(playerRes.get("kits").getAsJsonArray(), typeToken.getType()));
        skyPlayer.setPurchCages(gson.fromJson(playerRes.get("cages").getAsJsonArray(), typeToken.getType()));
        skyPlayer.setSelectedKit(playerRes.get("selKit").getAsString());
        skyPlayer.setCageName(playerRes.get("selCage").getAsString());

        Request statsReq = new Request().body(jsonObject).methodType(MethodType.GET).url(String.format("%s%s", Statics.API_URL, modeType.getPath()));
        JsonObject statsRes = statsReq.execute();

        skyPlayer.setWins(statsRes.get("wins").getAsInt());
        skyPlayer.setKills(statsRes.get("kills").getAsInt());
        skyPlayer.setDeaths(statsRes.get("deaths").getAsInt());
    }

    @Override
    public void updateStats(UUID uniqueId) {
        if (!plugin.getPlayerManager().containsPlayer(uniqueId)) return;

        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(uniqueId);

        if (skyPlayer.getName().equals("nonexistent")) return;

        JsonObject statsJson = new JsonObject();
        statsJson.addProperty("uuid", uniqueId.toString());
        statsJson.addProperty("username", skyPlayer.getName());
        statsJson.addProperty("wins", skyPlayer.getWins());
        statsJson.addProperty("kills", skyPlayer.getKills());
        statsJson.addProperty("deaths", skyPlayer.getDeaths());

        Request swPlayerStats = new Request().body(statsJson).methodType(MethodType.PUT).url(String.format("%s%s", Statics.API_URL, modeType.getPath()));

        try {
            swPlayerStats.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
