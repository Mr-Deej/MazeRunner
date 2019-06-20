package io.gpm.mazerunner.events.impl;

import io.gpm.mazerunner.game.GameLoop;
import io.gpm.mazerunner.utils.GameInformation;
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
        return loop.hasFinished();
    }

    public boolean hasGotEnoughPoints() {
        return GameInformation.points.get() == GameInformation.MAX_POINTS;
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
