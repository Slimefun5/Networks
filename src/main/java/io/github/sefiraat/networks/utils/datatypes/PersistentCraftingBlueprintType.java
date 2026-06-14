package io.github.sefiraat.networks.utils.datatypes;

import io.github.sefiraat.networks.network.stackcaches.BlueprintInstance;
import io.github.sefiraat.networks.compat.Pdc;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Version-safe persistence for {@link BlueprintInstance} (recipe array + output).
 * <p>
 * Stores flat String-keyed (Base64) values through {@link PersistentDataAPI} instead of a nested
 * {@code PersistentDataContainer}, so it never references {@code org.bukkit.persistence.*} in bytecode.
 */
public final class PersistentCraftingBlueprintType {

    private PersistentCraftingBlueprintType() {}

    private static final String RECIPE = "networks:blueprint_recipe";
    private static final String OUTPUT = "networks:blueprint_output";

    @Nullable
    public static BlueprintInstance read(@Nonnull ItemMeta meta) {
        if (!Pdc.hasString(meta, RECIPE)) {
            return null;
        }
        final ItemStack[] recipe = SerializationUtils.itemStackArrayFromString(Pdc.getString(meta, RECIPE, null));
        final ItemStack output = SerializationUtils.itemStackFromString(Pdc.getString(meta, OUTPUT, null));
        if (recipe == null || output == null) {
            return null;
        }
        return new BlueprintInstance(recipe, output);
    }

    public static void store(@Nonnull ItemMeta meta, @Nonnull BlueprintInstance instance) {
        final String recipe = SerializationUtils.itemStackArrayToString(instance.getRecipeItems());
        final String output = SerializationUtils.itemStackToString(instance.getItemStack());
        if (recipe != null) {
            Pdc.setString(meta, RECIPE, recipe);
        }
        if (output != null) {
            Pdc.setString(meta, OUTPUT, output);
        }
    }
}
