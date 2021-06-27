package com.isnakebuzz.skywars.Utils.PlaceholderAPI;

import com.isnakebuzz.skywars.Player.LobbyPlayer;
import com.isnakebuzz.skywars.SkyWars;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the directory
 * {@code /plugins/PlaceholderAPI/expansions} on your server.
 * <br>
 * <br>If you create such a class inside your own plugin, you have to
 * register it manually in your plugins {@code onEnable()} by using
 * {@code new YourExpansionClass().register();}
 */
public class SkyHolder extends PlaceholderExpansion {

    private SkyWars plugin;

    public SkyHolder(SkyWars plugin) {
        this.plugin = plugin;
    }

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     *
     * @return always true since we do not have any dependencies.
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor() {
        return "iSnakeBuzz_";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier() {
        return "sw";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player     A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param identifier A String containing the identifier/value.
     * @return Possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        String soloWins = "Loading..";
        String soloKills = "Loading..";

        String teamWins = "Loading..";
        String teamKills = "Loading..";

        String selKit = "Loading";
        String selCage = "Loading";
        String selBallon = "Soon";

        if (plugin.getPlayerManager().containsLbPlayer(player.getPlayer().getUniqueId())) {
            LobbyPlayer lobbyPlayer = plugin.getPlayerManager().getLbPlayer(player.getPlayer().getUniqueId());

            soloWins = String.valueOf(lobbyPlayer.getSolo_wins());
            soloKills = String.valueOf(lobbyPlayer.getSolo_kills());

            teamWins = String.valueOf(lobbyPlayer.getTeam_wins());
            teamKills = String.valueOf(lobbyPlayer.getTeam_kills());

            selKit = lobbyPlayer.getSelectedKit();
            selCage = lobbyPlayer.getCageName();

        }

        if (identifier.equals("solo_wins")) {
            return soloWins;
        } else if (identifier.equals("solo_kills")) {
            return soloKills;
        }

        if (identifier.equals("team_wins")) {
            return teamWins;
        } else if (identifier.equals("team_kills")) {
            return teamKills;
        }

        if (identifier.equals("sel_kit")) {
            return selKit;
        } else if (identifier.equals("sel_cage")) {
            return selCage;
        } else if (identifier.equals("sel_balloon")) {
            return selBallon;
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%) 
        // was provided
        return "Error";
    }
}