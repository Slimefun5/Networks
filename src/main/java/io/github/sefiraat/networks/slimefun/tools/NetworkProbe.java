package io.github.sefiraat.networks.slimefun.tools;

import io.github.sefiraat.networks.utils.Messages;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun5.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class NetworkProbe extends SlimefunItem implements CanCooldown {

    private static final MessageFormat MESSAGE_FORMAT = new MessageFormat("{0}{1}: {2}{3}", Locale.ROOT);
    private static final String DIVIDER = "------------------------------";

    public NetworkProbe(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

    }

    @Override
    public void preRegister() {
        addItemHandler((ItemUseHandler) this::onUse);
    }

    protected void onUse(PlayerRightClickEvent e) {
        final Optional<Block> optional = e.getClickedBlock();
        if (optional.isPresent()) {
            final Block block = optional.get();
            final Player player = e.getPlayer();
            if (canBeUsed(player, e.getItem())) {
                final SlimefunItem slimefunItem = BlockStorage.check(block);
                if (slimefunItem instanceof NetworkController) {
                    e.cancel();
                    displayToPlayer(block, player);
                    putOnCooldown(e.getItem());
                }
            }
        }
    }

    private static void row(@Nonnull Player player, ChatColor labelColor, ChatColor valueColor, @Nonnull String key, @Nonnull String english, @Nonnull Object value) {
        String label = Messages.get(player, "probe." + key, english);
        player.sendMessage(MESSAGE_FORMAT.format(new Object[] { labelColor, label, valueColor, value }, new StringBuffer(), null).toString());
    }

    private void displayToPlayer(@Nonnull Block block, @Nonnull Player player) {
        final NetworkRoot root = NetworkController.getNetworks().get(block.getLocation());
        if (root != null) {
            final int bridges = root.getBridges().size();
            final int monitors = root.getMonitors().size();
            final int importers = root.getImporters().size();
            final int exporters = root.getExporters().size();
            final int grids = root.getGrids().size();
            final int cells = root.getCells().size();
            final int wipers = root.getWipers().size();
            final int grabbers = root.getGrabbers().size();
            final int pushers = root.getPushers().size();
            final int cutters = root.getCutters().size();
            final int pasters = root.getPasters().size();
            final int vacuums = root.getVacuums().size();
            final int purgers = root.getPurgers().size();
            final int crafters = root.getCrafters().size();
            final int powerNodes = root.getPowerNodes().size();
            final int powerOutlets = root.getPowerOutlets().size();
            final int powerDisplays = root.getPowerDisplays().size();
            final int encoders = root.getEncoders().size();
            final int greedyBlocks = root.getGreedyBlockLocations().size();
            final int wirelessTransmitters = root.getWirelessTransmitters().size();
            final int wirelessReceivers = root.getWirelessReceivers().size();
            final long rootPower = root.getRootPower();

            final Map<ItemStack, Integer> allNetworkItems = root.getAllNetworkItems();
            final int distinctItems = allNetworkItems.size();
            long totalItems = allNetworkItems.values().stream().mapToLong(integer -> integer).sum();

            final String nodeCount = root.getNodeCount() >= root.getMaxNodes()
                ? Theme.ERROR + "" + root.getNodeCount() + "+"
                : String.valueOf(root.getNodeCount());

            final ChatColor c = Theme.CLICK_INFO.getColor();
            final ChatColor p = Theme.PASSIVE.getColor();

            player.sendMessage(DIVIDER);
            player.sendMessage(Messages.get(player, "probe.summary-title", "       Network Summary        "));
            player.sendMessage(DIVIDER);

            row(player, c, p, "bridges", "Bridges", bridges);
            row(player, c, p, "monitors", "Monitors", monitors);
            row(player, c, p, "importers", "Importers", importers);
            row(player, c, p, "exporters", "Exporters", exporters);
            row(player, c, p, "grids", "Grids", grids);
            row(player, c, p, "cells", "Cells", cells);
            row(player, c, p, "wipers", "Wipers", wipers);
            row(player, c, p, "grabbers", "Grabbers", grabbers);
            row(player, c, p, "pushers", "Pushers", pushers);
            row(player, c, p, "cutters", "Cutters", cutters);
            row(player, c, p, "pasters", "Pasters", pasters);
            row(player, c, p, "vacuums", "Vacuums", vacuums);
            row(player, c, p, "purgers", "Purgers", purgers);
            row(player, c, p, "crafters", "Crafters", crafters);
            row(player, c, p, "power-nodes", "Power Nodes", powerNodes);
            row(player, c, p, "power-outlets", "Power Outlets", powerOutlets);
            row(player, c, p, "power-displays", "Power Displays", powerDisplays);
            row(player, c, p, "encoders", "Encoders", encoders);
            row(player, c, p, "greedy-blocks", "Greedy Blocks", greedyBlocks);
            row(player, c, p, "wireless-transmitters", "Wireless Transmitters", wirelessTransmitters);
            row(player, c, p, "wireless-receivers", "Wireless Receivers", wirelessReceivers);

            player.sendMessage(DIVIDER);
            row(player, c, p, "distinct-items", "Distinct Items", distinctItems);
            row(player, c, p, "total-items", "Total Items", totalItems);

            player.sendMessage(DIVIDER);
            row(player, c, p, "root-power", "Root Power", rootPower);

            player.sendMessage(DIVIDER);
            row(player, c, p, "total-nodes", "Total Nodes", nodeCount + "/" + root.getMaxNodes());
            if (root.isOverburdened()) {
                player.sendMessage(Theme.ERROR + Messages.get(player, "probe.warning-label", "Warning: ") + Theme.PASSIVE
                    + Messages.get(player, "probe.overburdened",
                        "Your network has reached or exceeded the maximum node limit. "
                            + "Nodes beyond the limit will not function, which nodes these are "
                            + "may not always be the same. Reduce your total nodes."));
            }
        }
    }

    @Override
    public int cooldownDuration() {
        return 10;
    }
}

