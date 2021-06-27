package com.isnakebuzz.skywars.Inventory.MenuManager;

import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public abstract class Menu implements Listener {

    private Inventory _inv;
    private SkyWars plugin;
    private String _name;
    private Player player;

    public Menu(SkyWars plugin, String menu_name, Player player) {
        this._name = menu_name;
        this.plugin = plugin;
        this.player = player;
        Configuration config = plugin.getConfig("Extra/MenuCreator");
        String path = "MenuCreator." + menu_name + ".";
        if (config.getString(path + "title") == null) return;
        this._inv = Bukkit.createInventory(null, 9 * config.getInt(path + "rows"), ChatColor.translateAlternateColorCodes('&', config.getString(path + "title")));
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void a(final ItemStack itemStack) {
        this._inv.addItem(itemStack);
    }

    public void s(final int n, final ItemStack itemStack) {
        this._inv.setItem(n, itemStack);
    }

    public Inventory inventory() {
        return this._inv;
    }

    public String n() {
        return this._inv.getName();
    }

    public void open() {
        if (this._inv == null) {
            player.sendMessage("§4Menu error.. Wrong name.");
        }
        player.openInventory(this._inv);
    }

    public Player getPlayer() {
        return player;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent inventoryClickEvent) throws IOException {
        if (inventoryClickEvent.getInventory().equals(this.inventory()) && inventoryClickEvent.getCurrentItem() != null && this.inventory().contains(inventoryClickEvent.getCurrentItem()) && inventoryClickEvent.getWhoClicked() instanceof Player) {
            this.onClick((Player) inventoryClickEvent.getWhoClicked(), inventoryClickEvent.getCurrentItem(), _name);
            inventoryClickEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent inventoryCloseEvent) {
        if (inventoryCloseEvent.getInventory().equals(this.inventory()) && inventoryCloseEvent.getPlayer() instanceof Player) {
            this.onClose((Player) inventoryCloseEvent.getPlayer());
            HandlerList.unregisterAll(this);
        }
    }

    public abstract void onClose(final Player p);

    public abstract void onClick(final Player p0, final ItemStack p1, String _name) throws IOException;

}
