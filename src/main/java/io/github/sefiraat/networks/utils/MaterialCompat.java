package io.github.sefiraat.networks.utils;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Version-safe {@link Material} helpers for APIs added after Minecraft 1.8.8.
 * <p>
 * Many {@code org.bukkit.Material} constants used across the addon were introduced in 1.13+ (dyes,
 * stained glass, terracotta, coral, vines, ...). Referencing them directly throws
 * {@link NoSuchFieldError} at class-init on legacy servers. Resolving them through {@link XMaterial}
 * instead yields the correct {@link Material} for the running version. {@link XMaterial#parseMaterial()}
 * can still return {@code null} for a material with no legacy equivalent, so every entry point here
 * substitutes a {@link #FALLBACK} placeholder rather than passing {@code null} to a constructor.
 */
public final class MaterialCompat {

    /** Legacy-safe placeholder; {@code STONE} exists on every supported Minecraft version. */
    private static final Material FALLBACK = Material.STONE;

    private MaterialCompat() {}

    // Sensible legacy substitutes for materials that don't exist on older servers (e.g. 1.8).
    private static final java.util.Map<XMaterial, XMaterial> LEGACY_SUBSTITUTES = buildLegacySubstitutes();

    private static java.util.Map<XMaterial, XMaterial> buildLegacySubstitutes() {
        java.util.Map<XMaterial, XMaterial> m = new java.util.EnumMap<>(XMaterial.class);
        m.put(XMaterial.NETHERITE_BLOCK, XMaterial.DIAMOND_BLOCK);
        m.put(XMaterial.NETHERITE_INGOT, XMaterial.DIAMOND);
        m.put(XMaterial.NETHERITE_SCRAP, XMaterial.IRON_NUGGET);
        m.put(XMaterial.ANCIENT_DEBRIS, XMaterial.NETHERRACK);
        m.put(XMaterial.BEEHIVE, XMaterial.DISPENSER);
        m.put(XMaterial.BEE_NEST, XMaterial.DISPENSER);
        m.put(XMaterial.HONEY_BLOCK, XMaterial.SLIME_BLOCK);
        m.put(XMaterial.BARREL, XMaterial.CHEST);
        m.put(XMaterial.BLAST_FURNACE, XMaterial.FURNACE);
        m.put(XMaterial.SMOKER, XMaterial.FURNACE);
        m.put(XMaterial.CAMPFIRE, XMaterial.NETHERRACK);
        m.put(XMaterial.SMITHING_TABLE, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.CARTOGRAPHY_TABLE, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.FLETCHING_TABLE, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.LOOM, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.STONECUTTER, XMaterial.CRAFTING_TABLE);
        m.put(XMaterial.GRINDSTONE, XMaterial.ANVIL);
        m.put(XMaterial.LANTERN, XMaterial.GLOWSTONE);
        m.put(XMaterial.COMPOSTER, XMaterial.CHEST);
        m.put(XMaterial.MAGMA_BLOCK, XMaterial.NETHERRACK);
        m.put(XMaterial.LODESTONE, XMaterial.IRON_BLOCK);
        m.put(XMaterial.BLACKSTONE, XMaterial.COBBLESTONE);
        m.put(XMaterial.OBSERVER, XMaterial.PISTON);
        return m;
    }

    private static Material substitute(XMaterial xMaterial) {
        XMaterial sub = LEGACY_SUBSTITUTES.get(xMaterial);
        return sub != null ? sub.parseMaterial() : null;
    }

    /**
     * Resolves an {@link XMaterial} to a non-null {@link Material} for the running version.
     */
    @Nonnull
    public static Material material(@Nonnull XMaterial xMaterial) {
        Material material = xMaterial.parseMaterial();
        if (material == null) {
            material = substitute(xMaterial);
        }
        return material != null ? material : FALLBACK;
    }

    /**
     * Builds an {@link ItemStack} from an {@link XMaterial}, substituting {@link #FALLBACK} when the
     * material does not exist on the running (legacy) server.
     */
    @Nonnull
    public static ItemStack stack(@Nonnull XMaterial xMaterial) {
        return new ItemStack(material(xMaterial));
    }

    /**
     * Version-safe replacement for {@code Material#isAir()} (added in Minecraft 1.16). On older
     * servers the method is absent, so air is detected by name instead.
     */
    public static boolean isAir(@Nonnull Material material) {
        switch (material.name()) {
            case "AIR":
            case "CAVE_AIR":
            case "VOID_AIR":
            case "LEGACY_AIR":
                return true;
            default:
                return false;
        }
    }

    /**
     * Version-safe replacement for {@code Material#isItem()} (added in Minecraft 1.13). On older
     * servers every non-air material can back an item, so that is the legacy assumption.
     */
    public static boolean isItem(@Nonnull Material material) {
        Boolean result = invokeBooleanMethod(material, IS_ITEM_METHOD);
        return result != null ? result : !isAir(material);
    }

    /**
     * Version-safe replacement for {@code Material#isFuel()} (added in Minecraft 1.13). On older
     * servers the method is absent and there is no reliable lookup, so {@code false} is returned -
     * the calling code falls back to its non-fuel branch.
     */
    public static boolean isFuel(@Nonnull Material material) {
        Boolean result = invokeBooleanMethod(material, IS_FUEL_METHOD);
        return result != null && result;
    }

    private static final java.lang.reflect.Method IS_ITEM_METHOD = resolveMethod("isItem");
    private static final java.lang.reflect.Method IS_FUEL_METHOD = resolveMethod("isFuel");

    private static java.lang.reflect.Method resolveMethod(@Nonnull String name) {
        try {
            return Material.class.getMethod(name);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Boolean invokeBooleanMethod(@Nonnull Material material, java.lang.reflect.Method method) {
        if (method == null) {
            return null;
        }
        try {
            return (Boolean) method.invoke(material);
        } catch (Throwable ignored) {
            return null;
        }
    }

    // Bukkit#craftItem(ItemStack[], World, Player) is 1.18+; resolve reflectively so this class still
    // links on 1.8-1.17 servers where the method is absent (NoSuchMethodError otherwise).
    private static final java.lang.reflect.Method CRAFT_ITEM_METHOD = resolveCraftItemMethod();

    private static java.lang.reflect.Method resolveCraftItemMethod() {
        try {
            return Bukkit.class.getMethod("craftItem", ItemStack[].class, World.class, Player.class);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Version-safe replacement for {@code Bukkit#craftItem(ItemStack[], World, Player)} (added in
     * Minecraft 1.18), used to resolve vanilla crafting-grid recipes that Slimefun itself doesn't
     * know about. On servers where the method doesn't exist (pre-1.18) this returns {@code null}
     * instead of throwing, so callers should treat a {@code null} result as "no vanilla fallback
     * available here" rather than "not a valid recipe".
     */
    @Nullable
    public static ItemStack craftVanillaResult(@Nonnull ItemStack[] matrix, @Nonnull World world, @Nonnull Player player) {
        if (CRAFT_ITEM_METHOD == null) {
            return null;
        }
        try {
            return (ItemStack) CRAFT_ITEM_METHOD.invoke(null, matrix, world, player);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
