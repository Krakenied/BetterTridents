package de.jeff_media.bettertridents.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public final class EntityUtil {

    public static boolean isAquatic(@NotNull Entity entity) {
        return EntityUtil.isAquatic(entity.getType());
    }

    public static boolean isAquatic(@NotNull EntityType type) {
        return switch (type) {
            case DOLPHIN, GUARDIAN, ELDER_GUARDIAN, SQUID, TURTLE, COD, SALMON, PUFFERFISH, TROPICAL_FISH -> true;
            default -> false;
        };
    }

    public static boolean isInRain(@NotNull Entity entity) {
        final World world = entity.getWorld();

        if (!world.hasStorm()) {
            return false;
        }

        final Location min = entity.getBoundingBox().getMin().toLocation(world);
        final Location max = entity.getBoundingBox().getMax().toLocation(world);

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                final Block highest = world.getHighestBlockAt(x, z);
                if (highest.getType().isAir() || highest.getY() < entity.getLocation().getY()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isInWater(@NotNull Entity entity) {
        final World world = entity.getWorld();
        final Location min = entity.getBoundingBox().getMin().toLocation(world);
        final Location max = entity.getBoundingBox().getMax().toLocation(world);

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    if (world.getBlockAt(x, y, z).getType() == Material.WATER) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
