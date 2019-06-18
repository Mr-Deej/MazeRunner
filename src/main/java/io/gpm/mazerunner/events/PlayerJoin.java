package io.gpm.mazerunner.events;

import io.gpm.mazerunner.GameInformation;
import io.gpm.mazerunner.MazeRunner;
import io.gpm.mazerunner.game.GameLoop;
import io.gpm.mazerunner.utils.BossBar;
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

    private GameLoop loop = GameLoop.get();

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
        BossBar.set(player, ChatColor.GREEN +
                "Waiting for more players to join " + ChatColor.GRAY + "(" + ChatColor.YELLOW +
                GameInformation.currentPlayers.get() + ChatColor.GRAY + "/" + ChatColor.YELLOW + GameInformation.MAX_PLAYERS, 100);

        //the game logic, teleporting players and starting the loop
        if(GameInformation.currentPlayers.get() == GameInformation.MAX_PLAYERS) {
            Bukkit.getServer().getScheduler().runTask(MazeRunner.getInstance(), (Runnable) new GameLoop(loop.getLength(), loop.getDelay()));
            loop.setStarted(true);
            Bukkit.getServer().getWorld(world.getUID()).getPlayers().forEach(pl -> {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', MazeRunner.getInstance().getConfig().getString("game.start-message")));
                Location teleportLocation = new Location(world, MazeRunner.getInstance().getConfig().getInt("game.start-loc-x"),
                        MazeRunner.getInstance().getConfig().getInt("game.start-loc-y"),
                        MazeRunner.getInstance().getConfig().getInt("game.start-loc-z"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', MazeRunner.getInstance().getConfig().getString("game.teleport-message")));
                pl.teleport(teleportLocation);
            });
        }
    }
}
