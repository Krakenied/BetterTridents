package de.jeff_media.bettertridents.listeners;

import de.jeff_media.bettertridents.BetterTridents;
import de.jeff_media.bettertridents.tasks.MoveToOffhand;
import de.jeff_media.bettertridents.utils.EnchantmentUtil;
import org.bukkit.Material;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class OffhandListener implements Listener {

    private final BetterTridents plugin;

    public OffhandListener(BetterTridents plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerPickupArrow(@NotNull PlayerPickupArrowEvent event) {
        if (!(event.getArrow() instanceof final Trident trident) || !EnchantmentUtil.isOffhandThrown(trident) || event.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR) {
            return;
        }

        final ItemStack tridentItem = event.getItem().getItemStack().clone();
        final ItemMeta tridentItemMeta = tridentItem.getItemMeta();
        tridentItemMeta.getPersistentDataContainer().set(BetterTridents.OFFHAND_TAG, PersistentDataType.BYTE, (byte) 1);
        tridentItem.setItemMeta(tridentItemMeta);
        event.getItem().setItemStack(tridentItem);

        // this.plugin.debug("Starting offhand task...");
        new MoveToOffhand(event.getPlayer(), tridentItem).runTask(this.plugin);
    }
}


