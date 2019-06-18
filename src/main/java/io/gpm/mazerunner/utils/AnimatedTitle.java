package io.gpm.mazerunner.utils;

import io.gpm.mazerunner.MazeRunner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/***
 * @author George
 * @since 18-Jun-19
 */
public class AnimatedTitle {

    private HashMap<UUID, Integer> progress;
    private List<String> title;
    private List<String> subtitle;
    private int fadeIn, stay, fadeOut;
    private long delay, period;

    public AnimatedTitle(List<String> title, List<String> subtitle, int fadeIn, int stay, int fadeOut, long delay, long period) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.delay = delay;
        this.period = period;
    }

    public void send(Player player) {
        progress = new HashMap<>();
        progress.put(player.getUniqueId(), 0);

        //run the runnable to change the title packets
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!(progress.containsKey(player.getUniqueId()))) {
                    cancel();
                    return;
                }

                int p = progress.get(player.getUniqueId());

                if(title.size() > p || subtitle.size() > p) {
                    new Title(title.get(p), subtitle.get(p), fadeIn, stay, fadeOut).send(player);
                } else {
                    progress.remove(player.getUniqueId());
                }
            }
        }.runTaskTimer(MazeRunner.getInstance(), delay, period);
    }

    public void sendToAll() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::send);
    }
}
