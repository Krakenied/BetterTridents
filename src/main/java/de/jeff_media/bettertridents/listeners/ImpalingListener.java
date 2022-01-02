package de.jeff_media.bettertridents.listeners;

import de.jeff_media.bettertridents.utils.EnchantmentUtil;
import de.jeff_media.bettertridents.utils.EntityUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ImpalingListener implements Listener {

    private final Random random;

    public ImpalingListener() {
        this.random = new Random();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        final int impaling;
        if (event.getDamager() instanceof Trident tridentLastDamager) {
            impaling = EnchantmentUtil.getImpaling(tridentLastDamager);
        } else if (event.getDamager() instanceof Player playerLastDamager) {
            impaling = playerLastDamager.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.IMPALING);
        } else {
            impaling = 0;
        }

        if (impaling <= 0 || EntityUtil.isAquatic(event.getEntity()) || !(EntityUtil.isInRain(event.getEntity()) || EntityUtil.isInWater(event.getEntity()))) {
            return;
        }

        // this.plugin.debug("Adjusting impaling damage, [damage = " + event.getDamage() + 2.5 * impaling + ", additional = " + 2.5 * impaling + "]");
        event.setDamage(event.getDamage() + (2.5 * impaling));
        this.displayEnchantedHit(event.getEntity());
    }

    private void displayEnchantedHit(@NotNull Entity entity) {
        for (int i = 0; i < 48; i++) { // 16 * 3
            final double d = (this.random.nextFloat() * 2.0F - 1.0F);
            final double e = (this.random.nextFloat() * 2.0F - 1.0F);
            final double f = (this.random.nextFloat() * 2.0F - 1.0F);

            if (Math.sqrt(d) + Math.sqrt(e) + Math.sqrt(f) > 1.0D) {
                continue;
            }

            final Location location = entity.getLocation().clone();
            location.add((entity.getWidth() * (d / 4.0D)),
                    (entity.getHeight() * (0.5D + e / 4.0D)),
                    (entity.getWidth() * (f / 4.0D)));
            location.getWorld().spawnParticle(Particle.CRIT_MAGIC, location, 0, d, e + 0.2D, f);
        }
    }

}
