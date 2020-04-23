package com.isnakebuzz.skywars.Database.Versions.SnakeAPI;

import com.google.gson.JsonObject;
import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Database.System.SnakeAPI;
import com.isnakebuzz.skywars.Database.Utils.RoutePath;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.LobbyPlayer;
import com.isnakebuzz.skywars.Utils.Base64Utils;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class SLobby implements Database {

    private Main plugin;
    private SnakeAPI snakeAPI;

    public SLobby(Main plugin) {
        this.plugin = plugin;
        this.snakeAPI = new SnakeAPI(plugin);
    }

    @SneakyThrows
    @Override
    public void createPlayer(UUID playerUUID) {
        LobbyPlayer skyPlayer = plugin.getPlayerManager().getLbPlayer(playerUUID);
        plugin.getPlayerManager().addLbPlayer(playerUUID, skyPlayer);

        JsonObject response = snakeAPI.GET(RoutePath.PLAYER, playerUUID);

        if (response == null) return;
        int responseCode = response.get("code").getAsInt();
        if (responseCode == 103) {
            plugin.log("SkyWars - SnakeAPI", response.get("message").getAsString());
        } else if (response.get("error").getAsBoolean()) {
            plugin.log("SkyWars - SnakeAPI", "API Responses with error: " + response.get("message").getAsString() + ", error code: " + responseCode);
            return;
        }

        JsonObject playerData = response.getAsJsonObject("data");

        /*Setting player information*/
        List<String> kits = (List<String>) Base64Utils.fromBase64(playerData.get("kits").getAsString());
        List<String> cages = (List<String>) Base64Utils.fromBase64(playerData.get("cages").getAsString());

        skyPlayer.setPurchKits(kits);
        skyPlayer.setSelectedKit(playerData.get("selKit").getAsString());
        skyPlayer.setPurchCages(cages);
        skyPlayer.setCageName(playerData.get("selCage").getAsString());

        /*Setting player solo stats*/
        JsonObject playerStatsSolo = snakeAPI.GET(RoutePath.SOLO, playerUUID);

        if (!playerStatsSolo.get("error").getAsBoolean()) {
            JsonObject respData = playerStatsSolo.getAsJsonObject("data");
            plugin.debug("SOLO STATS: " + respData.toString());

            skyPlayer.setSolo_wins(respData.get("wins").getAsInt());
            skyPlayer.setSolo_kills(respData.get("kills").getAsInt());
            skyPlayer.setSolo_deaths(respData.get("deaths").getAsInt());
        }


        /*Setting player team stats*/
        JsonObject playerStatsTeam = snakeAPI.GET(RoutePath.TEAM, playerUUID);
        if (!playerStatsTeam.get("error").getAsBoolean()) {
            JsonObject respData = playerStatsTeam.getAsJsonObject("data");
            plugin.debug("TEAM STATS: " + playerStatsTeam.toString());

            skyPlayer.setTeam_wins(respData.get("wins").getAsInt());
            skyPlayer.setTeam_kills(respData.get("kills").getAsInt());
            skyPlayer.setTeam_deaths(respData.get("deaths").getAsInt());
        }

    }

    @Override
    public void savePlayer(Player p) {
        if (p == null) return;
        if (!plugin.getPlayerManager().containsLbPlayer(p.getUniqueId())) return;

        LobbyPlayer skyPlayer = plugin.getPlayerManager().getLbPlayer(p.getUniqueId());

        String kits = Base64Utils.toBase64(skyPlayer.getPurchKits());
        String cages = Base64Utils.toBase64(skyPlayer.getPurchCages());
        plugin.debug("Sended Base64 Kits: \"" + kits + "\"");
        plugin.debug("Sended Base64 cages: \"" + cages + "\"");
        String selectedKit = skyPlayer.getSelectedKit();
        String selectedCage = skyPlayer.getCageName();

        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("kits", kits);
        playerJson.addProperty("selKit", selectedKit);
        playerJson.addProperty("cages", cages);
        playerJson.addProperty("selCage", selectedCage);

        snakeAPI.POST(RoutePath.PLAYER, p.getUniqueId(), playerJson);
        plugin.getPlayerManager().removeLbPlayer(p.getUniqueId());
    }

    @Override
    public void closeConnection() {
        plugin.debug("Can't close connection, is using a REST connections..");
    }
}
