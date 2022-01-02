package de.jeff_media.bettertridents.config;

import de.jeff_media.bettertridents.BetterTridents;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

public class Settings {

    private final BetterTridents plugin;
    private final File file;

    public Settings(@NotNull BetterTridents plugin) {
        this.plugin = plugin;
        this.file = new File(this.plugin.getDataFolder(), "config.yml");
        this.addDefaults();
        this.reload();
    }

    public void reload() {
        if (!this.file.exists()) {
            this.plugin.saveDefaultConfig();
        }

        this.plugin.reloadConfig();
    }

    private void addDefaults() {
        Arrays.stream(Setting.values()).forEach(setting -> this.plugin.getConfig().addDefault(setting.getPath(), setting.getValue()));
    }
}
