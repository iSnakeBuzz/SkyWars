package com.isnakebuzz.skywars.Utils.ScoreBoard;

import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreUtils {

    private boolean reset;

    private Scoreboard scoreboard;
    private Objective nameHealthObj;
    private Objective tablistHealthObj;

    private Team spectator;

    private Team enemySpot;
    private Team friendlySpot;

    private boolean spect;

    public ScoreUtils(Player player, boolean health, boolean spect, boolean gameTags) {
        this.scoreboard = player.getScoreboard();
        this.spect = spect;

        if (health) {
            this.nameHealthObj = this.scoreboard.registerNewObjective("namelife", "health");
            this.nameHealthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
            this.nameHealthObj.setDisplayName(ChatColor.DARK_RED + "\u2764");

            this.tablistHealthObj = this.scoreboard.registerNewObjective("tablife", "dummy");
            this.tablistHealthObj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }

        if (spect) {
            this.spectator = this.scoreboard.registerNewTeam("Spectator");
            this.spectator.setPrefix(color("&7"));
            this.spectator.setCanSeeFriendlyInvisibles(true);
            this.spectator.setAllowFriendlyFire(false);
        }

        if (gameTags) {
            this.enemySpot = this.scoreboard.registerNewTeam("EnemySpot");
            this.enemySpot.setPrefix(color("&c"));
            this.enemySpot.setAllowFriendlyFire(true);

            this.friendlySpot = this.scoreboard.registerNewTeam("FriendlySpot");
            this.friendlySpot.setPrefix(color("&a"));
            this.friendlySpot.setAllowFriendlyFire(false);
        }

    }

    public void updatelife(Main plugin) {
        for (Player onlinePlayers : plugin.getSkyWarsArena().getGamePlayers()) {
            final Player player2;
            final Player player = player2 = onlinePlayers;
            this.nameHealthObj.getScore(player.getName()).setScore((int) (player2).getHealth() + absorpHearts(player2));
            this.tablistHealthObj.getScore(player.getName()).setScore((int) (player2).getHealth() + absorpHearts(player2));
        }
    }

    public void updatespect(Player p) {
        if (!spectator.hasPlayer(p)) {
            this.spectator.addPlayer(p);
        }
    }

    public void updategames(Main plugin, Player p) {
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
