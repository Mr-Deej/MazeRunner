package io.gpm.mazerunner.utils;

import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
    }
}
