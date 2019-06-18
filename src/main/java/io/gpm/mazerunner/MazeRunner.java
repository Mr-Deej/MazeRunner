package io.gpm.mazerunner;

import org.bukkit.plugin.java.JavaPlugin;

/***
 * @author George
 * @since 18-Jun-19
 */
public class MazeRunner extends JavaPlugin {

    private static MazeRunner instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = this;
    }

    public static MazeRunner getInstance() {
        return instance;
    }
}
