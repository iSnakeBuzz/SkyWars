package com.isnakebuzz.skywars.Teams;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import com.isnakebuzz.skywars.Utils.Statics;
import com.isnakebuzz.skywars.Utils.Strings.Alphabet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private Main plugin;
    private Map<String, Team> teamMap;
    private int teamSize;


    public TeamManager(Main plugin) {
        this.plugin = plugin;
        this.teamMap = new HashMap<>();
    }

    public void loadTeams() {
        ConfigurationSection arena = plugin.getConfig("Extra/Arena").getConfigurationSection("Teams");

        boolean alphabeticNames = arena.getBoolean("alphabetic names", true);
        int teamSize = arena.getInt("size", 2);
        int maxTeams = plugin.getSkyWarsArena().getMaxPlayers() / teamSize;
        this.teamSize = teamSize;

        plugin.debug("Arena Mode: " + plugin.getSkyWarsArena().getGameType().toString());

        if (plugin.getSkyWarsArena().getGameType().equals(GameType.TEAM)) {
            for (int i = 1; i <= maxTeams; i++) {
                String teamName;
                if (alphabeticNames) teamName = Alphabet.getById(i).getName();
                else teamName = String.valueOf(i);
                Team team = new Team(teamName, i);
                this.teamMap.put(teamName, team);
                plugin.debug("Creating team: " + team.getName() + ":" + i);
            }
        } else {
            for (int i = 1; i <= plugin.getSkyWarsArena().getMaxPlayers(); i++) {
                String teamName = String.valueOf(i);
                Team team = new Team(teamName, i);
                plugin.debug("Creating team: " + team.getName() + ":" + i);
                this.teamMap.put(teamName, team);
            }
        }

    }

    public void giveTeams() {
        ConfigurationSection arena = plugin.getConfig("Extra/Arena").getConfigurationSection("Teams");

        if (plugin.getSkyWarsArena().getGameType().equals(GameType.TEAM)) {
            /*
             * Posiblemente tenga que cambiar esto, pero si funciona bien nice :v
             */

            int i = 1;
            for (Player player : plugin.getSkyWarsArena().getGamePlayers()) {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
                Team team = getTeam(i);

                //plugin.debug("Team Size: " + team.getName() + " | " + team.getTeamPlayers().size());
                //plugin.debug("Math Value: " + team.getTeamPlayers().size() + " < " + this.teamSize + " = " + (team.getTeamPlayers().size() < this.teamSize));

                if (skyPlayer.getTeam() != null) {
                    continue;
                }

                if (team.getTeamPlayers().size() < this.teamSize) {
                    skyPlayer.setTeam(team);
                    plugin.debug("Adding player to team; " + "ID: " + i + ", NAME: " + team.getName() + ", SIZE: " + team.getTeamPlayers().size());
                } else {
                    i++;
                    Team team2 = getTeam(i);
                    skyPlayer.setTeam(team2);
                    plugin.debug("Adding player to new team; " + "ID: " + i + ", NAME: " + team2.getName() + ", SIZE: " + team2.getTeamPlayers().size());
                }

            }

        } else if (plugin.getSkyWarsArena().getGameType().equals(GameType.SOLO)) {
            int i = 1;
            plugin.debug("Initial Team: " + i);
            for (Player player : plugin.getSkyWarsArena().getGamePlayers()) {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
                skyPlayer.setTeam(getTeam(i));
                i++;
            }
        }
    }

    public Team getTeam(int id) {
        ConfigurationSection arena = plugin.getConfig("Extra/Arena").getConfigurationSection("Teams");
        boolean alphabeticNames = arena.getBoolean("alphabetic names", true);

        String name;

        if (Statics.skyMode.equals(GameType.SOLO)) {
            name = String.valueOf(id);
        } else {
            if (alphabeticNames) name = Alphabet.getById(id).getName();
            else name = String.valueOf(id);
        }

        plugin.debug("Getting team: " + name + " : " + this.teamMap.getOrDefault(name, null).toString());

        return this.teamMap.getOrDefault(name, null);
    }

    public Map<String, Team> getTeamMap() {
        return teamMap;
    }
}
