package io.github.sefiraat.networks.slimefun.network;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A higher-throughput {@link NetworkPusher}: it exposes twelve template slots instead of two, so a single
 * node can push up to twelve distinct matching item templates into the target machine per tick. The push
 * behaviour itself is inherited unchanged - only the menu layout (background + template slots) differs.
 * <p>
 * Ported from Networks-Exp (b3), where it is called the "Network Advanced Pusher".
 */
public class NetworkBestPusher extends NetworkPusher {

    private static final int[] BACKGROUND_SLOTS = new int[]{
        0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 13, 18, 20, 22, 23, 27, 28, 30, 31, 36, 37, 38, 39, 40, 41
    };
    private static final int[] TEMPLATE_BACKGROUND = new int[]{7};
    private static final int[] TEMPLATE_SLOTS = new int[]{15, 16, 17, 24, 25, 26, 33, 34, 35, 42, 43, 44};

    public NetworkBestPusher(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    @Nullable
    @Override
    protected int[] getOtherBackgroundSlots() {
        return TEMPLATE_BACKGROUND;
    }

    @Override
    public int[] getItemSlots() {
        return TEMPLATE_SLOTS;
    }
}
