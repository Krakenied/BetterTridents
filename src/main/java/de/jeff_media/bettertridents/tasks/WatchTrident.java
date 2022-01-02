package de.jeff_media.bettertridents.tasks;

import de.jeff_media.bettertridents.BetterTridents;
import org.bukkit.entity.Trident;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class WatchTrident extends BukkitRunnable {

    private static final int MAX_TICKS = 1200;
    private final BetterTridents plugin;
    private final Trident trident;
    private int ticks = 0;

    public WatchTrident(@NotNull BetterTridents plugin, @NotNull Trident trident) {
        this.plugin = plugin;
        this.trident = trident;
    }

    private void rescue() {
        if (this.trident.getLocation().getY() >= -64) {
            return;
        }

        try {
            this.plugin.getTridentDamageDealtField().set(this.plugin.getTridentGetHandleMethod().invoke(trident), true);
        } catch (InvocationTargetException | IllegalAccessException e) {
            this.plugin.getLogger().severe(e.getMessage());
            this.cancel();
        }
    }

    @Override
    public void run() {
        this.ticks++;
        if (this.ticks >= WatchTrident.MAX_TICKS || this.trident.isDead() || !this.trident.isValid() || this.trident.getVelocity().length() == 0) {
            this.cancel();
            return;
        }
        this.rescue();
    }
}
