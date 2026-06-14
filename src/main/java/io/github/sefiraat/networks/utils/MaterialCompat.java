package io.github.sefiraat.networks.utils;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

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

    /**
     * Resolves an {@link XMaterial} to a non-null {@link Material} for the running version.
     */
    @Nonnull
    public static Material material(@Nonnull XMaterial xMaterial) {
        Material material = xMaterial.parseMaterial();
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
}
