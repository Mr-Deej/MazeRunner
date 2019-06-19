package io.gpm.mazerunner.events.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/***
 * @author George
 * @since 19-Jun-19
 */
public class GameWinEvent extends Event implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public void teleport(Location location) {
        Bukkit.getServer().getWorld(location.getWorld().getUID()).getPlayers().forEach(pl -> pl.teleport(location));
    }

    public void killAllMobs(World world) {
        Bukkit.getServer().getWorld(world.getUID()).getEntities().forEach(e -> {
            if(!(e instanceof Player))
                e.remove();

        });
    }
}
