package io.gpm.mazerunner.impl;

import io.gpm.mazerunner.game.GameLoop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/***
 * @author George
 * @since 19-Jun-19
 */
public class EntityLocationNotifier {

    /*
    EntityLocationNotifier
    this provides an armorstand that floats and spins in the
    air - the purpose of this is to notify players where a
    certain location is, in this game it is normally the spawn
    of the mobs that chase them and the location of the
    ores that give them points
     */

    private GameLoop loop = GameLoop.get();

    private ArmorStand stand;
    private Location location;
    private String name;
    private ItemStack head;
    private float rotation = 360;

    public EntityLocationNotifier(ArmorStand stand, Location location, String name, ItemStack head) {
        this.stand = stand;
        this.location = location;
        this.name = name;
        this.head = head;
    }

    public void spawn() {
        stand = (ArmorStand) Bukkit.getServer().getWorld(location.getWorld().getUID()).spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setCustomName(name);
        stand.setBasePlate(false);
        stand.setHelmet(head);
        spin();
    }

    private void spin() {
        while (rotation != 0) {
            stand.getLocation().setPitch(--rotation);
            if(rotation == 0)
                stand.getLocation().setPitch(++rotation);
        }
    }

    public void despawn() {
        if(loop.hasFinished())
            stand.remove();
    }
}
