package de.jeff_media.bettertridents.commands;

import de.jeff_media.bettertridents.BetterTridents;
import de.jeff_media.bettertridents.config.Permission;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final BetterTridents plugin;

    public ReloadCommand(@NotNull BetterTridents plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission(Permission.RELOAD.getValue())) {
            sender.sendMessage(PaperComponents.legacySectionSerializer().deserialize(this.plugin.getServer().getPermissionMessage()));
            return true;
        }

        this.plugin.reload();
        sender.sendMessage(Component.text("BetterTridents and it's configuration have been reloaded successfully!", NamedTextColor.GREEN));
        return true;
    }
}
