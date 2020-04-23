package com.isnakebuzz.skywars.Database.System;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isnakebuzz.skywars.Database.Utils.RoutePath;
import com.isnakebuzz.skywars.Main;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class SnakeAPI {

    private String USER_AGENT = "Mozilla/5.0";
    private String URL;
    private Main plugin;

    public SnakeAPI(Main plugin) {
        this.plugin = plugin;
        ConfigurationSection config = plugin.getConfig("Extra/Database");
        this.URL = config.getString("SnakeDb", "http://localhost:9090/skywars/");
    }

    @SneakyThrows
    public JsonObject GET(RoutePath routePath, UUID uuid) {
        String localURL = URL + routePath.getPath() + uuid.toString();
        plugin.debug("Calling URL: " + localURL + "");
        URL obj = new URL(localURL);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-type", "application/json");

        int responseCode = con.getResponseCode();
        /*System.out.println("GET \"" + localURL + "\" Response Code :: " + responseCode);*/

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String jsonresponse = in.readLine();

            /*plugin.debug("Body of: " + URL + " = " + jsonresponse);*/

            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(jsonresponse);
            in.close();

            plugin.debug("Response: " + root.getAsJsonObject().toString());

            // print result
            return root.getAsJsonObject();
        } else {
            plugin.log("SkyWars", "GET \"" + localURL + "\" request not worked, response code: " + responseCode);
            return null;
        }

    }

    @SneakyThrows
    public JsonObject POST(RoutePath routePath, UUID uuid, JsonObject jsonObject) {
        String localURL = URL + routePath.getPath() + uuid.toString();

        URL obj = new URL(localURL);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-type", "application/json");

        // For POST only - START
        con.setDoOutput(true);

        DataOutputStream dataOutputStream = new DataOutputStream(con.getOutputStream());
        dataOutputStream.writeBytes(jsonObject.toString());
        dataOutputStream.flush();
        dataOutputStream.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST \"" + localURL + "\" Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String jsonresponse = in.readLine();
            JsonParser jsonParser = new JsonParser();
            JsonElement root = jsonParser.parse(jsonresponse);
            in.close();

            // print result
            return root.getAsJsonObject();
        } else {
            plugin.log("SkyWars", "POST \"" + localURL + "\" request not worked, response code: " + responseCode);
            return null;
        }
    }

}
