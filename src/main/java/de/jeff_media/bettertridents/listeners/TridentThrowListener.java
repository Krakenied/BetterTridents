package de.jeff_media.bettertridents.listeners;

import de.jeff_media.bettertridents.BetterTridents;
import de.jeff_media.bettertridents.config.Permission;
import de.jeff_media.bettertridents.tasks.WatchTrident;
import de.jeff_media.bettertridents.utils.EnchantmentUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class TridentThrowListener implements Listener {

    private final BetterTridents plugin;

    public TridentThrowListener(@NotNull BetterTridents plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunchImpaling(@NotNull ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof final Trident trident && trident.getShooter() instanceof final Player player)) {
            return;
        }

        final int impaling = EnchantmentUtil.getLevelFromTrident(player, Enchantment.IMPALING);
        EnchantmentUtil.registerImpaling(trident, impaling);

        if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT || player.getInventory().getItemInOffHand().getType() != Material.TRIDENT) {
            return;
        }

        trident.getPersistentDataContainer().set(BetterTridents.OFFHAND_TAG, PersistentDataType.BYTE, (byte) 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunchLoyalty(@NotNull ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof final Trident trident) || !(trident.getShooter() instanceof final Player player) || !(player.hasPermission(Permission.SAVE_VOID.getValue()))) {
            return;
        }

        final ItemStack tridentItem;
        if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
            tridentItem = player.getInventory().getItemInMainHand();
        } else if (player.getInventory().getItemInOffHand().getType() == Material.TRIDENT) {
            tridentItem = player.getInventory().getItemInOffHand();
        }  else {
            tridentItem = null;
        }

        if (tridentItem == null || !EnchantmentUtil.hasLoyalty(tridentItem)) {
            return;
        }

        new WatchTrident(this.plugin, trident).runTaskTimer(this.plugin,1L,1L);
    }

}
