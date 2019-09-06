package com.isnakebuzz.skywars.Utils.Holograms.nms;

import com.isnakebuzz.skywars.Utils.Holograms.TruenoHologram;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TruenoHologram_v1_8_r3 implements TruenoHologram {

    private Location location;
    private ArrayList<String> lines;
    private double linesdistance;
    private ArrayList<ArmorStand> armor_lines;
    private ArrayList<EntityArmorStand> NmsArmorLines;
    private Player player;

    public TruenoHologram_v1_8_r3() {
        this.linesdistance = 0.3;
        this.armor_lines = new ArrayList<>();
        this.NmsArmorLines = new ArrayList<>();
        this.player = null;
    }

    @Override
    public void setupWorldHologram(Location loc, ArrayList<String> lines) {
        this.location = loc.clone();
        this.lines = lines;
    }

    @Override
    public void setupPlayerHologram(Player player, Location loc, ArrayList<String> lines) {
        this.player = player;
        this.location = loc.clone();
        this.lines = lines;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    private void NmsDestroy(EntityArmorStand hololine) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(hololine.getId());
        ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
    }

    private Location getNmsLocation(EntityArmorStand hololine) {
        return new Location(hololine.getWorld().getWorld(), hololine.locX, hololine.locY, hololine.locZ);
    }

    @Override
    public ArrayList<Integer> getEntitiesIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (EntityArmorStand line : this.NmsArmorLines) {
            ids.add(line.getBukkitEntity().getEntityId());
        }
        return ids;
    }

    private void NmsSpawn(EntityArmorStand stand, String line, Location loc) {
        if (!line.equals("")) {
            stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setGravity(false);
            stand.setSmall(true);
            stand.setInvisible(true);
            stand.setBasePlate(false);
            stand.setArms(false);
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
            ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        } else {
            stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
        }
    }

    private void spawn() {
        int ind = 0;
        for (String line : this.lines) {
            Location finalLoc = this.location.clone();
            finalLoc.setY(this.location.getY() + this.linesdistance * this.lines.size());
            if (this.player != null) {
                if (ind > 0) {
                    finalLoc = this.getNmsLocation(this.NmsArmorLines.get(ind - 1));
                }
                finalLoc.setY(finalLoc.getY() - this.linesdistance);
                WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                EntityArmorStand stand = new EntityArmorStand(s);
                this.NmsArmorLines.add(stand);
                this.NmsSpawn(stand, line, finalLoc);
            } else {
                if (ind > 0) {
                    finalLoc = this.armor_lines.get(ind - 1).getLocation();
                }
                finalLoc.setY(finalLoc.getY() - this.linesdistance);
                ArmorStand Armorline = (ArmorStand) this.location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
                Armorline.setBasePlate(false);
                Armorline.setGravity(false);
                Armorline.setCanPickupItems(false);
                Armorline.setSmall(true);
                Armorline.setVisible(false);
                if (!line.equals("")) {
                    Armorline.setCustomName(line);
                    Armorline.setCustomNameVisible(true);
                }
                this.armor_lines.add(Armorline);
            }
            ++ind;
        }
    }

    private void despawn() {
        if (this.player != null) {
            for (EntityArmorStand nmsStand : this.NmsArmorLines) {
                this.NmsDestroy(nmsStand);
            }
            this.NmsArmorLines.clear();
        } else {
            for (ArmorStand line : this.armor_lines) {
                line.remove();
            }
            this.armor_lines.clear();
        }
    }

    @Override
    public void setDistanceBetweenLines(Double distance) {
        this.linesdistance = distance;
    }

    @Override
    public void display() {
        this.spawn();
    }

    @Override
    public void update(ArrayList<String> lines) {
        if (this.player != null) {
            int ind = 0;
            for (String newline : lines) {
                if (this.lines.size() >= ind) {
                    String oldline = this.lines.get(ind);
                    if (!newline.equals(oldline)) {
                        if (!newline.equals("")) {
                            EntityArmorStand oldstand = this.NmsArmorLines.get(ind);
                            Location finalLoc = this.location.clone();
                            finalLoc.setY(this.location.getY() + this.linesdistance * lines.size());
                            if (ind > 0) {
                                finalLoc = this.getNmsLocation(this.NmsArmorLines.get(ind - 1));
                            }
                            finalLoc.setY(finalLoc.getY() - this.linesdistance);
                            WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                            EntityArmorStand stand = new EntityArmorStand(s);
                            this.NmsSpawn(stand, newline, finalLoc);
                            this.NmsArmorLines.set(ind, stand);
                            this.lines.set(ind, newline);
                            this.NmsDestroy(oldstand);
                        } else {
                            this.lines.set(ind, newline);
                            EntityArmorStand oldstand = this.NmsArmorLines.get(ind);
                            this.NmsDestroy(oldstand);
                        }
                    }
                    ++ind;
                } else {
                    Location finalLoc2 = this.location.clone();
                    finalLoc2.setY(this.location.getY() + this.linesdistance * lines.size());
                    if (ind > 0) {
                        finalLoc2 = this.getNmsLocation(this.NmsArmorLines.get(ind - 1));
                    }
                    finalLoc2.setY(finalLoc2.getY() - this.linesdistance);
                    WorldServer s2 = ((CraftWorld) this.location.getWorld()).getHandle();
                    EntityArmorStand stand2 = new EntityArmorStand(s2);
                    this.NmsArmorLines.add(stand2);
                    this.lines.add(newline);
                    this.NmsSpawn(stand2, newline, finalLoc2);
                }
            }
            if (lines.size() > this.lines.size()) {
                for (int dif = lines.size() - this.lines.size(), in = 0; in <= dif; ++in) {
                    int arrayind = this.lines.size() - 1 - in;
                    this.lines.remove(arrayind);
                    this.NmsDestroy(this.NmsArmorLines.get(arrayind));
                    this.NmsArmorLines.remove(arrayind);
                }
            }
        } else {
            int ind = 0;
            for (String newline : lines) {
                if (this.lines.size() >= ind) {
                    String oldline = this.lines.get(ind);
                    if (!newline.equals(oldline)) {
                        if (!newline.equals("")) {
                            this.armor_lines.get(ind).setCustomNameVisible(true);
                            this.armor_lines.get(ind).setCustomName(newline);
                        } else {
                            this.armor_lines.get(ind).setCustomNameVisible(false);
                        }
                        this.lines.set(ind, newline);
                    }
                    ++ind;
                } else {
                    Location finalLoc2 = this.location.clone();
                    finalLoc2.setY(this.location.getY() + this.linesdistance * lines.size());
                    if (ind > 0) {
                        finalLoc2 = this.armor_lines.get(ind - 1).getLocation();
                    }
                    finalLoc2.setY(finalLoc2.getY() - this.linesdistance);
                    ArmorStand Armorline = (ArmorStand) this.location.getWorld().spawnEntity(finalLoc2, EntityType.ARMOR_STAND);
                    Armorline.setBasePlate(false);
                    Armorline.setCustomNameVisible(true);
                    Armorline.setGravity(false);
                    Armorline.setCanPickupItems(false);
                    Armorline.setCustomName(newline);
                    Armorline.setSmall(true);
                    Armorline.setVisible(false);
                    this.armor_lines.add(Armorline);
                    this.lines.add(newline);
                }
            }
            if (lines.size() > this.lines.size()) {
                for (int dif = lines.size() - this.lines.size(), in = 0; in <= dif; ++in) {
                    int arrayind = this.lines.size() - 1 - in;
                    this.lines.remove(arrayind);
                    this.armor_lines.get(arrayind).remove();
                    this.armor_lines.remove(arrayind);
                }
            }
        }
    }

    @Override
    public void updateLine(int index, String text) {
        if (this.lines.size() >= index) {
            int realindex = this.lines.size() - 1 - index;
            String oldtext = this.lines.get(realindex);
            if (!text.equals(oldtext)) {
                if (this.player != null) {
                    if (!text.equals("")) {
                        EntityArmorStand oldstand = this.NmsArmorLines.get(realindex);
                        Location finalLoc = this.location.clone();
                        finalLoc.setY(this.location.getY() + this.linesdistance * this.lines.size());
                        if (realindex > 0) {
                            finalLoc = this.getNmsLocation(this.NmsArmorLines.get(realindex - 1));
                        }
                        finalLoc.setY(finalLoc.getY() - this.linesdistance);
                        WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                        EntityArmorStand stand = new EntityArmorStand(s);
                        this.NmsSpawn(stand, text, finalLoc);
                        this.NmsArmorLines.set(realindex, stand);
                        this.NmsDestroy(oldstand);
                    } else {
                        this.lines.set(realindex, text);
                        EntityArmorStand oldstand = this.NmsArmorLines.get(realindex);
                        this.NmsDestroy(oldstand);
                    }
                } else if (!text.equals("")) {
                    this.armor_lines.get(realindex).setCustomName(text);
                } else {
                    ArmorStand oldstand2 = this.armor_lines.get(realindex);
                    oldstand2.remove();
                }
                this.lines.set(realindex, text);
            }
        }
    }

    @Override
    public void removeLine(int index) {
        if (this.lines.size() >= index) {
            int realindex = this.lines.size() - 1 - index;
            if (this.player != null) {
                EntityArmorStand stand = this.NmsArmorLines.get(realindex);
                this.NmsArmorLines.remove(stand);
                this.NmsDestroy(stand);
            } else {
                this.armor_lines.get(realindex).remove();
            }
            this.lines.remove(realindex);
        }
    }

    @Override
    public void delete() {
        this.despawn();
        this.player = null;
        this.NmsArmorLines = new ArrayList<>();
        this.armor_lines = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.location = null;
    }
}
