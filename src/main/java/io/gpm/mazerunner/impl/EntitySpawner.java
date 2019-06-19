package io.gpm.mazerunner.impl;

import io.gpm.mazerunner.MazeRunner;
import io.gpm.mazerunner.game.GameLoop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

/***
 * @author George
 * @since 19-Jun-19
 */
public class EntitySpawner {

    private static GameLoop loop = GameLoop.get();
    private static EntityType type;
    private static Location location;
    private static int amount;

    public EntitySpawner() { }

    //this is a really bad way of doing it but fuck it
    public static void spawn() {
        for(int i = 0; i < amount + 1; ++i) {
            Bukkit.getServer().getWorld(location.getWorld().getUID()).spawnEntity(location, type);
        }
    }

    public static void onDeath(EntityDeathEvent event) {

        long respawnDelay = MazeRunner.getInstance().getConfig().getLong("game.mob-respawn-time");

        if(event.getEntity().getType() == type && !(event.getEntity() instanceof Player)
            && !(loop.isGameEnded())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    spawn();
                }
            }.runTaskTimer(MazeRunner.getInstance(), respawnDelay, 10L);
        }
    }
}
