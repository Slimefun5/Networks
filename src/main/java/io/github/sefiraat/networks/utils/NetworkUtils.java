package io.github.sefiraat.networks.utils;

import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.network.NetworkNode;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.sefiraat.networks.slimefun.network.NetworkPusher;
import io.github.sefiraat.networks.slimefun.tools.NetworkConfigurator;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public class NetworkUtils {

    @Nonnull
    private static String itemLabel(@Nonnull Player player, int slot) {
        return Messages.get(player, "configurator.item-label", "Item [%slot%]: ").replace("%slot%", String.valueOf(slot));
    }

    public static void applyConfig(@Nonnull NetworkDirectional directional, @Nonnull BlockMenu blockMenu, @Nonnull Player player) {
        ItemStack itemStack = HandCompat.offHandItem(player.getInventory());

        if (SlimefunItem.getByItem(itemStack) instanceof NetworkConfigurator) {
            applyConfig(directional, itemStack, blockMenu, player);
        }
    }

    public static void applyConfig(@Nonnull NetworkDirectional directional, @Nonnull ItemStack itemStack, @Nonnull BlockMenu blockMenu, @Nonnull Player player) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final ItemStack[] templateStacks = DataTypeMethods.getItemStackArray(itemMeta, Keys.ITEM.toString());
        final String string = DataTypeMethods.getString(itemMeta, Keys.FACE.toString());

        if (string == null) {
            player.sendMessage(Theme.ERROR + Messages.get(player, "configurator.direction-label", "Direction: ")
                + Theme.PASSIVE + Messages.get(player, "configurator.direction-not-supplied", "Not supplied"));
            return;
        }

        directional.setDirection(blockMenu, BlockFace.valueOf(string));
        player.sendMessage(Theme.ERROR + Messages.get(player, "configurator.direction-label", "Direction: ")
            + Theme.PASSIVE + Messages.get(player, "configurator.direction-applied", "Successfully applied"));


        if (directional.getItemSlots().length > 0) {
            for (int slot : directional.getItemSlots()) {
                final ItemStack stackToDrop = blockMenu.getItemInSlot(slot);
                if (stackToDrop != null && stackToDrop.getType() != Material.AIR) {
                    blockMenu.getLocation().getWorld().dropItem(blockMenu.getLocation(), stackToDrop.clone());
                    stackToDrop.setAmount(0);
                }
            }
        }

        if (templateStacks != null) {
            int i = 0;
            for (ItemStack templateStack : templateStacks) {
                if (templateStack != null && templateStack.getType() != Material.AIR) {
                    boolean worked = false;
                    for (ItemStack stack : player.getInventory()) {
                        if (StackUtils.itemsMatch(stack, templateStack)) {
                            final ItemStack stackClone = StackUtils.getAsQuantity(stack, 1);
                            stack.setAmount(stack.getAmount() - 1);
                            blockMenu.replaceExistingItem(directional.getItemSlots()[i], stackClone);
                            player.sendMessage(Theme.SUCCESS + itemLabel(player, i)
                                + Theme.PASSIVE + Messages.get(player, "configurator.item-added", "Item added into filter"));
                            worked = true;
                            break;
                        }
                    }
                    if (!worked) {
                        player.sendMessage(Theme.WARNING + itemLabel(player, i)
                            + Theme.PASSIVE + Messages.get(player, "configurator.not-enough-items", "Not enough items to fill filter"));
                    }
                } else if (directional instanceof NetworkPusher) {
                    player.sendMessage(Theme.WARNING + itemLabel(player, i)
                        + Theme.PASSIVE + Messages.get(player, "configurator.no-item-stored", "No item in stored config"));
                }
                i++;
            }
        } else {
            player.sendMessage(Theme.WARNING + Messages.get(player, "configurator.items-label", "Items: ")
                + Theme.PASSIVE + Messages.get(player, "configurator.no-items-stored", "No items in stored config"));
        }
    }

    public static void clearNetwork(Location location) {
        NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(location);

        if (definition == null || definition.getNode() == null) {
            return;
        }

        NetworkNode node = definition.getNode();

        if (node != null && node.getNodeType() == NodeType.CONTROLLER) {
            NetworkController.wipeNetwork(location);
        }

        NetworkStorage.removeNode(location);
    }
}

