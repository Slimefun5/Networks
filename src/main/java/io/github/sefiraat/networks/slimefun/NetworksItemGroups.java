package io.github.sefiraat.networks.slimefun;

import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.slimefun.groups.DummyItemGroup;
import io.github.sefiraat.networks.slimefun.groups.MainFlexGroup;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.sefiraat.networks.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class NetworksItemGroups {

    public static final MainFlexGroup MAIN = new MainFlexGroup(
        new NamespacedKey(Networks.getInstance(), "main"),
        CustomItemStack.create(
            MaterialCompat.stack(XMaterial.BLACK_STAINED_GLASS),
            Theme.MAIN.getColor() + "Networks"
        )
    );

    public static final DummyItemGroup MATERIALS = new DummyItemGroup(
        new NamespacedKey(Networks.getInstance(), "materials"),
        CustomItemStack.create(
            MaterialCompat.stack(XMaterial.WHITE_STAINED_GLASS),
            Theme.MAIN.getColor() + "Crafting Materials"
        )
    );

    public static final DummyItemGroup TOOLS = new DummyItemGroup(
        new NamespacedKey(Networks.getInstance(), "tools"),
        CustomItemStack.create(
            MaterialCompat.stack(XMaterial.PAINTING),
            Theme.MAIN.getColor() + "Network Management Tools"
        )
    );

    public static final DummyItemGroup NETWORK_ITEMS = new DummyItemGroup(
        new NamespacedKey(Networks.getInstance(), "network_items"),
        CustomItemStack.create(
            MaterialCompat.stack(XMaterial.BLACK_STAINED_GLASS),
            Theme.MAIN.getColor() + "Network Items"
        )
    );

    public static final DummyItemGroup NETWORK_QUANTUMS = new DummyItemGroup(
        new NamespacedKey(Networks.getInstance(), "network_quantums"),
        CustomItemStack.create(
            MaterialCompat.stack(XMaterial.WHITE_TERRACOTTA),
            Theme.MAIN.getColor() + "Network Quantum Storage Devices"
        )
    );

    public static final ItemGroup DISABLED_ITEMS = new HiddenItemGroup(
        new NamespacedKey(Networks.getInstance(), "disabled_items"),
        CustomItemStack.create(
            MaterialCompat.stack(XMaterial.BARRIER),
            Theme.MAIN.getColor() + "Disabled/Removed Items"
        )
    );

    static {
        final Networks plugin = Networks.getInstance();

        NetworksItemGroups.MAIN.setTheme("logistics");
        NetworksItemGroups.MATERIALS.setTheme("resources");
        NetworksItemGroups.TOOLS.setTheme("tools");
        NetworksItemGroups.NETWORK_ITEMS.setTheme("logistics");
        NetworksItemGroups.NETWORK_QUANTUMS.setTheme("logistics");
        NetworksItemGroups.DISABLED_ITEMS.setTheme("misc");

        NetworksItemGroups.MAIN.register(plugin);
        NetworksItemGroups.MATERIALS.register(plugin);
        NetworksItemGroups.TOOLS.register(plugin);
        NetworksItemGroups.NETWORK_ITEMS.register(plugin);
        NetworksItemGroups.NETWORK_QUANTUMS.register(plugin);
        NetworksItemGroups.DISABLED_ITEMS.register(plugin);
    }

    public static class HiddenItemGroup extends ItemGroup {

        public HiddenItemGroup(NamespacedKey key, ItemStack item) {
            super(key, item);
        }

        @Override
        public boolean isHidden(@Nonnull Player p) {
            return true;
        }
    }
}
