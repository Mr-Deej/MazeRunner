package io.gpm.mazerunner.events;

import io.gpm.mazerunner.GameInformation;
import io.gpm.mazerunner.MazeRunner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/***
 * @author George
 * @since 18-Jun-19
 */
public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int x = MazeRunner.getInstance().getConfig().getInt("game.pre-spawn-x"),
                y = MazeRunner.getInstance().getConfig().getInt("game.pre-spawn-y"),
                z = MazeRunner.getInstance().getConfig().getInt("game.pre-spawn-z");
        World world = Bukkit.getWorld(MazeRunner.getInstance().getConfig().getString("game.world"));
        Location startLocation = new Location(world, x, y, z);
        player.teleport(startLocation);

        //update the max players and check
        GameInformation.currentPlayers.getAndIncrement();

        if(GameInformation.currentPlayers.get() == GameInformation.MAX_PLAYERS) {
            Bukkit.getServer().getWorld(world.getUID()).getPlayers().forEach(pl -> {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', MazeRunner.getInstance().getConfig().getString("game.start-message")));
                Location teleportLocation = new Location(world, MazeRunner.getInstance().getConfig().getInt("game.start-loc-x"),
                        MazeRunner.getInstance().getConfig().getInt("game.start-loc-y"),
                        MazeRunner.getInstance().getConfig().getInt("game.start-loc-z"));
            });
        }
    }
}
