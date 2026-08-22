package io.github.sefiraat.networks.slimefun.network;

import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.MaterialCompat;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.StringUtils;
import io.github.sefiraat.networks.utils.Theme;
import io.github.sefiraat.networks.utils.datatypes.PersistentQuantumStorageType;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun5.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun5.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import org.bukkit.Material;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkQuantumStorage extends SlimefunItem implements DistinctiveItem {

    private static final int[] SIZES = new int[]{
        4096,
        32768,
        262144,
        2097152,
        16777216,
        134217728,
        1073741824,
        Integer.MAX_VALUE
    };

    public static final String BS_AMOUNT = "stored_amount";
    public static final String BS_VOID = "void_excess";

    public static final int INPUT_SLOT = 1;
    public static final int ITEM_SLOT = 4;
    public static final int ITEM_SET_SLOT = 13;
    public static final int OUTPUT_SLOT = 7;
    // Repurpose two background slots (part of BACKGROUND_SLOTS) as quick deposit / extract buttons.
    public static final int QUICK_DEPOSIT_SLOT = 16;
    public static final int QUICK_EXTRACT_SLOT = 17;

    private static final ItemStack QUICK_DEPOSIT = CustomItemStack.create(
        MaterialCompat.material(XMaterial.PINK_STAINED_GLASS_PANE),
        Theme.CLICK_INFO + "Quick Deposit",
        Theme.PASSIVE + "Click to deposit every matching item",
        Theme.PASSIVE + "from your inventory into storage."
    );

    private static final ItemStack QUICK_EXTRACT = CustomItemStack.create(
        MaterialCompat.material(XMaterial.RED_STAINED_GLASS_PANE),
        Theme.CLICK_INFO + "Quick Extract",
        Theme.PASSIVE + "Left click: fill your inventory",
        Theme.PASSIVE + "Right click: take out 1",
        Theme.PASSIVE + "Shift + right click: take out 64"
    );

    private static final ItemStack BACK_INPUT = CustomItemStack.create(
        MaterialCompat.material(XMaterial.GREEN_STAINED_GLASS_PANE),
        Theme.PASSIVE + "Input"
    );

    private static final ItemStack BACK_ITEM = CustomItemStack.create(
        MaterialCompat.material(XMaterial.BLUE_STAINED_GLASS_PANE),
        Theme.PASSIVE + "Item Stored"
    );

    private static final ItemStack NO_ITEM = CustomItemStack.create(
        MaterialCompat.material(XMaterial.RED_STAINED_GLASS_PANE),
        Theme.ERROR + "No Registered Item",
        Theme.PASSIVE + "Click the icon below while",
        Theme.PASSIVE + "holding an item to register it."
    );

    private static final ItemStack SET_ITEM = CustomItemStack.create(
        MaterialCompat.material(XMaterial.LIME_STAINED_GLASS_PANE),
        Theme.SUCCESS + "Set Item",
        Theme.PASSIVE + "Drag an item on top of this pane to register it.",
        Theme.PASSIVE + "Shift Click to change voiding"
    );

    private static final ItemStack BACK_OUTPUT = CustomItemStack.create(
        MaterialCompat.material(XMaterial.ORANGE_STAINED_GLASS_PANE),
        Theme.PASSIVE + "Output"
    );

    private static final int[] INPUT_SLOTS = new int[]{0, 2};
    private static final int[] ITEM_SLOTS = new int[]{3, 5};
    private static final int[] OUTPUT_SLOTS = new int[]{6, 8};
    private static final int[] BACKGROUND_SLOTS = new int[]{9, 10, 11, 12, 14, 15, 16, 17};

    private static final Map<Location, QuantumCache> CACHES = new HashMap<>();

    static {
        final ItemMeta itemMeta = NO_ITEM.getItemMeta();
        // Version-safe PDC marker (no-op pre-1.14); stored as a byte to mirror dough's boolean encoding.
        PdcCompat.set(itemMeta, Keys.newKey("display"), "BYTE", (byte) 1);
        NO_ITEM.setItemMeta(itemMeta);
    }

    private final List<Integer> slotsToDrop = new ArrayList<>();
    private final int maxAmount;

    public NetworkQuantumStorage(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int maxAmount) {
        super(itemGroup, item, recipeType, recipe);
        this.maxAmount = maxAmount;
        slotsToDrop.add(INPUT_SLOT);
        slotsToDrop.add(OUTPUT_SLOT);
    }

    @Override
    public void preRegister() {
        addItemHandler(
            new BlockTicker() {
                @Override
                public boolean isSynchronized() {
                    return false;
                }

                @Override
                public void tick(Block b, SlimefunItem item, Config data) {
                    onTick(b);
                }
            },
            new BlockBreakHandler(false, false) {
                @Override
                @ParametersAreNonnullByDefault
                public void onPlayerBreak(BlockBreakEvent event, ItemStack item, List<ItemStack> drops) {
                    onBreak(event);
                }
            },
            new BlockPlaceHandler(false) {
                @Override
                public void onPlayerPlace(@Nonnull BlockPlaceEvent event) {
                    onPlace(event);
                }
            }
        );
    }

    private void onTick(Block block) {
        final BlockMenu blockMenu = BlockStorage.getInventory(block);

        if (blockMenu == null) {
            CACHES.remove(block.getLocation());
            return;
        }

        final QuantumCache cache = CACHES.get(blockMenu.getLocation());

        if (cache == null) {
            return;
        }

        if (blockMenu.hasViewer()) {
            updateDisplayItem(blockMenu, cache);
        }

        final ItemStack input = blockMenu.getItemInSlot(INPUT_SLOT);
        if (input != null && input.getType() != Material.AIR) {
            tryInputItem(blockMenu.getLocation(), new ItemStack[]{input}, cache);
        }

        final ItemStack output = blockMenu.getItemInSlot(OUTPUT_SLOT);
        ItemStack fetched = null;
        if (output == null || output.getType() == Material.AIR) {
            fetched = cache.withdrawItem();
        } else if (StackUtils.itemsMatch(cache, output, true) && output.getAmount() < output.getMaxStackSize()) {
            final int requestAmount = output.getMaxStackSize() - output.getAmount();
            fetched = cache.withdrawItem(requestAmount);
        }

        if (fetched != null && fetched.getType() != Material.AIR) {
            blockMenu.pushItem(fetched, OUTPUT_SLOT);
            syncBlock(blockMenu.getLocation(), cache);
        }

        CACHES.put(blockMenu.getLocation().clone(), cache);
    }

    private void toggleVoid(@Nonnull BlockMenu blockMenu) {
        final QuantumCache cache = CACHES.get(blockMenu.getLocation());
        cache.setVoidExcess(!cache.isVoidExcess());
        updateDisplayItem(blockMenu, cache);
        syncBlock(blockMenu.getLocation(), cache);
        CACHES.put(blockMenu.getLocation(), cache);
    }

    private void setItem(@Nonnull BlockMenu blockMenu, @Nonnull Player player) {
        final ItemStack itemStack = player.getItemOnCursor().clone();

        if (isBlacklisted(itemStack)) {
            return;
        }

        final QuantumCache cache = CACHES.get(blockMenu.getLocation());
        if (cache == null || cache.getAmount() > 0) {
            player.sendMessage(Theme.WARNING + "Quantum Storage must be empty before changing the set item.");
            return;
        }
        itemStack.setAmount(1);
        cache.setItemStack(itemStack);
        updateDisplayItem(blockMenu, cache);
        syncBlock(blockMenu.getLocation(), cache);
        CACHES.put(blockMenu.getLocation(), cache);
    }

    /** Quick Deposit: pulls every matching item from the player's inventory into storage (up to capacity). */
    private void quickDeposit(@Nonnull BlockMenu blockMenu, @Nonnull Player player) {
        final QuantumCache cache = CACHES.get(blockMenu.getLocation());

        if (cache == null || cache.getItemStack() == null) {
            return;
        }

        final PlayerInventory inventory = player.getInventory();
        final ItemStack[] contents = inventory.getContents();

        for (int i = 0; i < contents.length; i++) {
            final ItemStack item = contents[i];

            if (item == null || item.getType() == Material.AIR || !StackUtils.itemsMatch(cache, item, true)) {
                continue;
            }

            final int space = cache.getLimit() - cache.getAmount();

            if (space <= 0) {
                break;
            }

            final int toAdd = Math.min(item.getAmount(), space);

            if (toAdd > 0) {
                cache.increaseAmount(toAdd);
                item.setAmount(item.getAmount() - toAdd);

                if (item.getAmount() <= 0) {
                    inventory.setItem(i, null);
                }
            }
        }

        updateDisplayItem(blockMenu, cache);
        syncBlock(blockMenu.getLocation(), cache);
        CACHES.put(blockMenu.getLocation(), cache);
    }

    /** Quick Extract: left click fills the inventory, right click takes 1, shift + right click takes 64. */
    private void quickExtract(@Nonnull BlockMenu blockMenu, @Nonnull Player player, @Nonnull ClickAction action) {
        final QuantumCache cache = CACHES.get(blockMenu.getLocation());

        if (cache == null || cache.getItemStack() == null || cache.getAmount() <= 0) {
            return;
        }

        if (action.isRightClicked()) {
            final ItemStack extracted = cache.withdrawItem(action.isShiftClicked() ? 64 : 1);

            if (extracted != null && extracted.getType() != Material.AIR) {
                giveOrDrop(player, extracted);
            }
        } else {
            final ItemStack stored = cache.getItemStack();
            final int maxStack = stored.getMaxStackSize();
            final PlayerInventory inventory = player.getInventory();
            final ItemStack[] contents = inventory.getStorageContents();

            for (int i = 0; i < contents.length && cache.getAmount() > 0; i++) {
                if (contents[i] == null || contents[i].getType() == Material.AIR) {
                    final int amount = Math.min(cache.getAmount(), maxStack);
                    final ItemStack give = stored.clone();
                    give.setAmount(amount);
                    contents[i] = give;
                    cache.reduceAmount(amount);
                }
            }

            inventory.setStorageContents(contents);
        }

        updateDisplayItem(blockMenu, cache);
        syncBlock(blockMenu.getLocation(), cache);
        CACHES.put(blockMenu.getLocation(), cache);
    }

    /** Adds the item to the player's inventory, dropping any overflow at their feet. */
    private void giveOrDrop(@Nonnull Player player, @Nonnull ItemStack item) {
        for (ItemStack rest : player.getInventory().addItem(item).values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), rest);
        }
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                optOutOfHeaderItem();
                for (int i : INPUT_SLOTS) {
                    addItem(i, BACK_INPUT, (p, slot, item, action) -> false);
                }
                for (int i : ITEM_SLOTS) {
                    addItem(i, BACK_ITEM, (p, slot, item, action) -> false);
                }
                for (int i : OUTPUT_SLOTS) {
                    addItem(i, BACK_OUTPUT, (p, slot, item, action) -> false);
                }
                addItem(ITEM_SET_SLOT, SET_ITEM, (p, slot, item, action) -> false);
                addMenuClickHandler(ITEM_SLOT, ChestMenuUtils.getEmptyClickHandler());
                drawBackground(BACKGROUND_SLOTS);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return new int[]{INPUT_SLOT};
                } else if (flow == ItemTransportFlow.WITHDRAW) {
                    return new int[]{OUTPUT_SLOT};
                }
                return new int[0];
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block block) {
                menu.addMenuClickHandler(ITEM_SET_SLOT, (p, slot, item, action) -> {
                    if (action.isShiftClicked()) {
                        toggleVoid(menu);
                    } else {
                        setItem(menu, p);
                    }
                    return false;
                });

                // Quick deposit / extract buttons (repurposed background slots).
                menu.replaceExistingItem(QUICK_DEPOSIT_SLOT, QUICK_DEPOSIT);
                menu.addMenuClickHandler(QUICK_DEPOSIT_SLOT, (p, slot, item, action) -> {
                    quickDeposit(menu, p);
                    return false;
                });
                menu.replaceExistingItem(QUICK_EXTRACT_SLOT, QUICK_EXTRACT);
                menu.addMenuClickHandler(QUICK_EXTRACT_SLOT, (p, slot, item, action) -> {
                    quickExtract(menu, p, action);
                    return false;
                });

                // Cache may exist if placed with items held inside.
                QuantumCache cache = CACHES.get(block.getLocation());
                if (cache == null) {
                    cache = addCache(menu);
                }
                updateDisplayItem(menu, cache);
            }
        };
    }

    private QuantumCache addCache(@Nonnull BlockMenu blockMenu) {
        final Location location = blockMenu.getLocation();
        final String amountString = BlockStorage.getLocationInfo(location, BS_AMOUNT);
        final String voidString = BlockStorage.getLocationInfo(location, BS_VOID);
        final int amount = amountString == null ? 0 : Integer.parseInt(amountString);
        final boolean voidExcess = voidString == null || Boolean.parseBoolean(voidString);
        final ItemStack itemStack = blockMenu.getItemInSlot(ITEM_SLOT);

        QuantumCache cache = createCache(itemStack, blockMenu, amount, voidExcess);

        CACHES.put(location, cache);
        return cache;
    }

    private QuantumCache createCache(@Nullable ItemStack itemStack, @Nonnull BlockMenu menu, int amount, boolean voidExcess) {
        if (itemStack == null || itemStack.getType() == Material.AIR || isDisplayItem(itemStack)) {
            menu.addItem(ITEM_SLOT, NO_ITEM);
            return new QuantumCache(null, 0, this.maxAmount, true);
        } else {
            final ItemStack clone = itemStack.clone();
            final ItemMeta itemMeta = clone.getItemMeta();
            final List<String> lore = itemMeta.getLore();
            for (int i = 0; i < 3; i++) {
                lore.remove(lore.size() - 1);
            }
            itemMeta.setLore(lore.isEmpty() ? null : lore);
            clone.setItemMeta(itemMeta);

            final QuantumCache cache = new QuantumCache(clone, amount, this.maxAmount, voidExcess);

            updateDisplayItem(menu, cache);
            return cache;
        }
    }

    private boolean isDisplayItem(@Nonnull ItemStack itemStack) {
        return PdcCompat.getByte(itemStack.getItemMeta(), Keys.newKey("display")) == (byte) 1;
    }

    protected void onBreak(@Nonnull BlockBreakEvent event) {
        final Location location = event.getBlock().getLocation();
        final BlockMenu blockMenu = BlockStorage.getInventory(event.getBlock());

        if (blockMenu != null) {
            final QuantumCache cache = CACHES.remove(blockMenu.getLocation());

            if (cache != null && cache.getAmount() > 0 && cache.getItemStack() != null) {
                final ItemStack itemToDrop = this.getItem().clone();
                final ItemMeta itemMeta = itemToDrop.getItemMeta();

                PersistentQuantumStorageType.store(itemMeta, cache);
                cache.addMetaLore(itemMeta);
                itemToDrop.setItemMeta(itemMeta);
                location.getWorld().dropItem(location.clone().add(0.5, 0.5, 0.5), itemToDrop);
                event.setDropItems(false);
            }

            for (int i : this.slotsToDrop) {
                blockMenu.dropItems(location, i);
            }
        }
    }

    protected void onPlace(@Nonnull BlockPlaceEvent event) {
        final ItemStack itemStack = event.getItemInHand();
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final QuantumCache cache = PersistentQuantumStorageType.read(itemMeta);

        if (cache == null) {
            return;
        }

        syncBlock(event.getBlock().getLocation(), cache);
        CACHES.put(event.getBlock().getLocation(), cache);
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    @ParametersAreNonnullByDefault
    public static void tryInputItem(Location location, ItemStack[] input, QuantumCache cache) {
        if (cache.getItemStack() == null) {
            return;
        }
        for (ItemStack itemStack : input) {
            if (isBlacklisted(itemStack)) {
                continue;
            }
            if (StackUtils.itemsMatch(cache, itemStack, true)) {
                int leftover = cache.increaseAmount(itemStack.getAmount());
                itemStack.setAmount(leftover);
            }
        }
        syncBlock(location, cache);
    }

    private static boolean isShulkerBox(@Nonnull Material material) {
        // Version-safe replacement for Tag.SHULKER_BOXES (the Tag system is 1.13+, shulker boxes 1.11+).
        return material.name().endsWith("SHULKER_BOX");
    }

    private static boolean isBlacklisted(@Nonnull ItemStack itemStack) {
        return itemStack.getType() == Material.AIR
            || itemStack.getType().getMaxDurability() < 0
            || isShulkerBox(itemStack.getType())
            || SlimefunItem.getByItem(itemStack) instanceof NetworkQuantumStorage;
    }

    @ParametersAreNonnullByDefault
    @Nullable
    public static ItemStack getItemStack(@Nonnull QuantumCache cache, @Nonnull BlockMenu blockMenu) {
        if (cache.getItemStack() == null || cache.getAmount() <= 0) {
            return null;
        }
        return getItemStack(cache, blockMenu, cache.getItemStack().getMaxStackSize());
    }

    @ParametersAreNonnullByDefault
    @Nullable
    public static ItemStack getItemStack(@Nonnull QuantumCache cache, @Nonnull BlockMenu blockMenu, int amount) {
        if (cache.getAmount() < amount) {
            // Storage alone can't fill the request, so combine it with the output slot
            ItemStack output = blockMenu.getItemInSlot(OUTPUT_SLOT);
            ItemStack fetched = cache.withdrawItem(amount);

            if (output != null
                && output.getType() != Material.AIR
                && StackUtils.itemsMatch(cache, output, true)
            ) {
                if (fetched == null || fetched.getType() == Material.AIR) {
                    fetched = output.clone();
                    if (fetched.getAmount() > amount) {
                        fetched.setAmount(amount);
                    }
                    output.setAmount(output.getAmount() - fetched.getAmount());
                } else {
                    int additional = Math.min(amount - fetched.getAmount(), output.getAmount());
                    output.setAmount(output.getAmount() - additional);
                    fetched.setAmount(fetched.getAmount() + additional);
                }
            }
            syncBlock(blockMenu.getLocation(), cache);
            return fetched;
        } else {
            syncBlock(blockMenu.getLocation(), cache);
            return cache.withdrawItem(amount);
        }
    }

    private static void updateDisplayItem(@Nonnull BlockMenu menu, @Nonnull QuantumCache cache) {
        if (cache.getItemStack() == null) {
            menu.replaceExistingItem(ITEM_SLOT, NO_ITEM);
        } else {
            final ItemStack itemStack = cache.getItemStack().clone();
            final ItemMeta itemMeta = itemStack.getItemMeta();
            final List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
            lore.add("");
            lore.add(Theme.CLICK_INFO + "Voiding: " + Theme.PASSIVE + StringUtils.toTitleCase(String.valueOf(cache.isVoidExcess())));
            lore.add(Theme.CLICK_INFO + "Amount: " + Theme.PASSIVE + cache.getAmount());
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            itemStack.setAmount(1);
            menu.replaceExistingItem(ITEM_SLOT, itemStack);
        }
    }

    private static void syncBlock(@Nonnull Location location, @Nonnull QuantumCache cache) {
        BlockStorage.addBlockInfo(location, BS_AMOUNT, String.valueOf(cache.getAmount()));
        BlockStorage.addBlockInfo(location, BS_VOID, String.valueOf(cache.isVoidExcess()));
    }

    public static Map<Location, QuantumCache> getCaches() {
        return CACHES;
    }

    public static int[] getSizes() {
        return SIZES;
    }

    @Override
    public boolean canStack(@Nonnull ItemMeta sfItemMeta, @Nonnull ItemMeta itemMeta) {
        if (!PdcCompat.isSupported()) {
            // No PersistentDataContainer pre-1.14; fall back to meta equality.
            return sfItemMeta.equals(itemMeta);
        }
        return containersEqual(sfItemMeta, itemMeta);
    }

    // Reflective PDC-container comparison (1.14+); avoids naming PersistentDataContainer in bytecode.
    private static boolean containersEqual(@Nonnull ItemMeta a, @Nonnull ItemMeta b) {
        try {
            Object ca = ItemMeta.class.getMethod("getPersistentDataContainer").invoke(a);
            Object cb = ItemMeta.class.getMethod("getPersistentDataContainer").invoke(b);
            return ca == null ? cb == null : ca.equals(cb);
        } catch (ReflectiveOperationException e) {
            return a.equals(b);
        }
    }
}








