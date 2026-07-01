package io.github.sefiraat.networks.utils.datatypes;

import io.github.sefiraat.networks.compat.Pdc;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Version-safe persistent-data helpers keyed by plain {@link String}s.
 * <p>
 * The legacy implementation routed through {@code org.bukkit.persistence.*} (1.14+) which the JVM verifier
 * links at enable, breaking 1.8. Every value is now stored as a flat String-keyed entry through
 * {@link PersistentDataAPI}, so no {@code org.bukkit.persistence.*} type appears in bytecode.
 */
public final class DataTypeMethods {

    private DataTypeMethods() {}

    public static void setItemStackArray(@Nonnull ItemMeta meta, @Nonnull String key, @Nullable ItemStack[] value) {
        final String serialized = SerializationUtils.itemStackArrayToString(value);
        if (serialized != null) {
            Pdc.setString(meta, key, serialized);
        }
    }

    @Nullable
    public static ItemStack[] getItemStackArray(@Nonnull ItemMeta meta, @Nonnull String key) {
        return SerializationUtils.itemStackArrayFromString(Pdc.getString(meta, key, null));
    }

    public static void setString(@Nonnull ItemMeta meta, @Nonnull String key, @Nonnull String value) {
        Pdc.setString(meta, key, value);
    }

    @Nullable
    public static String getString(@Nonnull ItemMeta meta, @Nonnull String key) {
        return Pdc.getString(meta, key, null);
    }

    public static void setLocation(@Nonnull ItemMeta meta, @Nonnull String key, @Nonnull Location value) {
        Pdc.setString(meta, key, SerializationUtils.locationToString(value));
    }

    @Nullable
    public static Location getLocation(@Nonnull ItemMeta meta, @Nonnull String key) {
        return SerializationUtils.locationFromString(Pdc.getString(meta, key, null));
    }
}
