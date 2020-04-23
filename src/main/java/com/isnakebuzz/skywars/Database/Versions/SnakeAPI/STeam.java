package com.isnakebuzz.skywars.Database.Versions.SnakeAPI;

import com.google.gson.JsonObject;
import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Database.System.SnakeAPI;
import com.isnakebuzz.skywars.Database.Utils.RoutePath;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Base64Utils;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class STeam implements Database {

    private Main plugin;
    private SnakeAPI snakeAPI;

    public STeam(Main plugin) {
        this.plugin = plugin;
        this.snakeAPI = new SnakeAPI(plugin);
    }

    @SneakyThrows
    @Override
    public void createPlayer(UUID playerUUID) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(playerUUID);
        plugin.getPlayerManager().addPlayer(playerUUID, skyPlayer);

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

        /*Setting player stats*/
        JsonObject playerStats = snakeAPI.GET(RoutePath.TEAM, playerUUID).getAsJsonObject("data");

        skyPlayer.setWins(playerStats.get("wins").getAsInt());
        skyPlayer.setKills(playerStats.get("kills").getAsInt());
        skyPlayer.setDeaths(playerStats.get("deaths").getAsInt());

    }

    @Override
    public void savePlayer(Player p) {
        if (p == null) return;
        if (!plugin.getPlayerManager().containsPlayer(p.getUniqueId())) return;

        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p.getUniqueId());

        String kits = Base64Utils.toBase64(skyPlayer.getPurchKits());
        String cages = Base64Utils.toBase64(skyPlayer.getPurchCages());
        String selectedKit = skyPlayer.getSelectedKit();
        String selectedCage = skyPlayer.getCageName();

        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("kits", kits);
        playerJson.addProperty("selKit", selectedKit);
        playerJson.addProperty("cages", cages);
        playerJson.addProperty("selCage", selectedCage);

        JsonObject statsJson = new JsonObject();
        statsJson.addProperty("username", p.getDisplayName());
        statsJson.addProperty("wins", skyPlayer.getWins());
        statsJson.addProperty("kills", skyPlayer.getKills());
        statsJson.addProperty("deaths", skyPlayer.getDeaths());

        snakeAPI.POST(RoutePath.PLAYER, p.getUniqueId(), playerJson);
        snakeAPI.POST(RoutePath.TEAM, p.getUniqueId(), statsJson);
        plugin.getPlayerManager().removePlayer(p.getUniqueId());
    }

    @Override
    public void closeConnection() {
        plugin.debug("Can't close connection, is using a REST connections..");
    }
}
