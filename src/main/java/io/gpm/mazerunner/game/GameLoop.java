package io.gpm.mazerunner.game;

import io.gpm.mazerunner.MazeRunner;
import org.bukkit.scheduler.BukkitRunnable;

/***
 * @author George
 * @since 18-Jun-19
 */
public class GameLoop extends BukkitRunnable {

    private static GameLoop get;
    private long period, delay; //todo assign this to the value later
    @Override
    public void run() {
        get = this;
        runTaskTimer(MazeRunner.getInstance(), delay, period);
    }

    public static GameLoop get() {
        return get;
    }
}
