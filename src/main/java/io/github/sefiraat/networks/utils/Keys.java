package io.github.sefiraat.networks.utils;

import io.github.sefiraat.networks.Networks;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.BukkitKeys;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class Keys {
    private Keys() {}

    public static final NamespacedKey ON_COOLDOWN = newKey("cooldown");
    public static final NamespacedKey CARD_INSTANCE = newKey("ntw_card");
    public static final NamespacedKey QUANTUM_STORAGE_INSTANCE = newKey("quantum_storage");
    public static final NamespacedKey BLUEPRINT_INSTANCE = newKey("ntw_blueprint");
    public static final NamespacedKey FACE = newKey("face");
    public static final NamespacedKey ITEM = newKey("item");

    @Nonnull
    public static NamespacedKey newKey(@Nonnull String value) {
        return new NamespacedKey(Networks.getInstance(), value);
    }

    /**
     * Converts one of the addon's version-safe {@link NamespacedKey keys} to the real
     * {@code org.bukkit.NamespacedKey} required by the (1.14+) PersistentData API. Returns {@code null}
     * on legacy servers (1.8&ndash;1.13) where {@code org.bukkit.NamespacedKey} is absent, so the PDC
     * boundary degrades to a no-op instead of throwing at load.
     */
    @Nullable
    public static org.bukkit.NamespacedKey bukkit(@Nonnull NamespacedKey key) {
        return (org.bukkit.NamespacedKey) BukkitKeys.toBukkit(key);
    }
}
