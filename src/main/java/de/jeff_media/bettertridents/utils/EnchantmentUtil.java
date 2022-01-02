package de.jeff_media.bettertridents.utils;

import de.jeff_media.bettertridents.BetterTridents;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class EnchantmentUtil {

    public static int getLevelFromTrident(@NotNull Player player, @NotNull Enchantment enchantment) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.TRIDENT) {
            item = player.getInventory().getItemInOffHand();
        }

        if (item.getType() != Material.TRIDENT) {
            return 0;
        }

        if (!item.hasItemMeta()) {
            return 0;
        }

        final ItemMeta meta = item.getItemMeta();

        if (meta.hasEnchant(enchantment)) {
            return meta.getEnchantLevel(enchantment);
        }

        return 0;
    }

    public static int getImpaling(@NotNull Trident trident) {
        return trident.getPersistentDataContainer().getOrDefault(BetterTridents.IMPALING_TAG, PersistentDataType.INTEGER, 0);
    }

    public static void registerImpaling(@NotNull Trident trident, int level) {
        if (level == 0) {
            return;
        }
        trident.getPersistentDataContainer().set(BetterTridents.IMPALING_TAG, PersistentDataType.INTEGER, level);
    }

    public static int getLoyalty(@NotNull Trident trident) {
        return trident.getPersistentDataContainer().getOrDefault(BetterTridents.LOYALTY_TAG, PersistentDataType.INTEGER, 0);
    }

    public static void registerLoyalty(@NotNull Trident trident, int level) {
        if (level == 0) {
            return;
        }
        trident.getPersistentDataContainer().set(BetterTridents.LOYALTY_TAG, PersistentDataType.INTEGER, level);
    }

    public static boolean isOffhandThrown(@NotNull Trident trident) {
        return trident.getPersistentDataContainer().has(BetterTridents.OFFHAND_TAG, PersistentDataType.BYTE);
    }

    public static boolean hasLoyalty(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasEnchant(Enchantment.LOYALTY);
    }

}
