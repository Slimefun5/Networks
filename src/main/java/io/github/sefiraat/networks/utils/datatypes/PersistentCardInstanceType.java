package io.github.sefiraat.networks.utils.datatypes;

import io.github.sefiraat.networks.network.stackcaches.CardInstance;
import io.github.sefiraat.networks.compat.Pdc;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Version-safe persistence for {@link CardInstance} (item + amount + limit).
 * <p>
 * Stores flat String-keyed values through {@link PersistentDataAPI} instead of a nested
 * {@code PersistentDataContainer}, so it never references {@code org.bukkit.persistence.*} in bytecode.
 */
public final class PersistentCardInstanceType {

    private PersistentCardInstanceType() {}

    private static final String ITEM = "networks:card_item";
    private static final String AMOUNT = "networks:card_amount";
    private static final String LIMIT = "networks:card_limit";

    public static boolean has(@Nonnull ItemMeta meta) {
        return Pdc.hasInt(meta, AMOUNT) || Pdc.hasString(meta, ITEM);
    }

    @Nullable
    public static CardInstance read(@Nonnull ItemMeta meta) {
        if (!has(meta)) {
            return null;
        }
        final ItemStack item = SerializationUtils.itemStackFromString(Pdc.getString(meta, ITEM, null));
        final int amount = Pdc.getInt(meta, AMOUNT, 0);
        final int limit = Pdc.getInt(meta, LIMIT, 0);
        return new CardInstance(item, amount, limit);
    }

    public static void store(@Nonnull ItemMeta meta, @Nonnull CardInstance instance) {
        final String serialized = SerializationUtils.itemStackToString(instance.getItemStack());
        if (serialized != null) {
            Pdc.setString(meta, ITEM, serialized);
        }
        Pdc.setInt(meta, AMOUNT, instance.getAmount());
        Pdc.setInt(meta, LIMIT, instance.getLimit());
    }
}
