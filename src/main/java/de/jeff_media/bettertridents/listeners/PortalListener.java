package de.jeff_media.bettertridents.listeners;

import de.jeff_media.bettertridents.utils.EnchantmentUtil;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.jetbrains.annotations.NotNull;

public class PortalListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityPortal(@NotNull EntityPortalEvent event) {
        if (!(event.getEntity() instanceof final Trident trident) || EnchantmentUtil.getLoyalty(trident) <= 0) {
            return;
        }

        // this.plugin.debug("Prevented loyalty trident from travelling through a portal!");
        event.setCancelled(true);
    }

}
