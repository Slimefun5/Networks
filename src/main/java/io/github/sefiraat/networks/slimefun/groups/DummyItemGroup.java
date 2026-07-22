package io.github.sefiraat.networks.slimefun.groups;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class DummyItemGroup extends ItemGroup {

    private final boolean hidden;

    @ParametersAreNonnullByDefault
    public DummyItemGroup(NamespacedKey key, ItemStack item) {
        this(key, item, true);
    }

    @ParametersAreNonnullByDefault
    public DummyItemGroup(NamespacedKey key, ItemStack item, boolean hidden) {
        super(key, item);
        this.hidden = hidden;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isHidden(Player p) {
        return hidden;
    }

}

