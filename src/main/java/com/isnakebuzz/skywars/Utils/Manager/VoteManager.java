package com.isnakebuzz.skywars.Utils.Manager;

import com.isnakebuzz.skywars.Calls.Events.SkyCheckVotesEvent;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.ChestType;
import com.isnakebuzz.skywars.Utils.Enums.ProjectileType;
import com.isnakebuzz.skywars.Utils.Enums.TimeType;
import com.isnakebuzz.skywars.Utils.Enums.VoteType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoteManager {

    private Main plugin;

    private List<UUID> chest_overpowered, chest_normal, chest_basics;
    private List<UUID> time_day, time_night, time_sunset;
    private List<UUID> proj_empty, proj_normal, proj_breakable;

    public VoteManager(Main plugin) {
        this.plugin = plugin;

        //Chest setup
        this.chest_basics = new ArrayList<>();
        this.chest_normal = new ArrayList<>();
        this.chest_overpowered = new ArrayList<>();

        //Time setup
        this.time_day = new ArrayList<>();
        this.time_night = new ArrayList<>();
        this.time_sunset = new ArrayList<>();

        //Projectile setup
        this.proj_empty = new ArrayList<>();
        this.proj_normal = new ArrayList<>();
        this.proj_breakable = new ArrayList<>();

    }

    public void checkVotes() {
        SkyCheckVotesEvent skyCheckVotesEvent = new SkyCheckVotesEvent();
        Bukkit.getPluginManager().callEvent(skyCheckVotesEvent);
        if (skyCheckVotesEvent.isCancelled()) return;

        // bools
        boolean votedTime = false, votedChest = false, votedProjectiles = false;

        //Chest check
        int chest_normal = this.chest_normal.size(),
                chest_overpowered = this.chest_overpowered.size(),
                chest_basics = this.chest_basics.size();

        if (chest_normal > chest_overpowered && chest_normal > chest_basics) {
            plugin.getSkyWarsArena().setChestType(ChestType.NORMAL);
            votedChest = true;
        } else if (chest_basics > chest_normal && chest_basics > chest_overpowered) {
            plugin.getSkyWarsArena().setChestType(ChestType.BASIC);
            votedChest = true;
        } else if (chest_overpowered > chest_normal && chest_overpowered > chest_basics) {
            plugin.getSkyWarsArena().setChestType(ChestType.OVERPOWERED);
            votedChest = true;
        }


        //Time check
        int time_day = this.time_day.size(),
                time_sunset = this.time_sunset.size(),
                time_night = this.time_night.size();

        if (time_day > time_sunset && time_day > time_night) {
            Bukkit.getWorld("world").setTime(1000);
            plugin.getSkyWarsArena().setTimeType(TimeType.DAY);
            votedTime = true;
        } else if (time_night > time_day && time_night > time_sunset) {
            Bukkit.getWorld("world").setTime(13000);
            plugin.getSkyWarsArena().setTimeType(TimeType.NIGHT);
            votedTime = true;
        } else if (time_sunset > time_day && time_sunset > time_night) {
            Bukkit.getWorld("world").setTime(12000);
            plugin.getSkyWarsArena().setTimeType(TimeType.SUNSET);
            votedTime = true;
        }

        //Projectile check
        int proj_throwables = this.proj_empty.size(),
                proj_normal = this.proj_normal.size(),
                proj_breakable = this.proj_breakable.size();

        if (proj_throwables > proj_normal && proj_throwables > proj_breakable) {
            plugin.getSkyWarsArena().setProjectileType(ProjectileType.TWROWABLES);
            votedProjectiles = true;
        } else if (proj_normal > proj_throwables && proj_normal > proj_breakable) {
            plugin.getSkyWarsArena().setProjectileType(ProjectileType.NORMAL);
            votedProjectiles = true;
        } else if (proj_breakable > proj_throwables && proj_breakable > proj_normal) {
            plugin.getSkyWarsArena().setProjectileType(ProjectileType.SOFTBLOCKS);
            plugin.getListenerManager().loadVoteEvents();
            votedProjectiles = true;
        }

        //Send messages
        ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");

        //Messages and placeholders
        String selectedMessage = lang.getString("message");
        String hasVoted_yes = lang.getString("HasVoted.Yes");
        String hasVoted_no = lang.getString("HasVoted.No");

        plugin.debug("Vots: " + hasVoted_no + " : " + hasVoted_yes);

        //Chest message
        String chestType = lang.getString("Types.Chest." + plugin.getSkyWarsArena().getChestType().getName());

        TextComponent chestMessage = new TextComponent();
        chestMessage.setText(c(selectedMessage
                .replaceAll("%type%", chestType)
        ));

        StringBuilder chestHoverMsg = new StringBuilder();
        int chest_size = 0;
        for (String msg : lang.getStringList("HoverText.Chest")) {
            if (chest_size != 0) {
                msg = "\n" + msg;
            }
            chestHoverMsg.append(c(msg
                    .replaceAll("%type%", chestType)
                    .replaceAll("%votes_basic%", String.valueOf(this.chest_basics.size()))
                    .replaceAll("%votes_normal%", String.valueOf(this.chest_normal.size()))
                    .replaceAll("%votes_overpowered%", String.valueOf(this.chest_overpowered.size()))
                    .replaceAll("%has_voted%", votedChest ? hasVoted_yes.replaceAll("%votes%", String.valueOf(getTotalVotes(VoteType.CHEST))) : hasVoted_no)
            ));
            chest_size++;
        }
        chestMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chestHoverMsg.toString()).create()));
        plugin.broadcast(chestMessage);


        //Time message
        String timeType = lang.getString("Types.Time." + plugin.getSkyWarsArena().getTimeType().getName());

        TextComponent timeMessage = new TextComponent();
        timeMessage.setText(c(selectedMessage
                .replaceAll("%type%", timeType)
        ));

        StringBuilder timeHoverMessage = new StringBuilder();
        int time_size = 0;
        for (String msg : lang.getStringList("HoverText.Time")) {
            if (time_size != 0) {
                msg = "\n" + msg;
            }
            timeHoverMessage.append(c(msg
                    .replaceAll("%type%", timeType)
                    .replaceAll("%votes_day%", String.valueOf(this.time_day.size()))
                    .replaceAll("%votes_night%", String.valueOf(this.time_night.size()))
                    .replaceAll("%votes_sunset%", String.valueOf(this.time_sunset.size()))
                    .replaceAll("%has_voted%", votedTime ? hasVoted_yes.replaceAll("votes", String.valueOf(getTotalVotes(VoteType.TIME))) : hasVoted_no)
            ));
            time_size++;
        }
        timeMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(timeHoverMessage.toString()).create()));
        plugin.broadcast(timeMessage);

        //Projectile message
        String projType = lang.getString("Types.Projectiles." + plugin.getSkyWarsArena().getProjectileType().getName());

        TextComponent projMessage = new TextComponent();
        projMessage.setText(c(selectedMessage
                .replaceAll("%type%", projType)
        ));


        StringBuilder projHoverMessage = new StringBuilder();
        int proj_size = 0;
        for (String msg : lang.getStringList("HoverText.Projectiles")) {
            if (proj_size != 0) {
                msg = "\n" + msg;
            }
            projHoverMessage.append(c(msg
                    .replaceAll("%type%", projType)
                    .replaceAll("%votes_throwables%", String.valueOf(this.proj_empty.size()))
                    .replaceAll("%votes_normal%", String.valueOf(this.proj_normal.size()))
                    .replaceAll("%votes_softblocks%", String.valueOf(this.proj_breakable.size()))
                    .replaceAll("%has_voted%", votedProjectiles ? hasVoted_yes.replaceAll("%votes%", String.valueOf(getTotalVotes(VoteType.PROJECTILE))) : hasVoted_no)
            ));
            proj_size++;
        }
        projMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(projHoverMessage.toString()).create()));
        plugin.broadcast(projMessage);

    }

    public boolean setProjectileVote(Player player, ProjectileType projectileType) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (skyPlayer.isProjectile()) return false;
        UUID uuid = player.getUniqueId();

        switch (projectileType) {
            case TWROWABLES:
                this.proj_empty.add(uuid);
                break;
            case NORMAL:
                this.proj_normal.add(uuid);
                break;
            case SOFTBLOCKS:
                this.proj_breakable.add(uuid);
                break;
        }
        skyPlayer.setProjectile(true);

        //Send message
        ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
        String voteType = lang.getString("Types.Projectiles." + projectileType.getName());
        String message = lang.getString("Voted").replaceAll("%player%", player.getName()).replaceAll("%type%", voteType);
        plugin.broadcast(message.replaceAll("%votes%", String.valueOf(getVoteSize(VoteType.PROJECTILE, projectileType))));

        return true;
    }

    public boolean setTimeVote(Player player, TimeType timeType) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (skyPlayer.isTime()) return false;
        UUID uuid = player.getUniqueId();

        switch (timeType) {
            case DAY:
                this.time_day.add(uuid);
                break;
            case SUNSET:
                this.time_sunset.add(uuid);
                break;
            case NIGHT:
                this.time_night.add(uuid);
                break;
        }
        skyPlayer.setTime(true);

        //Send message
        ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
        String voteType = lang.getString("Types.Time." + timeType.getName());
        String message = lang.getString("Voted").replaceAll("%player%", player.getName()).replaceAll("%type%", voteType);
        plugin.broadcast(message.replaceAll("%votes%", String.valueOf(getVoteSize(VoteType.TIME, timeType))));

        return true;
    }

    public boolean setChestVote(Player player, ChestType chestType) {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (skyPlayer.isChest()) return false;
        UUID uuid = player.getUniqueId();

        switch (chestType) {
            case BASIC:
                this.chest_basics.add(uuid);
                break;
            case NORMAL:
                this.chest_normal.add(uuid);
                break;
            case OVERPOWERED:
                this.chest_overpowered.add(uuid);
                break;
        }
        skyPlayer.setChest(true);

        //Send message
        ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
        String voteType = lang.getString("Types.Chest." + chestType.getName());
        String message = lang.getString("Voted").replaceAll("%player%", player.getName()).replaceAll("%type%", voteType);
        plugin.broadcast(message.replaceAll("%votes%", String.valueOf(getVoteSize(VoteType.CHEST, chestType))));

        return true;
    }

    public void removeVoteFrom(Player player) {
        UUID uuid = player.getUniqueId();

        //Chest types
        this.chest_basics.remove(uuid);
        this.chest_normal.remove(uuid);
        this.chest_overpowered.remove(uuid);

        //Time types
        this.time_day.remove(uuid);
        this.time_sunset.remove(uuid);
        this.time_night.remove(uuid);

        //Projectiles types
        this.proj_empty.remove(uuid);
        this.proj_normal.remove(uuid);
        this.proj_breakable.remove(uuid);
    }

    public int getTotalVotes(VoteType voteType) {
        if (voteType.equals(VoteType.CHEST)) {
            return this.chest_basics.size() + this.chest_normal.size() + this.chest_overpowered.size();
        } else if (voteType.equals(VoteType.TIME)) {
            return this.time_night.size() + this.time_sunset.size() + this.time_day.size();
        } else if (voteType.equals(VoteType.PROJECTILE)) {
            return this.proj_breakable.size() + this.proj_normal.size() + this.proj_empty.size();
        }

        return 0;
    }

    public <T> int getVoteSize(VoteType voteType, T value) {
        if (voteType.equals(VoteType.CHEST)) {

            ChestType chestType = (ChestType) value;

            if (chestType.equals(ChestType.BASIC)) {
                return this.chest_basics.size();
            } else if (chestType.equals(ChestType.NORMAL)) {
                return this.chest_normal.size();
            } else if (chestType.equals(ChestType.OVERPOWERED)) {
                return this.chest_overpowered.size();
            }

        } else if (voteType.equals(VoteType.TIME)) {

            TimeType timeType = (TimeType) value;

            if (timeType.equals(TimeType.DAY)) {
                return this.time_day.size();
            } else if (timeType.equals(TimeType.SUNSET)) {
                return this.time_sunset.size();
            } else if (timeType.equals(TimeType.NIGHT)) {
                return this.time_night.size();
            }

        } else if (voteType.equals(VoteType.PROJECTILE)) {

            ProjectileType projectileType = (ProjectileType) value;

            if (projectileType.equals(ProjectileType.TWROWABLES)) {
                return this.proj_empty.size();
            } else if (projectileType.equals(ProjectileType.NORMAL)) {
                return this.proj_normal.size();
            } else if (projectileType.equals(ProjectileType.SOFTBLOCKS)) {
                return this.proj_breakable.size();
            }

        }

        return 0;
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
