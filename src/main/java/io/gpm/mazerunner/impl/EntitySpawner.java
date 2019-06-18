package io.gpm.mazerunner.impl;

import io.gpm.mazerunner.game.GameLoop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/***
 * @author George
 * @since 19-Jun-19
 */
public class EntitySpawner {

    private GameLoop loop = GameLoop.get();

    private ArmorStand stand;
    private Location location;
    private String name;
    private ItemStack head;
    private float rotation = 360;

    public EntitySpawner(ArmorStand stand, Location location, String name, ItemStack head) {
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

    public void spin() {
        while (rotation != 0) {
            stand.getLocation().setPitch(--rotation);
            if(rotation == 0)
                stand.getLocation().setPitch(++rotation);
        }
    }

    public void despawn() {
        if(loop.isGameEnded())
            stand.remove();
    }
}
