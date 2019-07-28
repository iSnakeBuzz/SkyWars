package com.isnakebuzz.skywars.Inventory.Others;

import com.isnakebuzz.skywars.Inventory.MenuManager.Menu;
import com.isnakebuzz.skywars.Kits.Kit;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
import com.isnakebuzz.skywars.Utils.Enums.GameStatus;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class KitsMenu extends Menu {

    private Main plugin;

    public KitsMenu(Main plugin, String menu_name, Player player) {
        super(plugin, menu_name, player);
        this.plugin = plugin;

        this.loadKits();
    }

    @Override
    public void onClick(Player p, ItemStack item, String name) {
        ConfigurationSection lang = plugin.getConfig("Lang").getConfigurationSection("Kit");

        if (plugin.getSkyWarsArena().getGameStatus().equals(GameStatus.INGAME)) {
            p.sendMessage(c(lang.getString("its Too Late.msg")));

            return;
        }

        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(this.getPlayer().getUniqueId());


        for (Kit kit : plugin.getKitLoader().getKits()) {
            String itemName = strip(kit.getLogo().getItemMeta().getDisplayName());
            String cItemName = strip(item.getItemMeta().getDisplayName());
            if (itemName.equalsIgnoreCase(cItemName)) {
                boolean purchKit = skyPlayer.getPurchKits().contains(kit.getName());
                if (purchKit || kit.isDefault()) {
                    plugin.getPlayerManager().getPlayer(p.getUniqueId()).setSelectedKit(kit.getName());
                    p.sendMessage(c(lang.getString("Selected.msg")
                            .replaceAll("%kit%", kit.getName())
                    ));
                    plugin.getUtils().playSound(p, lang.getString("Selected.sound"));
                    this.loadKits();
                } else {
                    p.sendMessage(c(lang.getString("Locked Kit.msg")));
                    plugin.getUtils().playSound(p, lang.getString("Locked Kit.sound"));
                }

                break;
            }
        }

        plugin.debug("KitsMenu - onClickEvent " + plugin.getKitLoader().getKits().size());
    }

    @Override
    public void onClose(Player p) {

    }

    private void loadKits() {
        this.inventory().clear();
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(this.getPlayer().getUniqueId());

        plugin.getUtils().sortKits(skyPlayer, kits -> {
            for (Kit kit : kits) {
                boolean purchKit = skyPlayer.getPurchKits().contains(kit.getName());
                boolean isSelected = skyPlayer.getSelectedKit().equalsIgnoreCase(kit.getName());
                this.a(plugin.getUtils().createItem(kit, purchKit, isSelected, kit.isDefault()));
            }
        });
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String strip(String s) {
        return ChatColor.stripColor(s);
    }

}
