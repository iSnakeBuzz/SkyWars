package com.isnakebuzz.skywars.Chest;

import com.google.common.collect.Lists;
import com.isnakebuzz.skywars.Main;
import com.isnakebuzz.skywars.Utils.Enums.ProjectileType;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public final class ChestController {

    private List<ChestItem> op_iSland = Lists.newArrayList(), no_iSland = Lists.newArrayList(), ba_iSland = Lists.newArrayList();
    private List<ChestItem> op_Center = Lists.newArrayList(), no_Center = Lists.newArrayList(), ba_Center = Lists.newArrayList();

    private List<Material> bannedItems;

    private Random random = new Random();

    private List<Integer> randomLoc = new ArrayList<>();
    private Main plugin;
    private SettingsManager op_Conf_center, nor_Conf_center, ba_Conf_center;
    private SettingsManager op_Conf_island, nor_Conf_island, ba_Conf_island;

    public ChestController(Main plugin) {
        this.plugin = plugin;

        //Banned items
        this.bannedItems = new ArrayList<>();

        this.bannedItems.add(Material.EGG);
        this.bannedItems.add(Material.SNOW_BALL);
        this.bannedItems.add(Material.ARROW);
        this.bannedItems.add(Material.BOW);

        //End of Banned items

        op_Conf_island = new SettingsManager(plugin, "Chests/Islands/OVERPOWERED");
        nor_Conf_island = new SettingsManager(plugin, "Chests/Islands/NORMAL");
        ba_Conf_island = new SettingsManager(plugin, "Chests/Islands/BASIC");

        op_Conf_center = new SettingsManager(plugin, "Chests/Center/OVERPOWERED");
        nor_Conf_center = new SettingsManager(plugin, "Chests/Center/NORMAL");
        ba_Conf_center = new SettingsManager(plugin, "Chests/Center/BASIC");
        for (int i = 0; i < 27; i++) {
            randomLoc.add(i);
        }
    }

    public void load() {
        loadCenter();
        loadIsland();
    }

    private void loadCenter() {
        //Load overpowered
        op_Center.clear();
        if (op_Conf_center.getConfig().contains("Loot")) {
            for (String item : op_Conf_center.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);
                if (plugin.getSkyWarsArena().getProjectileType().equals(ProjectileType.TWROWABLES)) {
                    if (itemStack != null) {
                        if (!this.bannedItems.contains(itemStack.getType())) {
                            op_Center.add(new ChestItem(itemStack, chance));
                        }
                    }
                } else {
                    if (itemStack != null) {
                        op_Center.add(new ChestItem(itemStack, chance));
                    }
                }

            }
        }

        //Load normal
        no_Center.clear();
        if (nor_Conf_center.getConfig().contains("Loot")) {
            for (String item : nor_Conf_center.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);

                if (plugin.getSkyWarsArena().getProjectileType().equals(ProjectileType.TWROWABLES)) {
                    if (itemStack != null) {
                        if (!this.bannedItems.contains(itemStack.getType())) {
                            no_Center.add(new ChestItem(itemStack, chance));
                            plugin.debug("Loading " + itemStack.getType().toString());
                        }
                    }
                } else {
                    if (itemStack != null) {
                        no_Center.add(new ChestItem(itemStack, chance));
                    }
                }
            }
        }

        //Load basic
        ba_Center.clear();
        if (ba_Conf_center.getConfig().contains("Loot")) {
            for (String item : ba_Conf_center.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);

                if (plugin.getSkyWarsArena().getProjectileType().equals(ProjectileType.TWROWABLES)) {
                    if (itemStack != null) {
                        if (!this.bannedItems.contains(itemStack.getType())) {
                            ba_Center.add(new ChestItem(itemStack, chance));
                        }
                    }
                } else {
                    if (itemStack != null) {
                        ba_Center.add(new ChestItem(itemStack, chance));
                    }
                }
            }
        }
    }

    private void loadIsland() {
        //Load overpowered
        op_iSland.clear();
        if (op_Conf_island.getConfig().contains("Loot")) {
            for (String item : op_Conf_island.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);

                if (plugin.getSkyWarsArena().getProjectileType().equals(ProjectileType.TWROWABLES)) {
                    if (itemStack != null) {
                        if (!this.bannedItems.contains(itemStack.getType())) {
                            op_iSland.add(new ChestItem(itemStack, chance));
                        }
                    }
                } else {
                    if (itemStack != null) {
                        op_iSland.add(new ChestItem(itemStack, chance));
                    }
                }
            }
        }

        //Load normal
        no_iSland.clear();
        if (nor_Conf_island.getConfig().contains("Loot")) {
            for (String item : nor_Conf_island.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);

                if (plugin.getSkyWarsArena().getProjectileType().equals(ProjectileType.TWROWABLES)) {
                    if (itemStack != null) {
                        if (!this.bannedItems.contains(itemStack.getType())) {
                            no_iSland.add(new ChestItem(itemStack, chance));
                        }
                    }
                } else {
                    if (itemStack != null) {
                        no_iSland.add(new ChestItem(itemStack, chance));
                    }
                }
            }
        }

        //Load basic
        ba_iSland.clear();
        if (ba_Conf_island.getConfig().contains("Loot")) {
            for (String item : ba_Conf_island.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);

                if (plugin.getSkyWarsArena().getProjectileType().equals(ProjectileType.TWROWABLES)) {
                    if (itemStack != null) {
                        if (!this.bannedItems.contains(itemStack.getType())) {
                            ba_iSland.add(new ChestItem(itemStack, chance));
                        }
                    }
                } else {
                    if (itemStack != null) {
                        ba_iSland.add(new ChestItem(itemStack, chance));
                    }
                }
            }
        }

    }

    public void reFillIslands(Chest chest) {
        Inventory inventory = chest.getBlockInventory();
        inventory.clear();
        int added = 0;
        int max = 15;
        int min = 5;
        Collections.shuffle(this.randomLoc);
        for (final ChestItem chestItem : this.getIsland()) {
            if (this.random.nextInt(10) + 1 <= chestItem.getChance()) {
                inventory.setItem((int) this.randomLoc.get(added), chestItem.getItem());
                // Random int entre 21 25 22.
                if (added++ >= inventory.getSize() - (new Random().nextInt((max - min) + 1) + min)) {
                    break;
                }
            }
        }
    }

    public void reFillCenter(Chest chest) {
        Inventory inventory = chest.getBlockInventory();
        inventory.clear();
        int added = 0;
        Collections.shuffle(this.randomLoc);
        int max = 15;
        int min = 5;
        for (final ChestItem chestItem : this.getCenter()) {
            if (this.random.nextInt(10) + 1 <= chestItem.getChance()) {
                inventory.setItem((int) this.randomLoc.get(added), chestItem.getItem());
                // Random int entre 21 22 24 18.s
                if (added++ >= inventory.getSize() - (new Random().nextInt((max - min) + 1) + min)) {
                    break;
                }
            }
        }
    }

    public List<ChestItem> getCenter() {
        switch (plugin.getSkyWarsArena().getChestType()) {
            case BASIC:
                return this.ba_Center;
            case NORMAL:
                return this.no_Center;
            case OVERPOWERED:
                return this.op_Center;
        }

        return this.no_Center;
    }

    public List<ChestItem> getIsland() {
        switch (plugin.getSkyWarsArena().getChestType()) {
            case BASIC:
                return this.ba_iSland;
            case NORMAL:
                return this.no_iSland;
            case OVERPOWERED:
                return this.op_iSland;
        }

        return this.no_iSland;
    }

    public class ChestItem {

        private final ItemStack item;
        private final int chance;

        public ChestItem(ItemStack item, int chance) {
            this.item = item;
            this.chance = chance;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getChance() {
            return chance;
        }
    }

    public static ItemStack parseItem(List<String> item) {
        if (item.size() < 2) {
            return null;
        }

        ItemStack itemStack = null;

        try {
            if (item.get(0).contains(":")) {
                Material material = Material.getMaterial(item.get(0).split(":")[0].toUpperCase());
                int amount = Integer.parseInt(item.get(1));
                if (amount < 1) {
                    return null;
                }
                short data = (short) Integer.parseInt(item.get(0).split(":")[1].toUpperCase());
                itemStack = new ItemStack(material, amount, data);
            } else {
                itemStack = new ItemStack(Material.getMaterial(item.get(0).toUpperCase()),
                        Integer.parseInt(item.get(1)));
            }

            if (item.size() > 2) {
                for (int x = 2; x < item.size(); x++) {
                    if (item.get(x).split(":")[0].equalsIgnoreCase("name")) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(item.get(x).split(":")[1]);
                        itemStack.setItemMeta(itemMeta);
                    } else {
                        itemStack.addUnsafeEnchantment(getEnchant(item.get(x).split(":")[0]),
                                Integer.parseInt(item.get(x).split(":")[1]));
                    }
                }

            }

        } catch (Exception ignored) {

        }
        return itemStack;
    }

    private static Enchantment getEnchant(String enchant) {
        switch (enchant.toLowerCase()) {
            case "protection":
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "projectileprotection":
                return Enchantment.PROTECTION_PROJECTILE;
            case "fireprotection":
                return Enchantment.PROTECTION_FIRE;
            case "featherfall":
                return Enchantment.PROTECTION_FALL;
            case "blastprotection":
                return Enchantment.PROTECTION_EXPLOSIONS;
            case "respiration":
                return Enchantment.OXYGEN;
            case "aquaaffinity":
                return Enchantment.WATER_WORKER;
            case "sharpness":
                return Enchantment.DAMAGE_ALL;
            case "smite":
                return Enchantment.DAMAGE_UNDEAD;
            case "baneofarthropods":
                return Enchantment.DAMAGE_ARTHROPODS;
            case "knockback":
                return Enchantment.KNOCKBACK;
            case "fireaspect":
                return Enchantment.FIRE_ASPECT;
            case "depthstrider":
                return Enchantment.DEPTH_STRIDER;
            case "looting":
                return Enchantment.LOOT_BONUS_MOBS;
            case "power":
                return Enchantment.ARROW_DAMAGE;
            case "punch":
                return Enchantment.ARROW_KNOCKBACK;
            case "flame":
                return Enchantment.ARROW_FIRE;
            case "infinity":
                return Enchantment.ARROW_INFINITE;
            case "efficiency":
                return Enchantment.DIG_SPEED;
            case "silktouch":
                return Enchantment.SILK_TOUCH;
            case "unbreaking":
                return Enchantment.DURABILITY;
            case "fortune":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "luckofthesea":
                return Enchantment.LUCK;
            case "luck":
                return Enchantment.LUCK;
            case "lure":
                return Enchantment.LURE;
            case "thorns":
                return Enchantment.THORNS;
            default:
                return null;
        }
    }
}
