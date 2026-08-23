package io.github.sefiraat.networks.network.stackcaches;

import io.github.sefiraat.networks.utils.MaterialCompat;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlueprintInstance extends ItemStackCache {

    private final ItemStack[] recipeItems;
    @Nullable
    private Recipe recipe = null;

    public BlueprintInstance(@Nonnull ItemStack[] recipeItems, @Nonnull ItemStack expectedOutput) {
        super(expectedOutput);
        this.recipeItems = recipeItems;
    }

    public ItemStack[] getRecipeItems() {
        return recipeItems;
    }

    @Nullable
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(@Nullable Recipe recipe) {
        this.recipe = recipe;
    }

    /**
     * Resolves the vanilla recipe this blueprint encodes, so the auto crafter can execute it.
     *
     * @implNote Goes through {@link MaterialCompat#craftingRecipe(ItemStack[], World)} because
     *           {@code Bukkit#getCraftingRecipe} is 1.18+. Stubbing this out left {@link #getRecipe()}
     *           permanently null, which made the auto crafter reject every vanilla blueprint even
     *           though the encoder (which has its own reflective fallback) had happily written one.
     */
    public void generateVanillaRecipe(World world) {
        if (this.recipe == null) {
            this.recipe = MaterialCompat.craftingRecipe(this.recipeItems, world);
        }
    }
}
