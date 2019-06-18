package io.gpm.mazerunner.utils;

import io.gpm.mazerunner.MazeRunner;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * @author George
 * @since 18-Jun-19
 */
public class BossBar {

    private static Map<String, EntityEnderDragon> dragons = new ConcurrentHashMap<>();

    public static void set(Player player, String text, float health) {
        PlayerConnection craftPlayer = ((CraftPlayer)player).getHandle().playerConnection;

        //setup the location for the ender dragon to spawn
        Location location = player.getLocation();
        WorldServer world = ((CraftWorld)player.getLocation().getWorld()).getHandle();

        //spawn the ender dragon and set its location so that it's invisible
        EntityEnderDragon dragon = new EntityEnderDragon(world);
        dragon.setLocation(location.getX(), location.getY() - 100, location.getZ(), 0, 0);

        //spawn the dragon through packets for the player
        PacketPlayOutSpawnEntityLiving dragonPacket = new PacketPlayOutSpawnEntityLiving(dragon);

        //run the data watcher - this is what sets the health and the text
        DataWatcher watcher = new DataWatcher(dragon);
        watcher.a(0, (byte) 0x20);
        watcher.a(6, (health * 200) / 100);
        watcher.a(10, text);
        watcher.a(2, text);
        watcher.a(11, (byte) 1);
        watcher.a(3, (byte) 1);

        //assign the data watcher to the entity
        try {
            Field t = PacketPlayOutSpawnEntityLiving.class.getDeclaredField("l");
            t.setAccessible(true);
            t.set(dragonPacket, watcher);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            MazeRunner.getInstance().getLogger().severe("Could not set the datawatcher to the dragon packet!");
        }

        dragons.put(player.getName(), dragon);
        craftPlayer.sendPacket(dragonPacket);
    }

    public static void removeBar(Player player) {
        if(dragons.containsKey(player.getUniqueId())) {
            Location location = player.getLocation();
            PacketPlayOutEntityDestroy killPacket = new PacketPlayOutEntityDestroy(dragons.get(player.getUniqueId()).getId());
            dragons.remove(player.getUniqueId());
            (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(killPacket);
        }
    }

    public static void removeEveryonesBar() {
        Bukkit.getOnlinePlayers().forEach(pl -> removeBar(pl));
    }

    public static void setForAll(String text, float health) {
        Bukkit.getOnlinePlayers().forEach(pl -> set(pl, text, health));
    }

    public static void teleportBar(Player player) {
        if(dragons.containsKey(player.getUniqueId())) {
            Location location = player.getLocation();
            PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(dragons.get(player.getName()).getId(),
                    (int) location.getX() * 32, (int) location.getY() - 100 * 32, (int) location.getZ() * 32,
                    (byte) ((int) location.getYaw() * 256 / 360), (byte) ((int) location.getPitch() * 256 / 360), false);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(teleportPacket);
        }
    }

    public static void teleportEveryonesbar() {
        Bukkit.getOnlinePlayers().forEach(pl -> teleportBar(pl));
    }
}
