package io.github.sefiraat.networks.slimefun;

import io.github.sefiraat.networks.utils.Theme;
import io.github.sefiraat.networks.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

/**
 * Creating SlimefunItemstacks here due to some items being created in Enums so this will
 * act as a one-stop-shop for the stacks themselves.
 */
public class NetworksSlimefunItemStacks {

    // Materials
    public static final SlimefunItemStack SYNTHETIC_EMERALD_SHARD;
    public static final SlimefunItemStack OPTIC_GLASS;
    public static final SlimefunItemStack OPTIC_CABLE;
    public static final SlimefunItemStack OPTIC_STAR;
    public static final SlimefunItemStack RADIOACTIVE_OPTIC_STAR;
    public static final SlimefunItemStack SHRINKING_BASE;
    public static final SlimefunItemStack SIMPLE_NANOBOTS;
    public static final SlimefunItemStack ADVANCED_NANOBOTS;
    public static final SlimefunItemStack AI_CORE;
    public static final SlimefunItemStack EMPOWERED_AI_CORE;
    public static final SlimefunItemStack PRISTINE_AI_CORE;
    public static final SlimefunItemStack INTERDIMENSIONAL_PRESENCE;

    // Network Items
    public static final SlimefunItemStack NETWORK_CONTROLLER;
    public static final SlimefunItemStack NETWORK_BRIDGE;
    public static final SlimefunItemStack NETWORK_MONITOR;
    public static final SlimefunItemStack NETWORK_IMPORT;
    public static final SlimefunItemStack NETWORK_EXPORT;
    public static final SlimefunItemStack NETWORK_GRABBER;
    public static final SlimefunItemStack NETWORK_PUSHER;
    public static final SlimefunItemStack NETWORK_CONTROL_X;
    public static final SlimefunItemStack NETWORK_CONTROL_V;
    public static final SlimefunItemStack NETWORK_VACUUM;
    public static final SlimefunItemStack NETWORK_VANILLA_GRABBER;
    public static final SlimefunItemStack NETWORK_VANILLA_PUSHER;
    public static final SlimefunItemStack NETWORK_WIRELESS_TRANSMITTER;
    public static final SlimefunItemStack NETWORK_WIRELESS_RECEIVER;
    public static final SlimefunItemStack NETWORK_PURGER;
    public static final SlimefunItemStack NETWORK_GRID;
    public static final SlimefunItemStack NETWORK_CRAFTING_GRID;
    public static final SlimefunItemStack NETWORK_CELL;
    public static final SlimefunItemStack NETWORK_GREEDY_BLOCK;
    public static final SlimefunItemStack NETWORK_QUANTUM_WORKBENCH;
    public static final SlimefunItemStack NETWORK_QUANTUM_STORAGE_1;
    public static final SlimefunItemStack NETWORK_QUANTUM_STORAGE_2;
    public static final SlimefunItemStack NETWORK_QUANTUM_STORAGE_3;
    public static final SlimefunItemStack NETWORK_QUANTUM_STORAGE_4;
    public static final SlimefunItemStack NETWORK_QUANTUM_STORAGE_5;
    public static final SlimefunItemStack NETWORK_QUANTUM_STORAGE_6;
    public static final SlimefunItemStack NETWORK_QUANTUM_STORAGE_7;
    public static final SlimefunItemStack NETWORK_QUANTUM_STORAGE_8;
    public static final SlimefunItemStack NETWORK_CAPACITOR_1;
    public static final SlimefunItemStack NETWORK_CAPACITOR_2;
    public static final SlimefunItemStack NETWORK_CAPACITOR_3;
    public static final SlimefunItemStack NETWORK_CAPACITOR_4;
    public static final SlimefunItemStack NETWORK_POWER_OUTLET_1;
    public static final SlimefunItemStack NETWORK_POWER_OUTLET_2;
    public static final SlimefunItemStack NETWORK_POWER_DISPLAY;
    public static final SlimefunItemStack NETWORK_RECIPE_ENCODER;
    public static final SlimefunItemStack NETWORK_AUTO_CRAFTER;
    public static final SlimefunItemStack NETWORK_AUTO_CRAFTER_WITHHOLDING;

    // Tools
    public static final SlimefunItemStack CRAFTING_BLUEPRINT;
    public static final SlimefunItemStack NETWORK_PROBE;
    public static final SlimefunItemStack NETWORK_REMOTE;
    public static final SlimefunItemStack NETWORK_REMOTE_EMPOWERED;
    public static final SlimefunItemStack NETWORK_REMOTE_PRISTINE;
    public static final SlimefunItemStack NETWORK_REMOTE_ULTIMATE;
    public static final SlimefunItemStack NETWORK_CRAYON;
    public static final SlimefunItemStack NETWORK_CONFIGURATOR;
    public static final SlimefunItemStack NETWORK_WIRELESS_CONFIGURATOR;
    public static final SlimefunItemStack NETWORK_RAKE_1;
    public static final SlimefunItemStack NETWORK_RAKE_2;
    public static final SlimefunItemStack NETWORK_RAKE_3;
    public static final SlimefunItemStack NETWORK_DEBUG_STICK;

    static {

        SYNTHETIC_EMERALD_SHARD = Theme.themedSlimefunItemStack(
            "NTW_SYNTHETIC_EMERALD_SHARD",
            MaterialCompat.stack(XMaterial.LIME_DYE)
        );

        OPTIC_GLASS = Theme.themedSlimefunItemStack(
            "NTW_OPTIC_GLASS",
            MaterialCompat.stack(XMaterial.GLASS)
        );

        OPTIC_CABLE = Theme.themedSlimefunItemStack(
            "NTW_OPTIC_CABLE",
            MaterialCompat.stack(XMaterial.STRING)
        );

        OPTIC_STAR = Theme.themedSlimefunItemStack(
            "NTW_OPTIC_STAR",
            MaterialCompat.stack(XMaterial.NETHER_STAR)
        );

        RADIOACTIVE_OPTIC_STAR = Theme.themedSlimefunItemStack(
            "NTW_RADIOACTIVE_OPTIC_STAR",
            getPreEnchantedItemStack(MaterialCompat.material(XMaterial.NETHER_STAR), true, new Pair<>(Enchantment.ARROW_DAMAGE, 1))
        );

        SHRINKING_BASE = Theme.themedSlimefunItemStack(
            "NTW_SHRINKING_BASE",
            getPreEnchantedItemStack(MaterialCompat.material(XMaterial.PISTON), true, new Pair<>(Enchantment.ARROW_DAMAGE, 1))
        );

        SIMPLE_NANOBOTS = Theme.themedSlimefunItemStack(
            "NTW_SIMPLE_NANOBOTS",
            MaterialCompat.stack(XMaterial.MELON_SEEDS)
        );

        ADVANCED_NANOBOTS = Theme.themedSlimefunItemStack(
            "NTW_ADVANCED_NANOBOTS",
            getPreEnchantedItemStack(MaterialCompat.material(XMaterial.MELON_SEEDS), true, new Pair<>(Enchantment.ARROW_DAMAGE, 1))
        );

        AI_CORE = Theme.themedSlimefunItemStack(
            "NTW_AI_CORE",
            MaterialCompat.stack(XMaterial.BRAIN_CORAL_BLOCK)
        );

        EMPOWERED_AI_CORE = Theme.themedSlimefunItemStack(
            "NTW_EMPOWERED_AI_CORE",
            MaterialCompat.stack(XMaterial.TUBE_CORAL_BLOCK)
        );

        PRISTINE_AI_CORE = Theme.themedSlimefunItemStack(
            "NTW_PRISTINE_AI_CORE",
            getPreEnchantedItemStack(MaterialCompat.material(XMaterial.TUBE_CORAL_BLOCK), true, new Pair<>(Enchantment.ARROW_DAMAGE, 1))
        );

        INTERDIMENSIONAL_PRESENCE = Theme.themedSlimefunItemStack(
            "NTW_INTERDIMENSIONAL_PRESENCE",
            getPreEnchantedItemStack(MaterialCompat.material(XMaterial.ARMOR_STAND), true, new Pair<>(Enchantment.ARROW_DAMAGE, 1))
        );

        NETWORK_CONTROLLER = Theme.themedSlimefunItemStack(
            "NTW_CONTROLLER",
            MaterialCompat.stack(XMaterial.BLACK_STAINED_GLASS)
        );

        NETWORK_BRIDGE = Theme.themedSlimefunItemStack(
            "NTW_BRIDGE",
            MaterialCompat.stack(XMaterial.WHITE_STAINED_GLASS)
        );

        NETWORK_MONITOR = Theme.themedSlimefunItemStack(
            "NTW_MONITOR",
            MaterialCompat.stack(XMaterial.GREEN_STAINED_GLASS)
        );

        NETWORK_IMPORT = Theme.themedSlimefunItemStack(
            "NTW_IMPORT",
            MaterialCompat.stack(XMaterial.RED_STAINED_GLASS)
        );

        NETWORK_EXPORT = Theme.themedSlimefunItemStack(
            "NTW_EXPORT",
            MaterialCompat.stack(XMaterial.BLUE_STAINED_GLASS)
        );

        NETWORK_GRABBER = Theme.themedSlimefunItemStack(
            "NTW_GRABBER",
            MaterialCompat.stack(XMaterial.MAGENTA_STAINED_GLASS)
        );

        NETWORK_PUSHER = Theme.themedSlimefunItemStack(
            "NTW_PUSHER",
            MaterialCompat.stack(XMaterial.BROWN_STAINED_GLASS)
        );

        NETWORK_CONTROL_X = Theme.themedSlimefunItemStack(
            "NTW_CONTROL_X",
            MaterialCompat.stack(XMaterial.WHITE_GLAZED_TERRACOTTA)
        );

        NETWORK_CONTROL_V = Theme.themedSlimefunItemStack(
            "NTW_CONTROL_V",
            MaterialCompat.stack(XMaterial.PURPLE_GLAZED_TERRACOTTA)
        );

        NETWORK_VACUUM = Theme.themedSlimefunItemStack(
            "NTW_VACUUM",
            MaterialCompat.stack(XMaterial.ORANGE_GLAZED_TERRACOTTA)
        );

        NETWORK_VANILLA_GRABBER = Theme.themedSlimefunItemStack(
            "NTW_VANILLA_GRABBER",
            MaterialCompat.stack(XMaterial.ORANGE_STAINED_GLASS)
        );

        NETWORK_VANILLA_PUSHER = Theme.themedSlimefunItemStack(
            "NTW_VANILLA_PUSHER",
            MaterialCompat.stack(XMaterial.LIME_STAINED_GLASS)
        );

        NETWORK_WIRELESS_TRANSMITTER = Theme.themedSlimefunItemStack(
            "NTW_NETWORK_WIRELESS_TRANSMITTER",
            MaterialCompat.stack(XMaterial.CYAN_STAINED_GLASS)
        );

        NETWORK_WIRELESS_RECEIVER = Theme.themedSlimefunItemStack(
            "NTW_NETWORK_WIRELESS_RECEIVER",
            MaterialCompat.stack(XMaterial.PURPLE_STAINED_GLASS)
        );

        NETWORK_PURGER = Theme.themedSlimefunItemStack(
            "NTW_TRASH",
            MaterialCompat.stack(XMaterial.OBSERVER)
        );

        NETWORK_GRID = Theme.themedSlimefunItemStack(
            "NTW_GRID",
            MaterialCompat.stack(XMaterial.NOTE_BLOCK)
        );

        NETWORK_CRAFTING_GRID = Theme.themedSlimefunItemStack(
            "NTW_CRAFTING_GRID",
            MaterialCompat.stack(XMaterial.REDSTONE_LAMP)
        );

        NETWORK_CELL = Theme.themedSlimefunItemStack(
            "NTW_CELL",
            MaterialCompat.stack(XMaterial.HONEYCOMB_BLOCK)
        );

        NETWORK_GREEDY_BLOCK = Theme.themedSlimefunItemStack(
            "NTW_GREEDY_BLOCK",
            MaterialCompat.stack(XMaterial.SHROOMLIGHT)
        );

        NETWORK_QUANTUM_WORKBENCH = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_WORKBENCH",
            MaterialCompat.stack(XMaterial.DRIED_KELP_BLOCK)
        );


        NETWORK_QUANTUM_STORAGE_1 = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_STORAGE_1",
            MaterialCompat.stack(XMaterial.WHITE_TERRACOTTA)
        );

        NETWORK_QUANTUM_STORAGE_2 = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_STORAGE_2",
            MaterialCompat.stack(XMaterial.LIGHT_GRAY_TERRACOTTA)
        );

        NETWORK_QUANTUM_STORAGE_3 = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_STORAGE_3",
            MaterialCompat.stack(XMaterial.GRAY_TERRACOTTA)
        );

        NETWORK_QUANTUM_STORAGE_4 = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_STORAGE_4",
            MaterialCompat.stack(XMaterial.BROWN_TERRACOTTA)
        );

        NETWORK_QUANTUM_STORAGE_5 = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_STORAGE_5",
            MaterialCompat.stack(XMaterial.BLACK_TERRACOTTA)
        );

        NETWORK_QUANTUM_STORAGE_6 = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_STORAGE_6",
            MaterialCompat.stack(XMaterial.PURPLE_TERRACOTTA)
        );

        NETWORK_QUANTUM_STORAGE_7 = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_STORAGE_7",
            MaterialCompat.stack(XMaterial.MAGENTA_TERRACOTTA)
        );

        NETWORK_QUANTUM_STORAGE_8 = Theme.themedSlimefunItemStack(
            "NTW_QUANTUM_STORAGE_8",
            MaterialCompat.stack(XMaterial.RED_TERRACOTTA)
        );

        NETWORK_CAPACITOR_1 = Theme.themedSlimefunItemStack(
            "NTW_CAPACITOR_1",
            MaterialCompat.stack(XMaterial.BROWN_GLAZED_TERRACOTTA)
        );

        NETWORK_CAPACITOR_2 = Theme.themedSlimefunItemStack(
            "NTW_CAPACITOR_2",
            MaterialCompat.stack(XMaterial.GREEN_GLAZED_TERRACOTTA)
        );

        NETWORK_CAPACITOR_3 = Theme.themedSlimefunItemStack(
            "NTW_CAPACITOR_3",
            MaterialCompat.stack(XMaterial.BLACK_GLAZED_TERRACOTTA)
        );

        NETWORK_CAPACITOR_4 = Theme.themedSlimefunItemStack(
            "NTW_CAPACITOR_4",
            MaterialCompat.stack(XMaterial.GRAY_GLAZED_TERRACOTTA)
        );

        NETWORK_POWER_OUTLET_1 = Theme.themedSlimefunItemStack(
            "NTW_POWER_OUTLET_1",
            MaterialCompat.stack(XMaterial.YELLOW_GLAZED_TERRACOTTA)
        );

        NETWORK_POWER_OUTLET_2 = Theme.themedSlimefunItemStack(
            "NTW_POWER_OUTLET_2",
            MaterialCompat.stack(XMaterial.RED_GLAZED_TERRACOTTA)
        );

        NETWORK_POWER_DISPLAY = Theme.themedSlimefunItemStack(
            "NTW_POWER_DISPLAY",
            MaterialCompat.stack(XMaterial.GLASS)
        );

        NETWORK_RECIPE_ENCODER = Theme.themedSlimefunItemStack(
            "NTW_RECIPE_ENCODER",
            MaterialCompat.stack(XMaterial.TARGET)
        );

        NETWORK_AUTO_CRAFTER = Theme.themedSlimefunItemStack(
            "NTW_AUTO_CRAFTER",
            MaterialCompat.stack(XMaterial.BLACK_GLAZED_TERRACOTTA)
        );

        NETWORK_AUTO_CRAFTER_WITHHOLDING = Theme.themedSlimefunItemStack(
            "NTW_AUTO_CRAFTER_WITHHOLDING",
            MaterialCompat.stack(XMaterial.WHITE_GLAZED_TERRACOTTA)
        );

        CRAFTING_BLUEPRINT = Theme.themedSlimefunItemStack(
            "NTW_CRAFTING_BLUEPRINT",
            MaterialCompat.stack(XMaterial.BLUE_DYE)
        );

        NETWORK_PROBE = Theme.themedSlimefunItemStack(
            "NTW_PROBE",
            MaterialCompat.stack(XMaterial.CLOCK)
        );

        NETWORK_REMOTE = Theme.themedSlimefunItemStack(
            "NTW_REMOTE",
            MaterialCompat.stack(XMaterial.PAINTING)
        );

        NETWORK_REMOTE_EMPOWERED = Theme.themedSlimefunItemStack(
            "NTW_REMOTE_EMPOWERED",
            MaterialCompat.stack(XMaterial.ITEM_FRAME)
        );

        NETWORK_REMOTE_PRISTINE = Theme.themedSlimefunItemStack(
            "NTW_REMOTE_PRISTINE",
            MaterialCompat.stack(XMaterial.ITEM_FRAME)
        );

        NETWORK_REMOTE_ULTIMATE = Theme.themedSlimefunItemStack(
            "NTW_REMOTE_ULTIMATE",
            getPreEnchantedItemStack(MaterialCompat.material(XMaterial.ITEM_FRAME), true, new Pair<>(Enchantment.ARROW_DAMAGE, 1))
        );

        NETWORK_CRAYON = Theme.themedSlimefunItemStack(
            "NTW_CRAYON",
            MaterialCompat.stack(XMaterial.REDSTONE)
        );

        NETWORK_CONFIGURATOR = Theme.themedSlimefunItemStack(
            "NTW_CONFIGURATOR",
            MaterialCompat.stack(XMaterial.BLAZE_ROD)
        );

        NETWORK_WIRELESS_CONFIGURATOR = Theme.themedSlimefunItemStack(
            "NTW_WIRELESS_CONFIGURATOR",
            MaterialCompat.stack(XMaterial.BLAZE_ROD)
        );

        NETWORK_RAKE_1 = Theme.themedSlimefunItemStack(
            "NTW_RAKE_1",
            MaterialCompat.stack(XMaterial.TWISTING_VINES)
        );

        NETWORK_RAKE_2 = Theme.themedSlimefunItemStack(
            "NTW_RAKE_2",
            MaterialCompat.stack(XMaterial.WEEPING_VINES)
        );

        NETWORK_RAKE_3 = Theme.themedSlimefunItemStack(
            "NTW_RAKE_3",
            getPreEnchantedItemStack(MaterialCompat.material(XMaterial.WEEPING_VINES), true, new Pair<>(Enchantment.LUCK, 1))
        );

        NETWORK_DEBUG_STICK = Theme.themedSlimefunItemStack(
            "NTW_DEBUG_STICK",
            getPreEnchantedItemStack(MaterialCompat.material(XMaterial.STICK), true, new Pair<>(Enchantment.LUCK, 1))
        );
    }

    @Nonnull
    @SafeVarargs
    public static ItemStack getPreEnchantedItemStack(Material material, boolean hide, @Nonnull Pair<Enchantment, Integer>... enchantments) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        for (Pair<Enchantment, Integer> pair : enchantments) {
            itemMeta.addEnchant(pair.getFirstValue(), pair.getSecondValue(), true);
        }
        if (hide) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}





