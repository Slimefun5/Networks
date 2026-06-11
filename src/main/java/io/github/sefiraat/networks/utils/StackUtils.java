package io.github.sefiraat.networks.utils;

import io.github.sefiraat.networks.network.stackcaches.ItemStackCache;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.dough.data.persistent.PersistentDataAPI;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

public class StackUtils {
    private StackUtils() {}

    @Nonnull
    public static ItemStack getAsQuantity(@Nonnull ItemStack itemStack, int amount) {
        ItemStack clone = itemStack.clone();
        clone.setAmount(amount);
        return clone;
    }

    public static boolean itemsMatch(@Nullable ItemStack itemStack1, @Nullable ItemStack itemStack2) {
        return itemsMatch(new ItemStackCache(itemStack1), itemStack2, true);
    }

    /**
     * Checks if items match each other, checks go in order from lightest to heaviest
     *
     * @param cache     The cached {@link ItemStack} to compare against
     * @param itemStack The {@link ItemStack} being evaluated
     * @return True if items match
     */
    public static boolean itemsMatch(@Nonnull ItemStackCache cache, @Nullable ItemStack itemStack, boolean checkLore) {
        if (cache.getItemStack() == null || itemStack == null) {
            return itemStack == null && cache.getItemStack() == null;
        }

        if (itemStack.getType() != cache.getItemType()) {
            return false;
        }

        if (!itemStack.hasItemMeta() || !cache.getItemStack().hasItemMeta()) {
            return itemStack.hasItemMeta() == cache.getItemStack().hasItemMeta();
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        final ItemMeta cachedMeta = cache.getItemMeta();

        if (!itemMeta.getClass().equals(cachedMeta.getClass())) {
            return false;
        }

        if (canQuickEscapeMetaVariant(itemMeta, cachedMeta)) {
            return false;
        }

        if (itemMeta.hasDisplayName() != cachedMeta.hasDisplayName()) {
            return false;
        }

        final boolean hasCustomOne = itemMeta.hasCustomModelData();
        final boolean hasCustomTwo = cachedMeta.hasCustomModelData();
        if (hasCustomOne) {
            if (!hasCustomTwo || itemMeta.getCustomModelData() != cachedMeta.getCustomModelData()) {
                return false;
            }
        } else if (hasCustomTwo) {
            return false;
        }

        if (!itemMeta.getPersistentDataContainer().equals(cachedMeta.getPersistentDataContainer())) {
            return false;
        }

        if (!itemMeta.getEnchants().equals(cachedMeta.getEnchants())) {
            return false;
        }

        if (!itemMeta.getItemFlags().equals(cachedMeta.getItemFlags())) {
            return false;
        }

        if (checkLore && !Objects.equals(itemMeta.getLore(), cachedMeta.getLore())) {
            return false;
        }

        final Optional<String> optionalStackId1 = Slimefun.getItemDataService().getItemData(itemMeta);
        final Optional<String> optionalStackId2 = Slimefun.getItemDataService().getItemData(cachedMeta);
        if (optionalStackId1.isPresent() && optionalStackId2.isPresent()) {
            return optionalStackId1.get().equals(optionalStackId2.get());
        }

        if (itemMeta.hasDisplayName() && (!itemMeta.getDisplayName().equals(cachedMeta.getDisplayName()))) {
            return false;
        }

        return true;
    }


    public static boolean canQuickEscapeMetaVariant(@Nonnull ItemMeta metaOne, @Nonnull ItemMeta metaTwo) {

        if (metaOne instanceof Damageable && metaTwo instanceof Damageable) {
            Damageable instanceOne = (Damageable) metaOne;
            Damageable instanceTwo = (Damageable) metaTwo;
            if (instanceOne.getDamage() != instanceTwo.getDamage()) {
                return true;
            }
        }

        if (metaOne instanceof BannerMeta && metaTwo instanceof BannerMeta) {
            BannerMeta instanceOne = (BannerMeta) metaOne;
            BannerMeta instanceTwo = (BannerMeta) metaTwo;
            if (!instanceOne.getPatterns().equals(instanceTwo.getPatterns())) {
                return true;
            }
        }

        if (metaOne instanceof BookMeta && metaTwo instanceof BookMeta) {
            BookMeta instanceOne = (BookMeta) metaOne;
            BookMeta instanceTwo = (BookMeta) metaTwo;
            if (instanceOne.getPageCount() != instanceTwo.getPageCount()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getAuthor(), instanceTwo.getAuthor())) {
                return true;
            }
            if (!Objects.equals(instanceOne.getTitle(), instanceTwo.getTitle())) {
                return true;
            }
            if (!Objects.equals(instanceOne.getGeneration(), instanceTwo.getGeneration())) {
                return true;
            }
        }

        if (metaOne instanceof CompassMeta && metaTwo instanceof CompassMeta) {
            CompassMeta instanceOne = (CompassMeta) metaOne;
            CompassMeta instanceTwo = (CompassMeta) metaTwo;
            if (instanceOne.isLodestoneTracked() != instanceTwo.isLodestoneTracked()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getLodestone(), instanceTwo.getLodestone())) {
                return true;
            }
        }

        if (metaOne instanceof CrossbowMeta && metaTwo instanceof CrossbowMeta) {
            CrossbowMeta instanceOne = (CrossbowMeta) metaOne;
            CrossbowMeta instanceTwo = (CrossbowMeta) metaTwo;
            if (instanceOne.hasChargedProjectiles() != instanceTwo.hasChargedProjectiles()) {
                return true;
            }
            if (!instanceOne.getChargedProjectiles().equals(instanceTwo.getChargedProjectiles())) {
                return true;
            }
        }

        if (metaOne instanceof EnchantmentStorageMeta && metaTwo instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta instanceOne = (EnchantmentStorageMeta) metaOne;
            EnchantmentStorageMeta instanceTwo = (EnchantmentStorageMeta) metaTwo;
            if (instanceOne.hasStoredEnchants() != instanceTwo.hasStoredEnchants()) {
                return true;
            }
            if (!instanceOne.getStoredEnchants().equals(instanceTwo.getStoredEnchants())) {
                return true;
            }
        }

        if (metaOne instanceof FireworkEffectMeta && metaTwo instanceof FireworkEffectMeta) {
            FireworkEffectMeta instanceOne = (FireworkEffectMeta) metaOne;
            FireworkEffectMeta instanceTwo = (FireworkEffectMeta) metaTwo;
            if (!Objects.equals(instanceOne.getEffect(), instanceTwo.getEffect())) {
                return true;
            }
        }

        if (metaOne instanceof FireworkMeta && metaTwo instanceof FireworkMeta) {
            FireworkMeta instanceOne = (FireworkMeta) metaOne;
            FireworkMeta instanceTwo = (FireworkMeta) metaTwo;
            if (instanceOne.getPower() != instanceTwo.getPower()) {
                return true;
            }
            if (!instanceOne.getEffects().equals(instanceTwo.getEffects())) {
                return true;
            }
        }

        if (metaOne instanceof LeatherArmorMeta && metaTwo instanceof LeatherArmorMeta) {
            LeatherArmorMeta instanceOne = (LeatherArmorMeta) metaOne;
            LeatherArmorMeta instanceTwo = (LeatherArmorMeta) metaTwo;
            if (!instanceOne.getColor().equals(instanceTwo.getColor())) {
                return true;
            }
        }

        if (metaOne instanceof MapMeta && metaTwo instanceof MapMeta) {
            MapMeta instanceOne = (MapMeta) metaOne;
            MapMeta instanceTwo = (MapMeta) metaTwo;
            if (instanceOne.hasMapView() != instanceTwo.hasMapView()) {
                return true;
            }
            if (instanceOne.hasLocationName() != instanceTwo.hasLocationName()) {
                return true;
            }
            if (instanceOne.hasColor() != instanceTwo.hasColor()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getMapView(), instanceTwo.getMapView())) {
                return true;
            }
            if (!Objects.equals(instanceOne.getLocationName(), instanceTwo.getLocationName())) {
                return true;
            }
            if (!Objects.equals(instanceOne.getColor(), instanceTwo.getColor())) {
                return true;
            }
        }

        if (metaOne instanceof PotionMeta && metaTwo instanceof PotionMeta) {
            PotionMeta instanceOne = (PotionMeta) metaOne;
            PotionMeta instanceTwo = (PotionMeta) metaTwo;
            if (!instanceOne.getBasePotionData().equals(instanceTwo.getBasePotionData())) {
                return true;
            }
            if (instanceOne.hasCustomEffects() != instanceTwo.hasCustomEffects()) {
                return true;
            }
            if (instanceOne.hasColor() != instanceTwo.hasColor()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getColor(), instanceTwo.getColor())) {
                return true;
            }
            if (!instanceOne.getCustomEffects().equals(instanceTwo.getCustomEffects())) {
                return true;
            }
        }

        if (metaOne instanceof SkullMeta && metaTwo instanceof SkullMeta) {
            SkullMeta instanceOne = (SkullMeta) metaOne;
            SkullMeta instanceTwo = (SkullMeta) metaTwo;
            if (instanceOne.hasOwner() != instanceTwo.hasOwner()) {
                return true;
            }
            if (!Objects.equals(instanceOne.getOwningPlayer(), instanceTwo.getOwningPlayer())) {
                return true;
            }
        }

        if (metaOne instanceof TropicalFishBucketMeta && metaTwo instanceof TropicalFishBucketMeta) {
            TropicalFishBucketMeta instanceOne = (TropicalFishBucketMeta) metaOne;
            TropicalFishBucketMeta instanceTwo = (TropicalFishBucketMeta) metaTwo;
            if (instanceOne.hasVariant() != instanceTwo.hasVariant()) {
                return true;
            }
            if (!instanceOne.getPattern().equals(instanceTwo.getPattern())) {
                return true;
            }
            if (!instanceOne.getBodyColor().equals(instanceTwo.getBodyColor())) {
                return true;
            }
            if (!instanceOne.getPatternColor().equals(instanceTwo.getPatternColor())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param itemStack         The item to put on cooldown
     * @param durationInSeconds How long the cooldown lasts
     */
    @ParametersAreNonnullByDefault
    public static void putOnCooldown(ItemStack itemStack, int durationInSeconds) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            PersistentDataAPI.setLong(itemMeta, Keys.ON_COOLDOWN, System.currentTimeMillis() + (durationInSeconds * 1000L));
            itemStack.setItemMeta(itemMeta);
        }
    }

    /** @param itemStack The item to check */
    @ParametersAreNonnullByDefault
    public static boolean isOnCooldown(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            long cooldownUntil = PersistentDataAPI.getLong(itemMeta, Keys.ON_COOLDOWN, 0);
            return System.currentTimeMillis() < cooldownUntil;
        }
        return false;
    }
}
