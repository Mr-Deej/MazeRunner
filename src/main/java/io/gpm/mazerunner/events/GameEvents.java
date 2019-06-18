package io.gpm.mazerunner.events;

import io.gpm.mazerunner.utils.GameInformation;
import io.gpm.mazerunner.MazeRunner;
import io.gpm.mazerunner.events.impl.GameRunEvent;
import io.gpm.mazerunner.game.GameLoop;
import io.gpm.mazerunner.utils.AnimatedTitle;
import io.gpm.mazerunner.utils.BossBar;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @author George
 * @since 18-Jun-19
 */
public class GameEvents implements Listener {

    private GameLoop loop = GameLoop.get();
    private World world = Bukkit.getWorld(MazeRunner.getInstance().getConfig().getString("game.world"));

    //cost mappings for the game points system
    private Map<Material, Double> costMappings;

    {
        costMappings = new HashMap<Material, Double>() {
            {
                put(Material.COAL, 10.0);
                put(Material.IRON_INGOT, 25.0);
                put(Material.DIAMOND, 100.0);
                put(Material.EMERALD, 150.0);
                put(Material.COAL_BLOCK, 90.0);
                put(Material.IRON_BLOCK, 225.0);
                put(Material.DIAMOND_BLOCK, 900.0);
                put(Material.EMERALD_BLOCK, 1350.0);
            }
        };
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int x = MazeRunner.getInstance().getConfig().getInt("game.pre-spawn-x"),
                y = MazeRunner.getInstance().getConfig().getInt("game.pre-spawn-y"),
                z = MazeRunner.getInstance().getConfig().getInt("game.pre-spawn-z");

        Location startLocation = new Location(world, x, y, z);
        player.teleport(startLocation);

        //welcome title
        List<String> welcomeTitle = Arrays.asList(
            ChatColor.GOLD + "[   " + ChatColor.LIGHT_PURPLE + "Welcome!" + ChatColor.GOLD + "   ]",
            ChatColor.GOLD + "[  " + ChatColor.LIGHT_PURPLE + "Welcome!" + ChatColor.GOLD + "  ]",
            ChatColor.GOLD + "[ " + ChatColor.LIGHT_PURPLE + "Welcome!" + ChatColor.GOLD + " ]",
            ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "Welcome!" + ChatColor.GOLD + "]"
        );

        List<String> welcomeSubtitle = Arrays.asList(
                ChatColor.RED + "To the MazeRunner",
                ChatColor.DARK_RED + "To the MazeRunner",
                ChatColor.RED + "To the MazeRunner",
                ChatColor.DARK_RED + "To the MazeRunner",
                ChatColor.RED + "To the MazeRunner",
                ChatColor.DARK_RED + "To the MazeRunner",
                ChatColor.RED + "To the MazeRunner"
        );


        new AnimatedTitle(welcomeTitle, welcomeSubtitle, 10, 10, 10, 0, 5).send(player);

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

    @EventHandler
    public void gameRun(GameRunEvent event) {
        if(event.hasGameStarted()) {

            //kill all entities in the world except players
            Bukkit.getServer().getWorld(world.getUID()).getEntities().forEach(e -> {
                if(!(e instanceof Player))
                    e.remove();
            });

            //player armor

            Bukkit.getServer().getWorld(world.getUID()).getPlayers().forEach(pl -> {

                pl.getInventory().clear();

                pl.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                pl.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                pl.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                pl.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                pl.getInventory().setItemInHand(new ItemStack(Material.STONE_SWORD));

            });

            BossBar.updateEveryonesBar(ChatColor.RED + "Current points: " +
                    ChatColor.GREEN + "0" + ChatColor.GRAY + "/" + ChatColor.GREEN + GameInformation.MAX_POINTS, 0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    BossBar.updateEveryonesBar(ChatColor.RED + "Current points: " +
                            ChatColor.GREEN + GameInformation.points.get() + ChatColor.GRAY + "/" + ChatColor.GREEN + GameInformation.MAX_POINTS, 0);

                }
            }.runTaskTimer(MazeRunner.getInstance(), loop.getDelay(), loop.getLength());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if(event.getRightClicked() instanceof Villager &&
                event.getRightClicked().getCustomName().equalsIgnoreCase(MazeRunner.getInstance().getConfig().getString("game.banker-name"))) {
            //todo cost mappings and bar update
        }
    }
}
