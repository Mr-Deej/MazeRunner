package io.gpm.mazerunner.events.impl;

import io.gpm.mazerunner.GameInformation;
import io.gpm.mazerunner.MazeRunner;
import io.gpm.mazerunner.game.GameLoop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/***
 * @author George
 * @since 19-Jun-19
 */
public class GameRunEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private GameLoop loop = GameLoop.get();

    public boolean hasGameStarted() {
        return loop.isStarted();
    }

    public boolean hasEnded() {
        return loop.isGameEnded();
    }

    public int hasGotEnoughPoints() {
        return MazeRunner.getInstance().getConfig().getInt("game.required-points");
    }

    public void teleportToEnd(Location location) {
        Bukkit.getServer().getWorld(location.getWorld().getUID()).getPlayers().forEach(p -> p.teleport(location));
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public HandlerList getHandlersList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
