package com.isnakebuzz.skywars.Database.SnakeAPI;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isnakebuzz.netcore.Utils.Enums.MethodType;
import com.isnakebuzz.netcore.Utils.Network.Request;
import com.isnakebuzz.skywars.Database.IDatabase;
import com.isnakebuzz.skywars.Database.Utils.RoutePath;
import com.isnakebuzz.skywars.Player.LobbyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Console;
import com.isnakebuzz.skywars.Utils.Statics;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"UnstableApiUsage", "DuplicatedCode"})
public class SLobby implements IDatabase {

    private final SkyWars plugin;
    private final Gson gson;

    public SLobby(SkyWars plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
    }

    @SneakyThrows
    @Override
    public void loadPlayer(UUID uniqueId, String username) {
        LobbyPlayer skyPlayer = new LobbyPlayer(uniqueId, username);
        plugin.getPlayerManager().addLbPlayer(uniqueId, skyPlayer);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", uniqueId.toString());

        String url = String.format("%s%s", Statics.API_URL, RoutePath.PLAYER.getPath());

        Request playerReq = new Request().body(jsonObject).methodType(MethodType.GET).url(url);
        JsonObject playerRes = playerReq.execute();

        if (playerRes == null || playerRes.has("status")) {
            Console.debug(String.format("Player(%s) do not exist.", uniqueId));
            return;
        }

        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
        };
        skyPlayer.setPurchKits(gson.fromJson(playerRes.get("kits").getAsJsonArray(), typeToken.getType()));
        skyPlayer.setPurchCages(gson.fromJson(playerRes.get("cages").getAsJsonArray(), typeToken.getType()));
        skyPlayer.setSelectedKit(playerRes.get("selKit").getAsString());
        skyPlayer.setCageName(playerRes.get("selCage").getAsString());


        /* Getting solo teams */
        Request statsSoloReq = new Request().body(jsonObject).methodType(MethodType.GET).url(String.format("%s%s", Statics.API_URL, RoutePath.SOLO));
        JsonObject statsSoloRes = statsSoloReq.execute();

        if (statsSoloRes != null && statsSoloRes.get("status").getAsInt() != 404) {
            Console.debug("SOLO STATS: " + statsSoloRes);

            skyPlayer.setSolo_wins(statsSoloRes.get("wins").getAsInt());
            skyPlayer.setSolo_kills(statsSoloRes.get("kills").getAsInt());
            skyPlayer.setSolo_deaths(statsSoloRes.get("deaths").getAsInt());
        }

        /* Getting solo teams */
        Request statsTeamReq = new Request().body(jsonObject).methodType(MethodType.GET).url(String.format("%s%s", Statics.API_URL, RoutePath.TEAM));
        JsonObject statsTeamRes = statsTeamReq.execute();

        if (statsTeamRes != null && statsTeamRes.get("status").getAsInt() != 404) {
            Console.debug("SOLO STATS: " + statsTeamRes);
            skyPlayer.setTeam_wins(statsTeamRes.get("wins").getAsInt());
            skyPlayer.setTeam_kills(statsTeamRes.get("kills").getAsInt());
            skyPlayer.setTeam_deaths(statsTeamRes.get("deaths").getAsInt());
        }

    }

    @Override
    public void updateStats(UUID uniqueId) {
        if (!plugin.getPlayerManager().containsLbPlayer(uniqueId)) return;

        LobbyPlayer skyPlayer = plugin.getPlayerManager().getLbPlayer(uniqueId);

        if (skyPlayer.getName().equals("nonexistent")) return;

        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
        };

        JsonObject playerJson = new JsonObject();

        playerJson.addProperty("uuid", uniqueId.toString());
        playerJson.addProperty("username", skyPlayer.getName());
        playerJson.add("kits", gson.toJsonTree(skyPlayer.getPurchCages(), typeToken.getType()));
        playerJson.add("cages", gson.toJsonTree(skyPlayer.getPurchCages(), typeToken.getType()));
        playerJson.addProperty("selKit", skyPlayer.getSelectedKit());
        playerJson.addProperty("selCage", skyPlayer.getCageName());

        Request playerStats = new Request().body(playerJson).methodType(MethodType.PUT).url(String.format("%s%s", Statics.API_URL, RoutePath.PLAYER));

        try {
            playerStats.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonObject statsJson = new JsonObject();
        statsJson.addProperty("uuid", uniqueId.toString());
        statsJson.addProperty("username", skyPlayer.getName());
        statsJson.addProperty("wins", skyPlayer.getSolo_wins());
        statsJson.addProperty("kills", skyPlayer.getSolo_kills());
        statsJson.addProperty("deaths", skyPlayer.getSolo_deaths());

        Request swPlayerStats = new Request().body(statsJson).methodType(MethodType.PUT).url(String.format("%s%s", Statics.API_URL, RoutePath.SOLO));

        try {
            swPlayerStats.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Teams
        JsonObject statsTeamJson = new JsonObject();
        statsTeamJson.addProperty("uuid", uniqueId.toString());
        statsTeamJson.addProperty("username", skyPlayer.getName());
        statsJson.addProperty("wins", skyPlayer.getTeam_wins());
        statsJson.addProperty("kills", skyPlayer.getTeam_kills());
        statsJson.addProperty("deaths", skyPlayer.getTeam_deaths());

        Request swTeamStats = new Request().body(statsTeamJson).methodType(MethodType.PUT).url(String.format("%s%s", Statics.API_URL, RoutePath.TEAM));

        try {
            swTeamStats.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
