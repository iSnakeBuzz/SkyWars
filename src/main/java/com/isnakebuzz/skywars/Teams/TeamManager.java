package com.isnakebuzz.skywars.Teams;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Strings.Alphabet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
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

        boolean enabledTeams = arena.getBoolean("enabled", false);
        boolean alphabeticNames = arena.getBoolean("alphabetic names", true);
        int teamSize = arena.getInt("size", 2);
        int maxTeams = plugin.getSkyWarsArena().getMaxPlayers() / teamSize;
        this.teamSize = maxTeams;

        if (enabledTeams) {
            for (int i = 1; i < maxTeams; i++) {
                String teamName;
                if (alphabeticNames) teamName = Alphabet.valueOf(i).toString();
                else teamName = String.valueOf(i);
                Team team = new Team(teamName, i);
                this.teamMap.put(teamName, team);
            }
        } else {
            for (int i = 1; i < plugin.getSkyWarsArena().getMaxPlayers(); i++) {
                String teamName = String.valueOf(i);
                Team team = new Team(teamName, i);
                this.teamMap.put(teamName, team);
            }
        }

    }

    public void giveTeams() {
        ConfigurationSection arena = plugin.getConfig("Extra/Arena").getConfigurationSection("Teams");
        boolean enabledTeams = arena.getBoolean("enabled", false);

        if (enabledTeams) {

            /*
             * Posiblemente tenga que cambiar esto, pero si funciona bien nice :v
             */

            int i = 0;
            for (Player player : plugin.getSkyWarsArena().getGamePlayers()) {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player);
                Team team = getTeams().toArray(new Team[getTeams().size()])[i];
                int teamSize_local = team.getTeamPlayers().size();

                if (this.teamSize < teamSize_local) {
                    skyPlayer.setTeam(team);
                } else {
                    i++;
                    Team team2 = getTeams().toArray(new Team[getTeams().size()])[i];
                    skyPlayer.setTeam(team2);
                }
            }

        } else {
            int i = 0;
            for (Player player : plugin.getSkyWarsArena().getGamePlayers()) {
                SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player);
                skyPlayer.setTeam(getTeams().toArray(new Team[getTeams().size()])[i]);

                if (this.getTeams().size() < plugin.getSkyWarsArena().getGamePlayers().size()) i++;
            }
        }
    }

    public Collection<Team> getTeams() {
        return teamMap.values();
    }

}
