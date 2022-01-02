package de.jeff_media.bettertridents.tasks;

import de.jeff_media.bettertridents.BetterTridents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class MoveToOffhand extends BukkitRunnable {

    private final Player player;
    private final ItemStack tridentItem;

    public MoveToOffhand(@NotNull Player player, @NotNull ItemStack tridentItem) {
        this.player = player;
        this.tridentItem = tridentItem;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.player.getInventory().getSize(); i++) {
            final ItemStack item = this.player.getInventory().getItem(i);
            if (item != null && item.equals(this.tridentItem)) {
                final ItemMeta meta = this.tridentItem.getItemMeta();
                meta.getPersistentDataContainer().remove(BetterTridents.OFFHAND_TAG);
                this.tridentItem.setItemMeta(meta);
                final ItemStack offhand = this.player.getInventory().getItemInOffHand();
                if (offhand.getType() == Material.AIR) {
                    this.player.getInventory().setItemInOffHand(this.tridentItem.clone());
                    this.tridentItem.setAmount(this.tridentItem.getAmount()-1);
                    this.player.getInventory().setItem(i, this.tridentItem);
                }
                break;
            }
        }

        this.player.updateInventory();
    }
}
