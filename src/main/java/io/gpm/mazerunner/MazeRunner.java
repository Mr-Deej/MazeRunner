package io.gpm.mazerunner;

import io.gpm.mazerunner.events.CancelledEvents;
import io.gpm.mazerunner.events.GameEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

/***
 * @author George
 * @since 18-Jun-19
 */
public class MazeRunner extends JavaPlugin {

    private static MazeRunner instance;

    @Override
    public void onEnable() {
        instance = this;

        Stream.of(
                new GameEvents(),
                new CancelledEvents()
        ).forEach(l -> getServer().getPluginManager().registerEvents(l,this));
    }

    @Override
    public void onDisable() {
        instance = this;
    }

    public static MazeRunner getInstance() {
        return instance;
    }
}
