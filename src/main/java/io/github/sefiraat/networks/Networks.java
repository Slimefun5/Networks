package io.github.sefiraat.networks;

import io.github.sefiraat.networks.commands.NetworksMain;
import io.github.sefiraat.networks.managers.ListenerManager;
import io.github.sefiraat.networks.managers.SupportedPluginManager;


import io.github.sefiraat.networks.slimefun.NetworkSlimefunItems;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.dough.updater.BlobBuildUpdater;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class Networks extends JavaPlugin implements SlimefunAddon {


    private static Networks instance;

    private final String username;
    private final String repo;
    private final String branch;

    private ListenerManager listenerManager;
    private SupportedPluginManager supportedPluginManager;

    public Networks() {
        this.username = "Sefiraat";
        this.repo = "Networks";
        this.branch = "master";
    }

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("########################################");
        getLogger().info("         Networks - By Sefiraat         ");
        getLogger().info("########################################");

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
        io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiText wiki = io.github.thebusybiscuit.slimefun5.implementation.Slimefun.getWikiText();
        String topicId = "addon_networks";
        wiki.registerTopic(new io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiTopic(topicId, "Networks", io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial.HOPPER, "&7Item storage & transport networks"));
        wiki.setMechanic(topicId, java.util.Arrays.asList(
            "&7Item storage & transport networks.", "",
            "&7An alternative to cargo: quantum storage", "&7cells, crafting grids and import/export", "&7nodes wired to a central Controller.", "",
            "&7Click an item below for its recipe."));
        java.util.List<String> items = new java.util.ArrayList<>();
        for (io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem item : io.github.thebusybiscuit.slimefun5.implementation.Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try { if (item.getAddon() == this) { items.add(item.getId()); } } catch (Exception | LinkageError ignored) { }
        }
        wiki.setTopicItems(topicId, items);
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
        // bStats removed for Java 8 / legacy compatibility
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




