package io.github.sefiraat.networks;

import io.github.sefiraat.networks.commands.NetworksMain;
import io.github.sefiraat.networks.managers.ListenerManager;
import io.github.sefiraat.networks.managers.SupportedPluginManager;


import io.github.sefiraat.networks.slimefun.NetworkSlimefunItems;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiText;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiTopic;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.dough.updater.BlobBuildUpdater;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

<<<<<<< HEAD
import org.bstats.charts.AdvancedPie;
=======
>>>>>>> origin/experimental
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import dev.walshy.sfmetrics.MetricsModule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Networks extends JavaPlugin implements SlimefunAddon {


    private static Networks instance;

    private final String username;
    private final String repo;
    private final String branch;

    private ListenerManager listenerManager;
    private SupportedPluginManager supportedPluginManager;

    public Networks() {
        this.username = "Slimefun5";
        this.repo = "Networks";
        this.branch = "master";
    }

    @Override
    public void onEnable() {
        MetricsModule.setup(this, 31391);

        instance = this;

        // Startup banner intentionally omitted: Slimefun core logs every installed addon uniformly.

        saveDefaultConfig();
        tryUpdate();

        this.supportedPluginManager = new SupportedPluginManager();

        setupSlimefun();

        this.listenerManager = new ListenerManager();
        this.getCommand("networks").setExecutor(new NetworksMain());

        setupMetrics();

        Slimefun.getItemTranslationService().registerTranslations(this);
        registerWiki();
    }

    private void registerWiki() {
        WikiText wiki = Slimefun.getWikiText();

        // Bucket this addon's items by their ItemGroup, preserving discovery order.
        Map<ItemGroup, List<String>> groupedItems = new LinkedHashMap<>();
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (item.getAddon() != this) {
                    continue;
                }
                ItemGroup group = item.getItemGroup();
                groupedItems.computeIfAbsent(group, key -> new ArrayList<>()).add(item.getId());

                List<String> page = describeItem(item.getId());
                if (page != null) {
                    wiki.set(item.getId(), page);
                }
            } catch (Exception | LinkageError ignored) {
                // Skip items that fail to resolve their group/addon on legacy versions.
            }
        }

        for (Map.Entry<ItemGroup, List<String>> entry : groupedItems.entrySet()) {
            ItemGroup group = entry.getKey();
            String groupKey = group.getKey().getKey();
            String topicId = "addon_networks_" + groupKey;

            wiki.registerTopic(new WikiTopic(
                topicId,
                topicDisplayName(groupKey),
                topicIcon(groupKey),
                topicTagline(groupKey)
            ));
            wiki.setMechanic(topicId, describeCategory(groupKey));
            wiki.setTopicItems(topicId, entry.getValue());
        }
    }

    @Nonnull
    private String topicDisplayName(@Nonnull String groupKey) {
        switch (groupKey) {
            case "network_items": return "Networks: Core & Nodes";
            case "network_quantums": return "Networks: Quantum Storage";
            case "tools": return "Networks: Tools & Remotes";
            case "materials": return "Networks: Crafting Materials";
            default: return "Networks";
        }
    }

    @Nonnull
    private XMaterial topicIcon(@Nonnull String groupKey) {
        switch (groupKey) {
            case "network_items": return XMaterial.BEACON;
            case "network_quantums": return XMaterial.WHITE_TERRACOTTA;
            case "tools": return XMaterial.NETHER_STAR;
            case "materials": return XMaterial.GLASS;
            default: return XMaterial.HOPPER;
        }
    }

    @Nonnull
    private String topicTagline(@Nonnull String groupKey) {
        switch (groupKey) {
            case "network_items": return "&7Controller, bridges, grids & nodes";
            case "network_quantums": return "&7Compact quantum item storage";
            case "tools": return "&7Remotes, probes & configurators";
            case "materials": return "&7Optic components & AI cores";
            default: return "&7Item storage & transport networks";
        }
    }

    @Nonnull
    private List<String> describeCategory(@Nonnull String groupKey) {
        switch (groupKey) {
            case "network_items":
                return Arrays.asList(
                    "&7The beating heart of every Networks setup.",
                    "",
                    "&7Place a single &bNetwork Controller&7 and wire it",
                    "&7to machines and storage using &bNetwork Bridges&7.",
                    "&7Bridges form the wiring: every node must trace a",
                    "&7path of bridges back to the Controller to function.",
                    "",
                    "&7&lNodes &7do the work:",
                    "&7 - &bImport/Grabber&7 pull items into the network",
                    "&7 - &bExport/Pusher&7 send items out to machines",
                    "&7 - &bMonitor, Grid & Cell&7 view and hold contents",
                    "&7 - &bPurger&7 voids unwanted items",
                    "",
                    "&7Nodes draw &epower&7 from network Capacitors,",
                    "&7fed through a Power Outlet from any energy net.",
                    "",
                    "&7Click an item below for its recipe & details."
                );
            case "network_quantums":
                return Arrays.asList(
                    "&7Quantum Storage is mass storage for a single",
                    "&7item type, holding far more than a chest ever could.",
                    "",
                    "&7Craft cells in the &bQuantum Workbench&7. Each tier",
                    "&7(1-8) raises the maximum stored amount dramatically.",
                    "",
                    "&7Place a cell, drop in an item to set its type, then",
                    "&7feed it with an &bExport/Pusher&7 and draw from it",
                    "&7with an &bImport/Grabber&7 like any other container.",
                    "",
                    "&7Higher tiers store millions of a single item,",
                    "&7making them ideal sinks for cobblestone, ingots",
                    "&7or mob-farm drops.",
                    "",
                    "&7Click an item below for its recipe & details."
                );
            case "tools":
                return Arrays.asList(
                    "&7Handheld tools for building and managing networks.",
                    "",
                    "&7 - &bNetwork Remote&7 opens your network's grid from",
                    "&7   a distance; higher tiers reach further.",
                    "&7 - &bNetwork Probe&7 inspects a node's status & links.",
                    "&7 - &bConfigurator / Wireless Configurator&7 copy",
                    "&7   node settings between blocks.",
                    "&7 - &bNetwork Crayon&7 colours nodes for organisation.",
                    "&7 - &bNetwork Rake&7 cleans up items; tiers raise its",
                    "&7   working range.",
                    "&7 - &bCrafting Blueprint&7 stores a recipe for the",
                    "&7   auto-crafter.",
                    "",
                    "&7Click an item below for its recipe & details."
                );
            case "materials":
                return Arrays.asList(
                    "&7The crafting backbone of the Networks addon.",
                    "",
                    "&7Everything starts with &bOptic&7 components, built",
                    "&7from Synthetic Emerald Shards into Optic Glass,",
                    "&7Optic Cable and the Optic Star.",
                    "",
                    "&7These feed into &bAI Cores&7 and &bNanobots&7, the",
                    "&7advanced parts used by wireless gear, auto-crafters",
                    "&7and high-tier remotes.",
                    "",
                    "&7Most components are crafted in the &bEnhanced",
                    "&7Crafting Table&7. They are ingredients only and",
                    "&7cannot be placed in the world.",
                    "",
                    "&7Click an item below for its recipe & details."
                );
            default:
                return Arrays.asList(
                    "&7Item storage & transport networks.",
                    "",
                    "&7Click an item below for its recipe & details."
                );
        }
    }

    @Nullable
    private List<String> describeItem(@Nonnull String itemId) {
        switch (itemId) {
            // --- Core network blocks ---
            case "NTW_CONTROLLER":
                return Arrays.asList(
                    "&7The root of every network. Place exactly one.",
                    "&7All bridges and nodes must connect back to it.",
                    "&7Open it to see the network's stored items & status."
                );
            case "NTW_BRIDGE":
                return Arrays.asList(
                    "&7The wiring of a network.",
                    "&7Connects the Controller to nodes and to other",
                    "&7bridges, extending the network's reach."
                );
            case "NTW_MONITOR":
                return Arrays.asList(
                    "&7Displays a live readout of the network it is",
                    "&7attached to, useful for debugging connections."
                );
            case "NTW_IMPORT":
                return Arrays.asList(
                    "&7Pulls items out of the container it faces and",
                    "&7into the network's storage."
                );
            case "NTW_EXPORT":
                return Arrays.asList(
                    "&7Pushes items from the network into the container",
                    "&7it faces. Filterable to specific items."
                );
            case "NTW_GRABBER":
                return Arrays.asList(
                    "&7An advanced importer that grabs items from a",
                    "&7container into the network."
                );
            case "NTW_PUSHER":
                return Arrays.asList(
                    "&7An advanced exporter that pushes items from the",
                    "&7network into a container."
                );
            case "NTW_VANILLA_GRABBER":
                return Arrays.asList(
                    "&7Grabs items from vanilla inventories such as",
                    "&7chests and hoppers into the network."
                );
            case "NTW_VANILLA_PUSHER":
                return Arrays.asList(
                    "&7Pushes items from the network into vanilla",
                    "&7inventories such as chests and hoppers."
                );
            case "NTW_VACUUM":
                return Arrays.asList(
                    "&7Sucks up nearby dropped items from the world",
                    "&7and stores them in the network."
                );
            case "NTW_PURGER":
                return Arrays.asList(
                    "&7Permanently voids items fed into it.",
                    "&7Use it to dispose of unwanted overflow."
                );
            case "NTW_GRID":
                return Arrays.asList(
                    "&7A searchable window into the network's contents.",
                    "&7Browse and withdraw any stored item."
                );
            case "NTW_CRAFTING_GRID":
                return Arrays.asList(
                    "&7A crafting grid backed by network storage.",
                    "&7Craft using items held anywhere in the network."
                );
            case "NTW_CELL":
                return Arrays.asList(
                    "&7Provides general item storage capacity to the",
                    "&7network, like a network-attached chest."
                );
            case "NTW_GREEDY_BLOCK":
                return Arrays.asList(
                    "&7A storage block that greedily keeps the items",
                    "&7placed into it within the network."
                );
            case "NTW_CONTROL_X":
                return Arrays.asList(
                    "&7A combined node bundling a grabber with power",
                    "&7handling for compact builds."
                );
            case "NTW_CONTROL_V":
                return Arrays.asList(
                    "&7A combined node bundling a pusher with power",
                    "&7handling for compact builds."
                );
            case "NTW_RECIPE_ENCODER":
                return Arrays.asList(
                    "&7Encodes a crafting recipe onto a Blueprint for",
                    "&7use by the Network Auto-Crafter."
                );
            case "NTW_AUTO_CRAFTER":
                return Arrays.asList(
                    "&7Automatically crafts a Blueprint's recipe using",
                    "&7ingredients pulled from the network."
                );
            case "NTW_AUTO_CRAFTER_WITHHOLDING":
                return Arrays.asList(
                    "&7An auto-crafter that withholds a buffer of",
                    "&7ingredients, keeping the network stocked."
                );
            // --- Wireless ---
            case "NTW_NETWORK_WIRELESS_TRANSMITTER":
                return Arrays.asList(
                    "&7Beams items out of the network to a paired",
                    "&7Wireless Receiver, no bridges required."
                );
            case "NTW_NETWORK_WIRELESS_RECEIVER":
                return Arrays.asList(
                    "&7Receives items beamed from a paired Wireless",
                    "&7Transmitter into the network."
                );
            // --- Power ---
            case "NTW_CAPACITOR_1":
            case "NTW_CAPACITOR_2":
            case "NTW_CAPACITOR_3":
            case "NTW_CAPACITOR_4":
                return Arrays.asList(
                    "&7Stores energy for the network's nodes.",
                    "&7Higher tiers hold exponentially more power."
                );
            case "NTW_POWER_OUTLET_1":
            case "NTW_POWER_OUTLET_2":
                return Arrays.asList(
                    "&7Feeds energy from a Slimefun energy network",
                    "&7into the Networks power system."
                );
            case "NTW_POWER_DISPLAY":
                return Arrays.asList(
                    "&7Shows the current stored power of the network",
                    "&7it is connected to."
                );
            // --- Quantum storage ---
            case "NTW_QUANTUM_WORKBENCH":
                return Arrays.asList(
                    "&7The crafting station for Quantum Storage cells.",
                    "&7Lay out the pattern and craft each tier here."
                );
            case "NTW_QUANTUM_STORAGE_1":
            case "NTW_QUANTUM_STORAGE_2":
            case "NTW_QUANTUM_STORAGE_3":
            case "NTW_QUANTUM_STORAGE_4":
            case "NTW_QUANTUM_STORAGE_5":
            case "NTW_QUANTUM_STORAGE_6":
            case "NTW_QUANTUM_STORAGE_7":
            case "NTW_QUANTUM_STORAGE_8":
                return Arrays.asList(
                    "&7Mass storage for a single item type.",
                    "&7Drop an item in to set its type, then fill it",
                    "&7via the network. Higher tiers store far more."
                );
            // --- Tools ---
            case "NTW_REMOTE":
            case "NTW_REMOTE_EMPOWERED":
            case "NTW_REMOTE_PRISTINE":
            case "NTW_REMOTE_ULTIMATE":
                return Arrays.asList(
                    "&7Opens a linked network's grid remotely.",
                    "&7Higher tiers work over greater distances."
                );
            case "NTW_PROBE":
                return Arrays.asList(
                    "&7Right-click a node to inspect its status and",
                    "&7its connection to the network."
                );
            case "NTW_CONFIGURATOR":
                return Arrays.asList(
                    "&7Copies a node's configuration so it can be",
                    "&7applied to another node."
                );
            case "NTW_WIRELESS_CONFIGURATOR":
                return Arrays.asList(
                    "&7Configures wireless transmitters and receivers,",
                    "&7pairing them together."
                );
            case "NTW_CRAYON":
                return Arrays.asList(
                    "&7Colours network nodes, helping you organise",
                    "&7large or overlapping setups visually."
                );
            case "NTW_RAKE_1":
            case "NTW_RAKE_2":
            case "NTW_RAKE_3":
                return Arrays.asList(
                    "&7Cleans up loose items in an area.",
                    "&7Higher tiers cover a larger working range."
                );
            case "NTW_CRAFTING_BLUEPRINT":
                return Arrays.asList(
                    "&7Stores a crafting recipe encoded by the Recipe",
                    "&7Encoder for use in the Auto-Crafter."
                );
            // --- Key materials ---
            case "NTW_OPTIC_GLASS":
                return Arrays.asList(
                    "&7A glass that carries small amounts of data.",
                    "&7A core ingredient across most network items."
                );
            case "NTW_OPTIC_CABLE":
                return Arrays.asList(
                    "&7A wire that carries large amounts of data.",
                    "&7Used as connective material in many recipes."
                );
            case "NTW_OPTIC_STAR":
                return Arrays.asList(
                    "&7A crystalline star carrying large data loads.",
                    "&7Used in advanced network components."
                );
            case "NTW_AI_CORE":
            case "NTW_EMPOWERED_AI_CORE":
            case "NTW_PRISTINE_AI_CORE":
                return Arrays.asList(
                    "&7An artificial intelligence core.",
                    "&7Higher tiers power smarter, longer-range gear."
                );
            default:
                return null;
        }
    }

    public void tryUpdate() {
        if (getConfig().getBoolean("auto-update") && getDescription().getVersion().startsWith("Dev")) {
            new BlobBuildUpdater(this, getFile(), "Networks", "Dev").start();
        }
    }

    public void setupSlimefun() {
        NetworkSlimefunItems.setup();
        if (supportedPluginManager.isNetheopoiesis()) {
            try {
                // NetheoPlants.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().severe("Netheopoiesis must be updated to meet Networks' requirements.");
            }
        }
        if (supportedPluginManager.isSlimeHud()) {
            try {
                // HudCallbacks.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().severe("SlimeHUD must be updated to meet Networks' requirements.");
            }
        }
    }

    public void setupMetrics() {
<<<<<<< HEAD
        final 
        AdvancedPie networksChart = new AdvancedPie("networks", () -> {
            Map<String, Integer> networksMap = new HashMap<>();
            networksMap.put("Number of networks", NetworkController.getNetworks().size());
            return networksMap;
        });

        metrics.addCustomChart(networksChart);
=======
        // bStats removed for Java 8 / legacy compatibility
>>>>>>> origin/experimental
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return MessageFormat.format("https://github.com/{0}/{1}/issues/", this.username, this.repo);
    }

    @Nonnull
    public static PluginManager getPluginManager() {
        return Networks.getInstance().getServer().getPluginManager();
    }

    public static Networks getInstance() {
        return Networks.instance;
    }

    public static SupportedPluginManager getSupportedPluginManager() {
        return Networks.getInstance().supportedPluginManager;
    }

    public static ListenerManager getListenerManager() {
        return Networks.getInstance().listenerManager;
    }
}




