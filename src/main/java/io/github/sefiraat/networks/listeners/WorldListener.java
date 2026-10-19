package io.github.sefiraat.networks.listeners;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

import io.github.sefiraat.networks.NetworkStorage;

public class WorldListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(@Nonnull WorldUnloadEvent event) {
        NetworkStorage.forgetWorld(event.getWorld());
    }
}
