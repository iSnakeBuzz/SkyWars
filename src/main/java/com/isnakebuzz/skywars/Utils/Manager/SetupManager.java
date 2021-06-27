package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.MessageAPI.Messages;
import com.isnakebuzz.skywars.MessageAPI.Queue;
import com.isnakebuzz.skywars.MessageAPI.sSound;
import com.isnakebuzz.skywars.SkyWars;

public class SetupManager {

    private SkyWars plugin;

    public SetupManager(SkyWars plugin) {
        this.plugin = plugin;
    }

    Boolean[] helpTalk = new Boolean[]{
            false, /* Add SPAWN */
            false, /* setLobby */
            false, /* setArea */
            false, /* setChests */
            false, /* setMap */
            false, /* setMin */
            false, /* setMax */
            false, /* setStarting */
            false, /* setCages */
            false, /* setEnd */
            false, /* setRefillTime */
            false, /* setCenterSchem */
            false /* kitCommands */
    };

    public Messages talk(String message) {
        return new Queue(message);
    }

    public Messages talk(String message, sSound sound) {
        return new Queue(message, sound);
    }

}
