package com.isnakebuzz.skywars.Inventory.Others;

import com.isnakebuzz.skywars.Inventory.MenuManager.Menu;
import com.isnakebuzz.skywars.Kits.Kit;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Player.SkyPlayer;
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

        for (Kit kit : plugin.getKitLoader().getKits()) {
            String itemName = strip(kit.getLogo().getItemMeta().getDisplayName());
            String cItemName = strip(item.getItemMeta().getDisplayName());

            plugin.debug(itemName);
            plugin.debug(cItemName);

            if (itemName.equalsIgnoreCase(cItemName)) {
                plugin.getPlayerManager().getPlayer(p).setSelectedKit(kit.getName());
                p.sendMessage(c(
                        lang.getString("Selected")
                                .replaceAll("%kit%", kit.getName())
                ));
                p.closeInventory();
                break;
            }
        }
    }

    @Override
    public void onClose(Player p) {

    }

    private void loadKits() {
        SkyPlayer skyPlayer = plugin.getPlayerManager().getPlayer(this.getPlayer());

        for (Kit kit : plugin.getKitLoader().getKits()) {
            boolean purchKit = skyPlayer.getPurchKits().contains(kit.getName());
            boolean isSelected = skyPlayer.getSelectedKit().equalsIgnoreCase(kit.getName());
            plugin.debug("" + isSelected);
            plugin.debug("" + purchKit);
            this.a(plugin.getUtils().createItem(kit, purchKit, isSelected, kit.isDefault()));
        }
    }

    private String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String strip(String s) {
        return ChatColor.stripColor(s);
    }

}
