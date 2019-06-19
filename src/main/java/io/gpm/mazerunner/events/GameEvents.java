package io.gpm.mazerunner.events;

import io.gpm.mazerunner.impl.EntityLocationNotifier;
import io.gpm.mazerunner.utils.GameInformation;
import io.gpm.mazerunner.MazeRunner;
import io.gpm.mazerunner.events.impl.GameRunEvent;
import io.gpm.mazerunner.game.GameLoop;
import io.gpm.mazerunner.utils.AnimatedTitle;
import io.gpm.mazerunner.utils.BossBar;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
    private Map<Material, Integer> costMappings;

    {
        costMappings = new HashMap<Material, Integer>() {
            {
                put(Material.COAL, 10);
                put(Material.IRON_INGOT, 25);
                put(Material.DIAMOND, 100);
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

        //handle everything for the zombie spawning and the rotating armorstand
        Location zombieSpawnLocation = new Location(world, MazeRunner.getInstance().getConfig().getInt("game.zombie-spawn-loc-x"),
                MazeRunner.getInstance().getConfig().getInt("game.zombie-spawn-loc-y"),
                MazeRunner.getInstance().getConfig().getInt("game.zombie-spawn-loc-z"));
        Location skeletonSpawnLocation = new Location(world, MazeRunner.getInstance().getConfig().getInt("game.zombie-spawn-loc-x"),
                MazeRunner.getInstance().getConfig().getInt("game.zombie-spawn-loc-y"),
                MazeRunner.getInstance().getConfig().getInt("game.zombie-spawn-loc-z"));

        Entity zombieStand = Bukkit.getWorld(world.getUID()).spawnEntity(zombieSpawnLocation, EntityType.ARMOR_STAND);
        Entity skeletonStand = Bukkit.getWorld(world.getUID()).spawnEntity(skeletonSpawnLocation, EntityType.ARMOR_STAND);


        ItemStack zombieHead = new ItemStack(Material.SKULL_ITEM); //todo replace the head with a zombie head
        ItemStack skeletonHead = new ItemStack(Material.SKULL_ITEM); //this by default is a skeleton head - i think

        EntityLocationNotifier zombieNotifier = new EntityLocationNotifier((ArmorStand) zombieStand, zombieSpawnLocation, "Zombies", zombieHead);
        EntityLocationNotifier skeletonNotifier = new EntityLocationNotifier((ArmorStand) skeletonStand, skeletonSpawnLocation, "Skeletons", skeletonHead);


        if(event.hasGameStarted()) {

            zombieNotifier.spawn();
            skeletonNotifier.spawn();

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

                    //spawn the zombies

                    if(event.hasGotEnoughPoints()) {
                        event.setCancelled(true);
                        cancel();
                        zombieNotifier.despawn();
                        skeletonNotifier.despawn();
                    }

                }
            }.runTaskTimer(MazeRunner.getInstance(), loop.getDelay(), loop.getLength());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if(event.getRightClicked() instanceof Villager &&
                event.getRightClicked().getCustomName().equalsIgnoreCase(MazeRunner.getInstance().getConfig().getString("game.banker-name"))) {

            //cost mappings and such
            Arrays.stream(player.getInventory().getContents()).forEach(item -> {
                if(costMappings.containsKey(item.getType())) {
                    item.setAmount(0);
                    GameInformation.points.addAndGet(costMappings.get(item.getType()));
                }
            });
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        player.getInventory().clear();

        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        player.getInventory().setItemInHand(new ItemStack(Material.STONE_SWORD));
    }
}
