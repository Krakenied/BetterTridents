package de.jeff_media.bettertridents;

import de.jeff_media.bettertridents.commands.ReloadCommand;
import de.jeff_media.bettertridents.config.Setting;
import de.jeff_media.bettertridents.config.Settings;
import de.jeff_media.bettertridents.listeners.*;
import de.jeff_media.bettertridents.utils.ReflUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public class BetterTridents extends JavaPlugin {

    public static NamespacedKey LOYALTY_TAG;
    public static NamespacedKey IMPALING_TAG;
    public static NamespacedKey OFFHAND_TAG;

    private Settings settings;
    private Class<?> entityThrownTridentClass;
    private Field tridentDamageDealtField;
    private Method tridentGetHandleMethod;
    private boolean debug;

    @Override
    public void onEnable() {
        LOYALTY_TAG = new NamespacedKey(this, "loyalty");
        IMPALING_TAG = new NamespacedKey(this, "impaling");
        OFFHAND_TAG = new NamespacedKey(this, "offhand");

        Optional.ofNullable(this.getCommand(this.getName().toLowerCase()))
                .ifPresentOrElse(pluginCommand -> {
                    this.settings = new Settings(this);
                    this.debug = this.getConfig().getBoolean(Setting.DEBUG.getPath(), (Boolean) Setting.DEBUG.getValue());

                    if (this.debug) {
                        this.getLogger().warning("Debug mode enabled - this may affect performance!");
                    }

                    try {
                        this.entityThrownTridentClass = Class.forName("net.minecraft.world.entity.projectile.EntityThrownTrident");
                        this.tridentGetHandleMethod = ReflUtil.getOBCClass("entity.CraftTrident").getMethod("getHandle");
                        for (final Field field : entityThrownTridentClass.getDeclaredFields()) {
                            if (field.getType() == Boolean.TYPE) {
                                this.tridentDamageDealtField = field;
                                this.tridentDamageDealtField.setAccessible(true);
                            }
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        this.getLogger().severe(e.getMessage());
                    }

                    if (this.entityThrownTridentClass == null || this.tridentGetHandleMethod == null || this.tridentDamageDealtField == null) {
                        this.getLogger().severe("An unexpected error occurred while reflection class detection process, disabling!");
                        this.getServer().getPluginManager().disablePlugin(this);
                        return;
                    }

                    this.getServer().getLogger().info("Tridents will be rescued using reflection (field: '" + tridentDamageDealtField.getName() + "')!");

                    this.reload(true);
                    pluginCommand.setExecutor(new ReloadCommand(this));
                }, () -> {
                    this.getLogger().severe("An unexpected error occurred while registering '" + this.getName().toLowerCase() + "' command, disabling!");
                    this.getServer().getPluginManager().disablePlugin(this);
                });
    }

    public @NotNull Field getTridentDamageDealtField() {
        return this.tridentDamageDealtField;
    }

    public @NotNull Method getTridentGetHandleMethod() {
        return this.tridentGetHandleMethod;
    }

    public void reload() {
        this.reload(false);
    }

    public void reload(boolean firstLoad) {
        this.getServer().getLogger().info("Reloading the plugin and it's configuration...");
        this.settings.reload();

        if (!firstLoad) {
            this.getServer().getLogger().info("Unregistering all the plugin's listeners from all handlers lists...");
            final int listenerCount = HandlerList.getRegisteredListeners(this).size();
            HandlerList.unregisterAll(this);
            this.getServer().getLogger().info("Successfully unregistered " + listenerCount + " plugin's listeners!");
        }

        if (Setting.DROP_BEDROCK_CHANCE.getBoolean(this)) {
            this.getServer().getLogger().info("Registering bedrock drop chance listener...");
            this.getServer().getPluginManager().registerEvents(new DropListener(), this);
        }

        if (Setting.BEDROCK_IMPALING.getBoolean(this)) {
            this.getServer().getLogger().info("Registering bedrock impaling listener...");
            this.getServer().getPluginManager().registerEvents(new ImpalingListener(), this);
        }

        if (Setting.RETURN_TO_OFFHAND.getBoolean(this)) {
            this.getServer().getLogger().info("Registering return to offhand listener...");
            this.getServer().getPluginManager().registerEvents(new OffhandListener(this), this);
        }

        if (Setting.DISABLE_LOYALTY_PORTALS.getBoolean(this)) {
            this.getServer().getLogger().info("Registering disable loyalty portals listener...");
            this.getServer().getPluginManager().registerEvents(new PortalListener(), this);
        }

        if (Setting.VOID_SAVING.getBoolean(this)) {
            this.getServer().getLogger().info("Registering void saving listener...");
            this.getServer().getPluginManager().registerEvents(new TridentThrowListener(this), this);
        }

        this.getLogger().info("Successfully registered " + HandlerList.getRegisteredListeners(this).size() + " listeners!");
        this.getLogger().info("BetterTridents and it's configuration have been reloaded successfully!");
    }

    public void debug(@NotNull String msg) {
        if (this.debug) {
            this.getLogger().warning("[DEBUG] " + msg);
        }
    }
}
