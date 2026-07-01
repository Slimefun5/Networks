package io.github.sefiraat.networks.utils;

import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/** Reflective helpers for the 1.9+ off-hand API (getHand/getItemInOffHand/EquipmentSlot), so handlers run on 1.8. */
public final class HandCompat {

    private HandCompat() {}

    /** True if the interaction used the main hand. Always true before 1.9 (no off-hand exists). */
    public static boolean isMainHand(Object event) {
        try {
            Method getHand = event.getClass().getMethod("getHand");
            Object slot = getHand.invoke(event);
            return slot == null || !"OFF_HAND".equals(slot.toString());
        } catch (ReflectiveOperationException e) {
            return true;
        }
    }

    /** The off-hand item, or AIR on versions that have no off-hand. */
    public static ItemStack offHandItem(PlayerInventory inv) {
        try {
            Method m = inv.getClass().getMethod("getItemInOffHand");
            return (ItemStack) m.invoke(inv);
        } catch (ReflectiveOperationException e) {
            return new ItemStack(Material.AIR);
        }
    }
}
