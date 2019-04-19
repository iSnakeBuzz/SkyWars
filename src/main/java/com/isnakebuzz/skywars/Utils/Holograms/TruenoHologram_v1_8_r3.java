package com.isnakebuzz.skywars.Utils.Holograms;

import com.isnakebuzz.skywars.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.skywars.Main;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TruenoHologram_v1_8_r3 implements TruenoHologram {

    private Location location;
    private List<String> lines;
    private double linesdistance = 0.26;
    private Material item;
    private ArrayList<ArmorStand> armor_lines = new ArrayList<ArmorStand>();
    private ArrayList<EntityArmorStand> NmsArmorLines = new ArrayList<EntityArmorStand>();

    //Move armorstand
    private HashMap<ArmorStand, Boolean> ab = new HashMap<>();
    private HashMap<ArmorStand, Integer> as = new HashMap<>();

    private Player player = null;

    @Override
    public void setupWorldHologram(Location loc, List<String> lines, double linedistance) {
        this.location = loc.clone();
        this.lines = lines;
        this.linesdistance = linedistance;
    }

    @Override
    public void setupFloatingItem(Location loc, Material item) {
        this.location = loc.clone();
        this.item = item;
    }

    @Override
    public void setupPlayerHologram(Player player, Location loc, ArrayList<String> lines) {
        this.player = player;
        this.location = loc.clone();
        this.lines = lines;
    }

    public Location getLocation() {
        return this.location;
    }

    public Player getPlayer() {
        return player;
    }

    private void NmsDestroy(EntityArmorStand hololine) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(hololine.getId());
        ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
    }

    private Location getNmsLocation(EntityArmorStand hololine) {
        return new Location(hololine.getWorld().getWorld(), hololine.locX, hololine.locY, hololine.locZ);
    }

    private void NmsSpawn(EntityArmorStand stand, String line, Location loc) {
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setCustomName(line);
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setSmall(true);
        stand.setInvisible(true);
        stand.setBasePlate(false);
        stand.setArms(false);
        if (!line.equals("")) {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
            ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void spawnItemFloating(Main plugin) {
        Location finalLoc = location.clone();
        final ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
        stand.setBasePlate(false);
        stand.setGravity(false);
        stand.setCanPickupItems(false);
        stand.setSmall(false);
        stand.setVisible(false);
        stand.setCustomNameVisible(false);
        stand.setHelmet(ItemBuilder.crearItem(item.getId(), 1, 0));
        int yLoc = (int) stand.getLocation().getY();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                /*float yaw = location.getYaw();
                if (yaw >= 179)
                    yaw = -180;
                else yaw += 6f;

                location.setYaw(yaw);
                stand.teleport(location);*/
            moveArmor(stand, yLoc);
        }, 0, 0);
    }

    public void moveArmor(final ArmorStand armorStand, final int n) {
        if (!this.ab.containsKey(armorStand)) {
            this.ab.put(armorStand, true);
        }
        if (!this.as.containsKey(armorStand)) {
            this.as.put(armorStand, 0);
        }
        float yaw = armorStand.getLocation().getYaw();
        double y = armorStand.getLocation().getY();
        final Location location = armorStand.getLocation();
        if (this.ab.get(armorStand)) {
            if (y <= n + 0.5) {
                y += 0.06;
                yaw += 18.0f;
            } else if (this.as.get(armorStand) == 10) {
                yaw += 16.0f;
                this.as.put(armorStand, this.as.get(armorStand) - 1);
                this.as.put(armorStand, this.as.get(armorStand) - 1);
            } else if (this.as.get(armorStand) == 9) {
                yaw += 16.0f;
                this.as.put(armorStand, this.as.get(armorStand) - 1);
            } else if (this.as.get(armorStand) == 8) {
                yaw += 14.0f;
                this.as.put(armorStand, this.as.get(armorStand) - 1);
            } else if (this.as.get(armorStand) == 7) {
                yaw += 12.0f;
                this.as.put(armorStand, this.as.get(armorStand) - 1);
            } else if (this.as.get(armorStand) == 6) {
                yaw += 12.0f;
                this.as.put(armorStand, this.as.get(armorStand) - 1);
            } else if (this.as.get(armorStand) == 5) {
                yaw += 10.0f;
                this.as.put(armorStand, this.as.get(armorStand) - 1);
            } else if (this.as.get(armorStand) == 4) {
                this.as.put(armorStand, this.as.get(armorStand) - 1);
                yaw += 10.0f;
            } else if (this.as.get(armorStand) == 3) {
                this.as.put(armorStand, this.as.get(armorStand) - 1);
                yaw += 8.0f;
            } else if (this.as.get(armorStand) == 2) {
                this.as.put(armorStand, this.as.get(armorStand) - 1);
                yaw += 6.0f;
            } else if (this.as.get(armorStand) == 1) {
                this.as.put(armorStand, this.as.get(armorStand) - 1);
                yaw += 4.0f;
            } else if (this.as.get(armorStand) == 0) {
                this.as.put(armorStand, 11);
                yaw += 4.0f;
            } else if (this.as.get(armorStand) == 11) {
                this.as.put(armorStand, this.as.get(armorStand) + 1);
                yaw -= 4.0f;
            } else if (this.as.get(armorStand) == 12) {
                this.as.put(armorStand, this.as.get(armorStand) + 1);
                yaw -= 6.0f;
            } else if (this.as.get(armorStand) == 13) {
                this.as.put(armorStand, this.as.get(armorStand) + 1);
                yaw -= 8.0f;
            } else if (this.as.get(armorStand) == 14) {
                this.as.put(armorStand, this.as.get(armorStand) + 1);
                yaw -= 10.0f;
            } else if (this.as.get(armorStand) == 15) {
                this.as.put(armorStand, this.as.get(armorStand) + 1);
                yaw -= 12.0f;
            } else if (this.as.get(armorStand) == 16) {
                this.as.put(armorStand, this.as.get(armorStand) + 1);
                yaw -= 14.0f;
            } else if (this.as.get(armorStand) == 17) {
                this.as.put(armorStand, 10);
                this.ab.put(armorStand, false);
                yaw -= 16.0f;
            }
        } else if (y >= n) {
            y -= 0.06;
            yaw -= 18.0f;
        } else if (this.as.get(armorStand) == 10) {
            yaw -= 16.0f;
            this.as.put(armorStand, this.as.get(armorStand) - 1);
        } else if (this.as.get(armorStand) == 9) {
            yaw -= 16.0f;
            this.as.put(armorStand, this.as.get(armorStand) - 1);
        } else if (this.as.get(armorStand) == 8) {
            yaw += 14.0f;
            this.as.put(armorStand, this.as.get(armorStand) - 1);
        } else if (this.as.get(armorStand) == 7) {
            yaw -= 12.0f;
            this.as.put(armorStand, this.as.get(armorStand) - 1);
        } else if (this.as.get(armorStand) == 6) {
            yaw -= 12.0f;
            this.as.put(armorStand, this.as.get(armorStand) - 1);
        } else if (this.as.get(armorStand) == 5) {
            yaw -= 10.0f;
            this.as.put(armorStand, this.as.get(armorStand) - 1);
        } else if (this.as.get(armorStand) == 4) {
            this.as.put(armorStand, this.as.get(armorStand) - 1);
            yaw -= 10.0f;
        } else if (this.as.get(armorStand) == 3) {
            this.as.put(armorStand, this.as.get(armorStand) - 1);
            yaw -= 8.0f;
        } else if (this.as.get(armorStand) == 2) {
            this.as.put(armorStand, this.as.get(armorStand) - 1);
            yaw -= 6.0f;
        } else if (this.as.get(armorStand) == 1) {
            this.as.put(armorStand, this.as.get(armorStand) - 1);
            yaw -= 4.0f;
        } else if (this.as.get(armorStand) == 0) {
            this.as.put(armorStand, 11);
            yaw -= 4.0f;
        } else if (this.as.get(armorStand) == 11) {
            this.as.put(armorStand, this.as.get(armorStand) + 1);
            yaw += 4.0f;
        } else if (this.as.get(armorStand) == 12) {
            this.as.put(armorStand, this.as.get(armorStand) + 1);
            yaw += 6.0f;
        } else if (this.as.get(armorStand) == 13) {
            this.as.put(armorStand, this.as.get(armorStand) + 1);
            yaw += 8.0f;
        } else if (this.as.get(armorStand) == 14) {
            this.as.put(armorStand, this.as.get(armorStand) + 1);
            yaw += 10.0f;
        } else if (this.as.get(armorStand) == 15) {
            this.as.put(armorStand, this.as.get(armorStand) + 1);
            yaw += 12.0f;
        } else if (this.as.get(armorStand) == 16) {
            this.as.put(armorStand, 10);
            this.ab.put(armorStand, true);
            yaw += 16.0f;
        }
        location.setYaw(yaw);
        location.setY(y);
        armorStand.teleport(location);
    }

    private void spawn() {
        int ind = 0;
        for (String line : lines) {
            Location finalLoc = location.clone();
            finalLoc.setY(location.getY() + (linesdistance * lines.size()));
            if (this.player != null) {
                if (ind > 0) finalLoc = getNmsLocation(NmsArmorLines.get(ind - 1));
                finalLoc.setY(finalLoc.getY() - linesdistance);
                WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                EntityArmorStand stand = new EntityArmorStand(s);
                NmsSpawn(stand, line, finalLoc);
                NmsArmorLines.add(stand);
            } else {
                if (ind > 0) finalLoc = armor_lines.get(ind - 1).getLocation();
                finalLoc.setY(finalLoc.getY() - linesdistance);
                ArmorStand Armorline = (ArmorStand) location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
                Armorline.setBasePlate(false);
                Armorline.setCustomNameVisible(true);
                Armorline.setGravity(false);
                Armorline.setCanPickupItems(false);
                Armorline.setCustomName(line);
                Armorline.setSmall(true);
                Armorline.setVisible(false);
                armor_lines.add(Armorline);
                if (line.equals("")) Armorline.remove();
            }
            ind++;
        }
    }

    private void despawn() {
        if (this.player != null) {
            for (EntityArmorStand nmsStand : NmsArmorLines) {
                NmsDestroy(nmsStand);
            }
            NmsArmorLines.clear();
        } else {
            for (ArmorStand line : armor_lines) {
                line.remove();
            }
            armor_lines.clear();
        }
    }

    public void setDistanceBetweenLines(Double distance) {
        this.linesdistance = distance;
    }

    public void display() {
        spawn();
    }

    public void newUpdate(List<String> lines) {
        if (this.player != null) {
            int ind = 0;
            for (String newline : lines) {
                if (this.lines.size() >= ind) {
                    String oldline = this.lines.get(ind);
                    if (!newline.equals(oldline)) {
                        if (!newline.equals("")) {
                            final EntityArmorStand oldstand = NmsArmorLines.get(ind);
                            Location finalLoc = location.clone();
                            finalLoc.setY(location.getY() + (linesdistance * lines.size()));
                            if (ind > 0) finalLoc = getNmsLocation(NmsArmorLines.get(ind - 1));
                            finalLoc.setY(finalLoc.getY() - linesdistance);
                            WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                            EntityArmorStand stand = new EntityArmorStand(s);
                            NmsSpawn(stand, newline, finalLoc);
                            this.NmsArmorLines.set(ind, stand);
                            this.lines.set(ind, newline);
                            NmsDestroy(oldstand);
                        } else {
                            this.lines.set(ind, newline);
                            final EntityArmorStand oldstand = NmsArmorLines.get(ind);
                            NmsDestroy(oldstand);
                        }
                    }
                    ind++;
                } else {
                    Location finalLoc = location.clone();
                    finalLoc.setY(location.getY() + (linesdistance * lines.size()));
                    if (ind > 0) finalLoc = getNmsLocation(NmsArmorLines.get(ind - 1));
                    finalLoc.setY(finalLoc.getY() - linesdistance);
                    WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                    EntityArmorStand stand = new EntityArmorStand(s);
                    NmsSpawn(stand, newline, finalLoc);
                    this.NmsArmorLines.add(stand);
                    this.lines.add(newline);
                }
            }
            if (lines.size() > this.lines.size()) {
                int dif = lines.size() - this.lines.size();
                for (int in = 0; in <= dif; in++) {
                    int arrayind = (this.lines.size() - 1) - in;
                    this.lines.remove(arrayind);
                    NmsDestroy(this.NmsArmorLines.get(arrayind));
                    this.NmsArmorLines.remove(arrayind);
                }
            }
        } else {
            int ind = 0;
            for (String newline : lines) {
                if (this.lines.size() >= ind) {
                    String oldline = this.lines.get(ind);
                    if (!newline.equals(oldline)) {
                        if (newline != "") {
                            this.armor_lines.get(ind).setCustomName(newline);
                        } else {
                            this.lines.set(ind, newline);
                            final ArmorStand oldstand = armor_lines.get(ind);
                            oldstand.remove();
                        }
                    }
                    ind++;
                } else {
                    Location finalLoc = location.clone();
                    finalLoc.setY(location.getY() + (linesdistance * lines.size()));
                    if (ind > 0) finalLoc = armor_lines.get(ind - 1).getLocation();
                    finalLoc.setY(finalLoc.getY() - linesdistance);
                    ArmorStand Armorline = (ArmorStand) location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
                    Armorline.setBasePlate(false);
                    Armorline.setCustomNameVisible(true);
                    Armorline.setGravity(false);
                    Armorline.setCanPickupItems(false);
                    Armorline.setCustomName(newline);
                    Armorline.setSmall(true);
                    Armorline.setVisible(false);
                    armor_lines.add(Armorline);
                    this.lines.add(newline);
                }
            }
            if (lines.size() > this.lines.size()) {
                int dif = lines.size() - this.lines.size();
                for (int in = 0; in <= dif; in++) {
                    int arrayind = (this.lines.size() - 1) - in;
                    this.lines.remove(arrayind);
                    this.armor_lines.get(arrayind).remove();
                    this.armor_lines.remove(arrayind);
                }
            }
        }
    }

    public void update(ArrayList<String> lines) {
        if (this.player != null) {
            int ind = 0;
            for (String newline : lines) {
                if (this.lines.size() >= ind) {
                    String oldline = this.lines.get(ind);
                    if (!newline.equals(oldline)) {
                        if (!newline.equals("")) {
                            final EntityArmorStand oldstand = NmsArmorLines.get(ind);
                            Location finalLoc = location.clone();
                            finalLoc.setY(location.getY() + (linesdistance * lines.size()));
                            if (ind > 0) finalLoc = getNmsLocation(NmsArmorLines.get(ind - 1));
                            finalLoc.setY(finalLoc.getY() - linesdistance);
                            WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                            EntityArmorStand stand = new EntityArmorStand(s);
                            NmsSpawn(stand, newline, finalLoc);
                            this.NmsArmorLines.set(ind, stand);
                            this.lines.set(ind, newline);
                            NmsDestroy(oldstand);
                        } else {
                            this.lines.set(ind, newline);
                            final EntityArmorStand oldstand = NmsArmorLines.get(ind);
                            NmsDestroy(oldstand);
                        }
                    }
                    ind++;
                } else {
                    Location finalLoc = location.clone();
                    finalLoc.setY(location.getY() + (linesdistance * lines.size()));
                    if (ind > 0) finalLoc = getNmsLocation(NmsArmorLines.get(ind - 1));
                    finalLoc.setY(finalLoc.getY() - linesdistance);
                    WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                    EntityArmorStand stand = new EntityArmorStand(s);
                    NmsSpawn(stand, newline, finalLoc);
                    this.NmsArmorLines.add(stand);
                    this.lines.add(newline);
                }
            }
            if (lines.size() > this.lines.size()) {
                int dif = lines.size() - this.lines.size();
                for (int in = 0; in <= dif; in++) {
                    int arrayind = (this.lines.size() - 1) - in;
                    this.lines.remove(arrayind);
                    NmsDestroy(this.NmsArmorLines.get(arrayind));
                    this.NmsArmorLines.remove(arrayind);
                }
            }
        } else {
            int ind = 0;
            for (String newline : lines) {
                if (this.lines.size() >= ind) {
                    String oldline = this.lines.get(ind);
                    if (!newline.equals(oldline)) {
                        if (newline != "") {
                            this.armor_lines.get(ind).setCustomName(newline);
                        } else {
                            this.lines.set(ind, newline);
                            final ArmorStand oldstand = armor_lines.get(ind);
                            oldstand.remove();
                        }
                    }
                    ind++;
                } else {
                    Location finalLoc = location.clone();
                    finalLoc.setY(location.getY() + (linesdistance * lines.size()));
                    if (ind > 0) finalLoc = armor_lines.get(ind - 1).getLocation();
                    finalLoc.setY(finalLoc.getY() - linesdistance);
                    ArmorStand Armorline = (ArmorStand) location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
                    Armorline.setBasePlate(false);
                    Armorline.setCustomNameVisible(true);
                    Armorline.setGravity(false);
                    Armorline.setCanPickupItems(false);
                    Armorline.setCustomName(newline);
                    Armorline.setSmall(true);
                    Armorline.setVisible(false);
                    armor_lines.add(Armorline);
                    this.lines.add(newline);
                }
            }
            if (lines.size() > this.lines.size()) {
                int dif = lines.size() - this.lines.size();
                for (int in = 0; in <= dif; in++) {
                    int arrayind = (this.lines.size() - 1) - in;
                    this.lines.remove(arrayind);
                    this.armor_lines.get(arrayind).remove();
                    this.armor_lines.remove(arrayind);
                }
            }
        }
    }

    public void updateLine(int index, String text) {
        if (this.lines.size() >= index) {
            int realindex = (this.lines.size() - 1) - index;
            String oldtext = this.lines.get(realindex);
            if (!text.equals(oldtext)) {
                if (this.player != null) {
                    if (text != "") {
                        final EntityArmorStand oldstand = NmsArmorLines.get(realindex);
                        Location finalLoc = location.clone();
                        finalLoc.setY(location.getY() + (linesdistance * lines.size()));
                        if (realindex > 0) finalLoc = getNmsLocation(NmsArmorLines.get(realindex - 1));
                        finalLoc.setY(finalLoc.getY() - linesdistance);
                        WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                        EntityArmorStand stand = new EntityArmorStand(s);
                        NmsSpawn(stand, text, finalLoc);
                        this.NmsArmorLines.set(realindex, stand);
                        NmsDestroy(oldstand);
                    } else {
                        this.lines.set(realindex, text);
                        final EntityArmorStand oldstand = NmsArmorLines.get(realindex);
                        NmsDestroy(oldstand);
                    }
                } else {
                    if (text != "") {
                        this.armor_lines.get(realindex).setCustomName(text);
                    } else {
                        final ArmorStand oldstand = armor_lines.get(realindex);
                        oldstand.remove();
                    }
                }
                this.lines.set(realindex, text);
            }
        }

    }

    public void removeLine(int index) {
        if (this.lines.size() >= index) {
            int realindex = (this.lines.size() - 1) - index;
            if (this.player != null) {
                final EntityArmorStand stand = NmsArmorLines.get(realindex);
                this.NmsArmorLines.remove(stand);
                NmsDestroy(stand);
            } else {
                this.armor_lines.get(realindex).remove();
            }
            this.lines.remove(realindex);
        }
    }

    public void delete() {
        despawn();
        this.player = null;
        this.NmsArmorLines = new ArrayList<>();
        this.armor_lines = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.location = null;
    }

}
