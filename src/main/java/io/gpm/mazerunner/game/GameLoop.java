package io.gpm.mazerunner.game;

import io.gpm.mazerunner.MazeRunner;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/***
 * @author George
 * @since 18-Jun-19
 */
public class GameLoop {

    private long length = 6000; //5 minutes -  think
    private long delay;
    private boolean started = false;
    private int loopTime = 6000; //5 minutes - think
    private BukkitTask runnable;
    private boolean gameEnded = false;
    private static GameLoop get;


    public GameLoop(long length, long delay) {
        get = this;

        this.length = length;
        this.delay = delay;

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                while(loopTime != 0) {
                    --loopTime;

                    if(loopTime == 0) {
                        gameEnded = true;
                        //todo send the end game stuff


                    }
                }

                if(gameEnded) {
                    cancel();
                    //todo teleport players
                }
            }
        }.runTaskTimer(MazeRunner.getInstance(), delay, length);
    }

    public long getLength() {
        return length;
    }

    public long getDelay() {
        return delay;
    }

    public boolean isStarted() {
        return started;
    }

    public int getLoopTime() {
        return loopTime;
    }

    public BukkitTask getRunnable() {
        return runnable;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public static GameLoop get() {
        return get;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
