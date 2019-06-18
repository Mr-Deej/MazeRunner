package io.gpm.mazerunner;

import java.util.concurrent.atomic.AtomicInteger;

/***
 * @author George
 * @since 18-Jun-19
 */
public class GameInformation {

    public static final int MAX_PLAYERS = MazeRunner.getInstance().getConfig().getInt("game.max-players");
    public static AtomicInteger currentPlayers = new AtomicInteger(0);
}