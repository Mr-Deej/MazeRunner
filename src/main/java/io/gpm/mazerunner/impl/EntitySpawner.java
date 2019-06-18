package io.gpm.mazerunner.impl;

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

    private ArmorStand stand;
    private Location location;
    private String name;
    private ItemStack head;

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
    }
}
