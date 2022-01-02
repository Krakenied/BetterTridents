package de.jeff_media.bettertridents.config;

import org.jetbrains.annotations.NotNull;

public enum Permission {
    SAVE_VOID("bettertridents.savevoid"),
    RELOAD("bettertridents.reload");

    private final String permission;

    Permission(@NotNull String permission) {
        this.permission = permission;
    }

    public @NotNull String getValue() {
        return this.permission;
    }
}
