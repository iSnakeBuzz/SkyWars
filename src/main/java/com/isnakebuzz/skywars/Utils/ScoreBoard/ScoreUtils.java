package com.isnakebuzz.skywars.Utils.ScoreBoard;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.GameType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class ScoreUtils {

    private Main plugin;

    private boolean reset;
    private boolean spect;

    private Scoreboard scoreboard;
    private Objective nameHealthObj;
    private Objective tablistHealthObj;

    //Teams
    private Map<String, Team> friendlyTeam;
    private Map<String, Team> enemyTeam;
    private Map<String, Team> spectatorTeam;

    //Solitaries teams
    private Team spectator;
    private Team friendlySpot;
    private Team enemySpot;
    private Team staffTeam;

    public ScoreUtils(Main plugin, Player player, boolean health, boolean spect, boolean gameTags) {
        this.scoreboard = player.getScoreboard();
        this.spect = spect;
        this.plugin = plugin;

        //Setting up staff team
        this.staffTeam = this.scoreboard.registerNewTeam("Staff");
        this.staffTeam.setPrefix(color("&C&lSTAFF&7 "));
        this.staffTeam.setAllowFriendlyFire(false);
        this.staffTeam.setCanSeeFriendlyInvisibles(true);

        if (health) {
            this.nameHealthObj = this.scoreboard.registerNewObjective("namelife", "health");
            this.nameHealthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
            this.nameHealthObj.setDisplayName(ChatColor.DARK_RED + "\u2764");

            this.tablistHealthObj = this.scoreboard.registerNewObjective("tablife", "dummy");
            this.tablistHealthObj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }

        if (spect) {

            if (!plugin.getSkyWarsArena().getGameType().equals(GameType.TEAM)) {
                this.spectator = this.scoreboard.registerNewTeam("Spectator");
                this.spectator.setPrefix(color("&7"));
                this.spectator.setCanSeeFriendlyInvisibles(true);
                this.spectator.setAllowFriendlyFire(false);
            } else {
                this.spectatorTeam = new HashMap<>();

                String spectator = "&7[:name:] ";

                for (com.isnakebuzz.skywars.Teams.Team team : plugin.getTeamManager().getTeamMap().values()) {
                    // Spectator Team
                    Team sTeam = this.scoreboard.registerNewTeam("B-" + team.getName() + "-s");
                    sTeam.setPrefix(color(spectator.replaceAll(":name:", team.getName())));
                    sTeam.setCanSeeFriendlyInvisibles(true);
                    sTeam.setAllowFriendlyFire(false);


                    this.spectatorTeam.put(team.getName(), sTeam);
                }
            }

        }

        if (gameTags) {
            if (!plugin.getSkyWarsArena().getGameType().equals(GameType.TEAM)) {

                // Enemy spot
                this.enemySpot = this.scoreboard.registerNewTeam("friendly");
                this.enemySpot.setPrefix(color("&c"));
                this.enemySpot.setAllowFriendlyFire(false);
                this.enemySpot.setCanSeeFriendlyInvisibles(true);

                // Friendly spot
                this.friendlySpot = this.scoreboard.registerNewTeam("enemy");
                this.friendlySpot.setPrefix(color("&a"));
                this.friendlySpot.setAllowFriendlyFire(false);
                this.friendlySpot.setCanSeeFriendlyInvisibles(true);


            } else {

                this.friendlyTeam = new HashMap<>();
                this.enemyTeam = new HashMap<>();

                String friendly = "&a[:name:] ";
                String enemy = "&c[:name:] ";

                for (com.isnakebuzz.skywars.Teams.Team team : plugin.getTeamManager().getTeamMap().values()) {
                    // Friendly team
                    Team fTeam = this.scoreboard.registerNewTeam("A-" + team.getName() + "-f");
                    fTeam.setPrefix(color(friendly.replaceAll(":name:", team.getName())));
                    fTeam.setCanSeeFriendlyInvisibles(true);
                    fTeam.setAllowFriendlyFire(false);


                    this.friendlyTeam.put(team.getName(), fTeam);
                    // Enemy team
                    Team eTeam = this.scoreboard.registerNewTeam("C-" + team.getName() + "-e");
                    eTeam.setPrefix(color(enemy.replaceAll(":name:", team.getName())));
                    eTeam.setCanSeeFriendlyInvisibles(true);
                    eTeam.setAllowFriendlyFire(false);

                    this.enemyTeam.put(team.getName(), eTeam);
                }


            }
            /*
            this.enemySpot = this.scoreboard.registerNewTeam("EnemySpot");
            this.enemySpot.setPrefix(color("&c"));
            this.enemySpot.setAllowFriendlyFire(true);

            this.friendlySpot = this.scoreboard.registerNewTeam("FriendlySpot");
            this.friendlySpot.setPrefix(color("&a"));
            this.friendlySpot.setAllowFriendlyFire(false);
             */
        }

    }

    public void updatelife() {
        for (Player onlinePlayers : plugin.getSkyWarsArena().getGamePlayers()) {
            final Player player2;
            final Player player = player2 = onlinePlayers;
            this.nameHealthObj.getScore(player.getName()).setScore((int) (player2).getHealth() + absorpHearts(player2));
            this.tablistHealthObj.getScore(player.getName()).setScore((int) (player2).getHealth() + absorpHearts(player2));
        }
    }

    public void updatespect(Player p) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);
        if (skyPlayer.isStaff()) {
            if (!staffTeam.hasPlayer(p)) {
                this.staffTeam.addPlayer(p);
            }
            return;
        }

        if (!plugin.getSkyWarsArena().getGameType().equals(GameType.TEAM)) {
            if (!spectator.hasPlayer(p)) {
                this.spectator.addPlayer(p);
            }
        } else {
            Team spectator = this.spectatorTeam.get(skyPlayer.getTeam().getName());
            if (!spectator.hasPlayer(p)) {
                spectator.addPlayer(p);
            }
        }
    }

    public void updateGameTAG(Player p) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(p);

        if (!this.spect) {
            if (this.friendlyTeam.containsKey(skyPlayer.getTeam().getName())) {
                Team team = this.friendlyTeam.get(skyPlayer.getTeam().getName());
                if (!team.hasPlayer(p)) {
                    //plugin.debug("Setting tag to " + p.getName() + ", TEAM: " + team.getName());
                    team.addPlayer(p);
                }
            }

        }
        for (Player p2 : plugin.getSkyWarsArena().getGamePlayers()) {
            if (!p.equals(p2)) {
                SkyPlayer skyPlayer2 = plugin.getPlayerManager().getPlayer(p2);
                if (!skyPlayer2.isDead()) {
                    if (this.enemyTeam.containsKey(skyPlayer2.getTeam().getName())) {

                        if (skyPlayer.getTeam().equals(skyPlayer2.getTeam())) {
                            Team team = this.friendlyTeam.get(skyPlayer.getTeam().getName());
                            if (!team.hasPlayer(p2)) {
                                //plugin.debug("Setting tag to " + p.getName() + ", TEAM: " + team.getName());
                                team.addPlayer(p2);
                            }
                        } else {
                            Team team = this.enemyTeam.get(skyPlayer2.getTeam().getName());
                            if (!team.hasPlayer(p2)) {
                                //plugin.debug("Setting tag to " + p.getName() + ", TEAM: " + team.getName());
                                team.addPlayer(p2);
                            }
                        }

                    }
                }
            }
        }
    }

    public void updategames(Player p) {
        if (!this.spect) {
            if (!this.friendlySpot.hasPlayer(p)) {
                this.friendlySpot.addPlayer(p);
            }
        }
        for (Player p2 : plugin.getSkyWarsArena().getGamePlayers()) {
            if (p2 != p) {
                SkyPlayer skyPlayer2 = plugin.getPlayerManager().getPlayer(p2);
                if (!skyPlayer2.isDead()) {
                    if (!this.enemySpot.hasPlayer(p2)) {
                        this.enemySpot.addPlayer(p2);
                    }
                }
            }
        }
    }

    private int absorpHearts(Player pl) {
        for (PotionEffect pe : pl.getActivePotionEffects()) {
            if (pe.getType().equals(PotionEffectType.ABSORPTION)) {
                return pe.getAmplifier() * 2 + 2;
            }
        }
        return 0;
    }

    private String color(final String s) {
        return s.replaceAll("&", "§");
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public void build(final Player player) {
        player.setScoreboard(this.scoreboard);
    }

}
