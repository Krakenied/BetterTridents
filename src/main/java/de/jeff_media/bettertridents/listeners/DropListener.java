package de.jeff_media.bettertridents.listeners;

import de.jeff_media.bettertridents.utils.EnchantmentUtil;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DropListener implements Listener {

    private final Random random;

    public DropListener() {
        this.random = new Random();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        if (!(event.getEntity().getLastDamageCause() instanceof final EntityDamageByEntityEvent damageEvent) || !(damageEvent.getDamager() instanceof final Player lastDamager) || !(event.getEntity() instanceof final Drowned drowned) || Boolean.FALSE.equals(drowned.getWorld().getGameRuleValue(GameRule.DO_MOB_LOOT)) || drowned.getEquipment().getItemInMainHand().getType() != Material.TRIDENT) {
            return;
        }

        for (final ItemStack drop : event.getDrops()) {
            if (drop.getType() == Material.TRIDENT) {
                return;
            }
        }

        final int chance = this.random.nextInt(100);
        final int fortune = EnchantmentUtil.getLevelFromTrident(lastDamager, Enchantment.LOOT_BONUS_MOBS);
        // this.plugin.debug("Using bedrock drop chance...  [chance = " + chance + ", fortune = " + fortune + "]");

        if (chance > 25 + 4 * fortune) {
            return;
        }

        final ItemStack tridentItem = new ItemStack(Material.TRIDENT, 1);
        final Damageable tridentItemMeta = (Damageable) tridentItem.getItemMeta();
        tridentItemMeta.setDamage(this.random.nextInt(248) + 1);
        tridentItem.setItemMeta(tridentItemMeta);

        event.getDrops().add(tridentItem);
    }

}
