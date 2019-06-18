package io.gpm.mazerunner.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/***
 * @author George
 * @since 19-Jun-19
 */
public class CancelledEvents implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getEntity() instanceof Player)
            event.setCancelled(true);
    }
}
