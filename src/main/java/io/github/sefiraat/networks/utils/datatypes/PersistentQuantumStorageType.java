package io.github.sefiraat.networks.utils.datatypes;

import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.compat.Pdc;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Version-safe persistence for {@link QuantumCache}.
 * <p>
 * The legacy implementation stored a nested {@code PersistentDataContainer} (1.14+). This class instead
 * stores the cache as flat String-keyed values through {@link PersistentDataAPI}, so it never references
 * {@code org.bukkit.persistence.*} in bytecode and is safe to link at enable on 1.8.
 */
public final class PersistentQuantumStorageType {

    private PersistentQuantumStorageType() {}

    private static final String ITEM = "networks:quantum_item";
    private static final String AMOUNT = "networks:quantum_amount";
    private static final String MAX_AMOUNT = "networks:quantum_max_amount";
    private static final String VOID = "networks:quantum_void";

    @Nullable
    public static QuantumCache read(@Nonnull ItemMeta meta) {
        if (!Pdc.hasString(meta, ITEM) && !Pdc.hasInt(meta, AMOUNT)) {
            return null;
        }
        final ItemStack item = SerializationUtils.itemStackFromString(Pdc.getString(meta, ITEM, null));
        final int amount = Pdc.getInt(meta, AMOUNT, 0);
        final int limit = Pdc.getInt(meta, MAX_AMOUNT, 0);
        final boolean voidExcess = Pdc.getBoolean(meta, VOID);
        return new QuantumCache(item, amount, limit, voidExcess);
    }

    public static void store(@Nonnull ItemMeta meta, @Nonnull QuantumCache cache) {
        final String serialized = SerializationUtils.itemStackToString(cache.getItemStack());
        if (serialized != null) {
            Pdc.setString(meta, ITEM, serialized);
        }
        Pdc.setInt(meta, AMOUNT, cache.getAmount());
        Pdc.setInt(meta, MAX_AMOUNT, cache.getLimit());
        Pdc.setBoolean(meta, VOID, cache.isVoidExcess());
    }
}
