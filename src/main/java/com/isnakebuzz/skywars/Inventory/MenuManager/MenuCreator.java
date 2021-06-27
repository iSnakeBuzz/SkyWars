package com.isnakebuzz.skywars.Inventory.MenuManager;

import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Player.PlayerUtils;
import com.isnakebuzz.skywars.SkyWars;
import com.isnakebuzz.skywars.Utils.Enums.ChestType;
import com.isnakebuzz.skywars.Utils.Enums.ProjectileType;
import com.isnakebuzz.skywars.Utils.Enums.TimeType;
import com.isnakebuzz.skywars.Utils.Enums.VoteType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MenuCreator extends Menu {

    private SkyWars plugin;
    private int taskId;

    public MenuCreator(Player player, SkyWars plugin, String _name) {
        super(plugin, _name, player);
        this.plugin = plugin;
        Configuration config = plugin.getConfig("Extra/MenuCreator");
        String path = "MenuCreator." + _name + ".";
        if (config.getBoolean(path + "update.enabled")) {
            taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
                updateInv(player, _name);
            }, 0, config.getInt(path + "update.time"));
        } else {
            this.updateInv(player, _name);
        }
    }

    @Override
    public void onClick(final Player p, final ItemStack itemStack, String _name) {
        Configuration lang = plugin.getConfigUtils().getConfig(plugin, "Lang");
        Configuration config = plugin.getConfig("Extra/MenuCreator");
        String menu_path = "MenuCreator." + _name + ".";
        Set<String> key = config.getConfigurationSection(menu_path + "items").getKeys(false);
        for (String _item : key) {
            String path = menu_path + "items." + _item + ".";
            String item = config.getString(path + "item");
            int amount = config.getInt(path + "amount");
            String name = config.getString(path + "name");
            List<String> lore = chars(config.getStringList(path + "lore"));
            String permission = config.getString(path + "perms");
            String action = config.getString(path + "action");
            ItemStack itemStack1 = ItemBuilder.crearItem1(Integer.valueOf(item.split(":")[0]), amount, Integer.valueOf(item.split(":")[1]), name, lore);
            if (itemStack.equals(itemStack1)) {
                if (!p.hasPermission(permission) && !permission.equalsIgnoreCase("none")) {
                    p.sendMessage(c(lang.getString("NoPerms")));
                    return;
                }
                if (action.split(":")[0].equalsIgnoreCase("open")) {
                    new MenuCreator(p, plugin, action.split(":")[1]).open();
                } else if (action.split(":")[0].equalsIgnoreCase("cmd")) {
                    String cmd = "/" + action.split(":")[1];
                    p.chat(cmd);
                } else if (action.split(":")[0].equalsIgnoreCase("speed")) {
                    float speed = Float.valueOf(action.split(":")[1]);
                    int speedLevel = Integer.valueOf(action.split("\\.")[1]);
                    plugin.debug("Player Actual velocity is: " + p.getFlySpeed());
                    if (speed == 0.0) {
                        PlayerUtils.removePotionEffect(p, PotionEffectType.SPEED);
                        p.setFlySpeed(0.1f);
                    } else {
                        PlayerUtils.removePotionEffect(p, PotionEffectType.SPEED);
                        PlayerUtils.addEffects(p, new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, (speedLevel - 1), false, false));
                        p.setFlySpeed(speed);
                    }
                } else if (action.split(":")[0].equalsIgnoreCase("close")) {
                    p.closeInventory();
                } else if (action.split(":")[0].equalsIgnoreCase("vote")) {
                    String voteCheck = action.split(":")[1];
                    switch (voteCheck) {

                        // Check chest votes
                        case "chest_normal":
                            if (!plugin.getVoteManager().setChestVote(p, ChestType.NORMAL)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;
                        case "chest_basic":
                            if (!plugin.getVoteManager().setChestVote(p, ChestType.BASIC)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;
                        case "chest_overpowered":
                            if (!plugin.getVoteManager().setChestVote(p, ChestType.OVERPOWERED)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;

                        // Check time votes
                        case "time_day":
                            if (!plugin.getVoteManager().setTimeVote(p, TimeType.DAY)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;
                        case "time_night":
                            if (!plugin.getVoteManager().setTimeVote(p, TimeType.NIGHT)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;
                        case "time_sunset":
                            if (!plugin.getVoteManager().setTimeVote(p, TimeType.SUNSET)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;

                        // Check projectiles votes
                        case "projectile_no_throwables":
                            if (!plugin.getVoteManager().setProjectileVote(p, ProjectileType.TWROWABLES)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;
                        case "projectile_normal":
                            if (!plugin.getVoteManager().setProjectileVote(p, ProjectileType.NORMAL)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;
                        case "projectile_soft_blocks":
                            if (!plugin.getVoteManager().setProjectileVote(p, ProjectileType.SOFTBLOCKS)) {
                                ConfigurationSection votemsg = plugin.getConfig("Lang").getConfigurationSection("VoteMessages");
                                p.sendMessage(c(votemsg.getString("Already")));
                            }
                            break;

                    }

                }

            }
        }

    }

    @Override
    public void onClose(Player p) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    private void updateInv(Player p, String _name) {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Extra/MenuCreator");
        String menu_path = "MenuCreator." + _name + ".";
        Set<String> key = config.getConfigurationSection(menu_path + "items").getKeys(false);
        for (String _item : key) {
            String path = menu_path + "items." + _item + ".";
            String item = config.getString(path + "item");
            int slot = config.getInt(path + "slot");
            int amount = config.getInt(path + "amount");
            String name = config.getString(path + "name");
            List<String> lore = chars(config.getStringList(path + "lore"));
            String action = config.getString(path + "action");
            ItemStack itemStack = ItemBuilder.crearItem1(Integer.valueOf(item.split(":")[0]), amount, Integer.valueOf(item.split(":")[1]), name, lore);
            this.s(slot, itemStack);
        }
    }

    private List<String> chars(List<String> messages) {
        List<String> newMessages = new ArrayList<>();
        for (String msg : messages) {
            newMessages.add(msg
                    // Chests
                    .replaceAll("%chest_op%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.CHEST, ChestType.OVERPOWERED)))
                    .replaceAll("%chest_normal%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.CHEST, ChestType.NORMAL)))
                    .replaceAll("%chest_basic%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.CHEST, ChestType.BASIC)))

                    // Times
                    .replaceAll("%time_day%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.TIME, TimeType.DAY)))
                    .replaceAll("%time_night%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.TIME, TimeType.NIGHT)))
                    .replaceAll("%time_sunset%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.TIME, TimeType.SUNSET)))

                    //Projectiles
                    .replaceAll("%projectiles_no_throwables%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.PROJECTILE, ProjectileType.TWROWABLES)))
                    .replaceAll("%projectiles_normal%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.PROJECTILE, ProjectileType.NORMAL)))
                    .replaceAll("%projectiles_soft_blocks%", String.valueOf(plugin.getVoteManager().getVoteSize(VoteType.PROJECTILE, ProjectileType.SOFTBLOCKS)))

                    //Totals
                    .replaceAll("%total_chest%", String.valueOf(plugin.getVoteManager().getTotalVotes(VoteType.CHEST)))
                    .replaceAll("%total_time%", String.valueOf(plugin.getVoteManager().getTotalVotes(VoteType.TIME)))
                    .replaceAll("%total_projectile%", String.valueOf(plugin.getVoteManager().getTotalVotes(VoteType.PROJECTILE)))
            );
        }
        return newMessages;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
