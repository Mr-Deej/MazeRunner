package io.gpm.mazerunner.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/***
 * @author George
 * @since 19-Jun-19
 */
public class EntitiySpawner {

    private EntityType type;
    private Location location;
    private int amount;

    public EntitiySpawner(EntityType type, Location location, int amount) {
        this.type = type;
        this.location = location;
        this.amount = amount;
    }

    //this is a really bad way of doing it but fuck it
    public void spawn() {
        for(int i = 0; i < amount + 1; ++i) {
            Bukkit.getServer().getWorld(location.getWorld().getUID()).spawnEntity(location, type);
        }
    }
}
