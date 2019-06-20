package io.gpm.mazerunner.impl;

import io.gpm.mazerunner.game.GameLoop;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

/***
 * @author George
 * @since 20-Jun-19
 */
public class ItemSpawner {

    private GameLoop loop = GameLoop.get();
    private Location location;
    private ItemStack item;
    private long period, delay;

    public ItemSpawner(GameLoop loop, Location location, ItemStack item, long period, long delay) {
        this.loop = loop;
        this.location = location;
        this.item = item;
        this.period = period;
        this.delay = delay;
    }


}
