package com.isnakebuzz.skywars.Database.Versions;

import com.isnakebuzz.skywars.Database.Database;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Statics;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class vMongoDB implements Database {

    private Main plugin;
    private MongoClient mongoClient;
    private MongoCollection<Document> collection_skywars_solo;
    private MongoCollection<Document> collection_skywars_team;

    public vMongoDB(Main plugin) {
        this.plugin = plugin;
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Database");
        MongoDatabase mongoDatabase = mongoClient.getDatabase(config.getString("MongoDB.database"));

        if (mongoDatabase.getCollection("SkyWars_SOLO") == null) {
            mongoDatabase.createCollection("SkyWars_SOLO");
        }
        if (mongoDatabase.getCollection("SkyWars_TEAM") == null) {
            mongoDatabase.createCollection("SkyWars_TEAM");
        }

        this.collection_skywars_solo = mongoDatabase.getCollection("SkyWars_SOLO");
        this.collection_skywars_team = mongoDatabase.getCollection("SkyWars_TEAM");
        this.loadMongo();
    }

    @Override
    public void createPlayer(Player p) throws IOException {

        if (Statics.skyMode.equalsIgnoreCase("SOLO")) {
            if (!playerExist_SOLO(p)) {
                Document document = new Document("UUID", p.getUniqueId());
                Document found = collection_skywars_solo.find(document).first();
                if (found == null) {
                    SkyPlayer skyPlayer = new SkyPlayer(p.getUniqueId());

                    document.append("Wins", skyPlayer.getWins());
                    document.append("Kills", skyPlayer.getKills());
                    document.append("Deaths", skyPlayer.getDeaths());
                    this.collection_skywars_solo.insertOne(document);
                    plugin.getPlayerManager().addPlayer(p, skyPlayer);
                }
            } else {
                Document document = new Document("UUID", p.getUniqueId());
                Document found = collection_skywars_solo.find(document).first();
                if (found != null) {
                    SkyPlayer skyPlayer = new SkyPlayer(p.getUniqueId());
                    skyPlayer.setWins(found.getInteger("Wins"));
                    skyPlayer.setKills(found.getInteger("Kills"));
                    skyPlayer.setDeaths(found.getInteger("Deaths"));
                }
            }
        } else if (Statics.skyMode.equalsIgnoreCase("TEAM")) {
            if (!playerExist_SOLO(p)) {
                Document document = new Document("UUID", p.getUniqueId());
                Document found = collection_skywars_team.find(document).first();
                if (found == null) {
                    SkyPlayer skyPlayer = new SkyPlayer(p.getUniqueId());

                    document.append("Wins", skyPlayer.getWins());
                    document.append("Kills", skyPlayer.getKills());
                    document.append("Deaths", skyPlayer.getDeaths());
                    this.collection_skywars_team.insertOne(document);
                    plugin.getPlayerManager().addPlayer(p, skyPlayer);
                }
            } else {
                Document document = new Document("UUID", p.getUniqueId());
                Document found = collection_skywars_team.find(document).first();
                if (found != null) {
                    SkyPlayer skyPlayer = new SkyPlayer(p.getUniqueId());
                    skyPlayer.setWins(found.getInteger("Wins"));
                    skyPlayer.setKills(found.getInteger("Kills"));
                    skyPlayer.setDeaths(found.getInteger("Deaths"));
                }
            }
        }
    }

    @Override
    public void savePlayer(Player p) {
        if (Statics.skyMode.equalsIgnoreCase("SOLO")) {
            if (playerExist_SOLO(p)) {
                Document document = new Document("UUID", p.getUniqueId());
                Document found = collection_skywars_solo.find(document).first();
                if (found != null) {
                    SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
                    document.append("Wins", skyPlayer.getWins());
                    document.append("Kills", skyPlayer.getKills());
                    document.append("Deaths", skyPlayer.getDeaths());
                    this.collection_skywars_solo.replaceOne(found, document, new UpdateOptions().upsert(true));
                }
            }
        } else if (Statics.skyMode.equalsIgnoreCase("TEAM")) {
            if (playerExist_TEAM(p)) {
                Document document = new Document("UUID", p.getUniqueId());
                Document found = collection_skywars_team.find(document).first();
                if (found != null) {
                    SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
                    document.append("Wins", skyPlayer.getWins());
                    document.append("Kills", skyPlayer.getKills());
                    document.append("Deaths", skyPlayer.getDeaths());
                    this.collection_skywars_team.replaceOne(found, document, new UpdateOptions().upsert(true));
                }
            }
        }
    }

    @Override
    public void closeConnection() {
        this.mongoClient.close();
    }

    private boolean playerExist_SOLO(Player p) {
        Document document = new Document("UUID", p.getUniqueId());
        Document found = collection_skywars_solo.find(document).first();
        if (found == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean playerExist_TEAM(Player p) {
        Document document = new Document("UUID", p.getUniqueId());
        Document found = collection_skywars_team.find(document).first();
        if (found == null) {
            return false;
        } else {
            return true;
        }
    }

    public void loadMongo() {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/Database");
        String user = config.getString("MongoDB.user");
        String pass = config.getString("MongoDB.pass");
        String servers = config.getString("MongoDB.hostname");
        String uri = "mongodb+srv://" + user + ":" + pass + "@" + servers + "/";

        MongoClientURI clientURI = new MongoClientURI(uri);
        mongoClient = new MongoClient(clientURI);
    }
}