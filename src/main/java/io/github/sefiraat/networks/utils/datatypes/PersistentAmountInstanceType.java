package io.github.sefiraat.networks.utils.datatypes;

import io.github.sefiraat.networks.network.stackcaches.CardInstance;
import io.github.sefiraat.networks.compat.Pdc;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Version-safe persistence for the amount/limit of a {@link CardInstance} (item intentionally omitted).
 * <p>
 * Stores flat String-keyed values through {@link PersistentDataAPI} instead of a nested
 * {@code PersistentDataContainer}, so it never references {@code org.bukkit.persistence.*} in bytecode.
 */
public final class PersistentAmountInstanceType {

    private PersistentAmountInstanceType() {}

    private static final String AMOUNT = "networks:amount_amount";
    private static final String LIMIT = "networks:amount_limit";

    @Nullable
    public static CardInstance read(@Nonnull ItemMeta meta) {
        if (!Pdc.hasInt(meta, AMOUNT)) {
            return null;
        }
        final int amount = Pdc.getInt(meta, AMOUNT, 0);
        final int limit = Pdc.getInt(meta, LIMIT, 0);
        return new CardInstance(null, amount, limit);
    }

    public static void store(@Nonnull ItemMeta meta, @Nonnull CardInstance instance) {
        Pdc.setInt(meta, AMOUNT, instance.getAmount());
        Pdc.setInt(meta, LIMIT, instance.getLimit());
    }
}
