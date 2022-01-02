package de.jeff_media.bettertridents.config;

import de.jeff_media.bettertridents.BetterTridents;
import org.jetbrains.annotations.NotNull;

public enum Setting {
    VOID_SAVING("void-saving", true),
    DROP_BEDROCK_CHANCE("bedrock-drop-chance", true),
    BEDROCK_IMPALING("bedrock-impaling", true),
    RETURN_TO_OFFHAND("return-to-offhand", true),
    DISABLE_LOYALTY_PORTALS("disable-loyalty-portals", true),
    DEBUG("debug", false);

    private final String path;
    private final Object value;

    Setting(@NotNull String path, @NotNull Object value) {
        this.path = path;
        this.value = value;
    }

    public @NotNull String getPath() {
        return this.path;
    }

    public @NotNull Object getValue() {
        return this.value;
    }

    public boolean getBoolean(@NotNull BetterTridents plugin) {
        return plugin.getConfig().getBoolean(this.path, (Boolean) this.value);
    }
}