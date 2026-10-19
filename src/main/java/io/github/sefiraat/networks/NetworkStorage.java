package io.github.sefiraat.networks;

import io.github.sefiraat.networks.network.NetworkNode;
import io.github.sefiraat.networks.network.NodeDefinition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @implNote Controllers tick asynchronously while breaks and sweeps run on the main thread, so the map is concurrent.
 */
public class NetworkStorage {

    private static final Map<Location, NodeDefinition> ALL_NETWORK_OBJECTS = new ConcurrentHashMap<>();

    public static void removeNode(Location location) {
        final NodeDefinition nodeDefinition = ALL_NETWORK_OBJECTS.remove(location);

        if (nodeDefinition == null) {
            return;
        }

        final NetworkNode node = nodeDefinition.getNode();

        if (node == null) {
            return;
        }

        for (NetworkNode childNode : nodeDefinition.getNode().getChildrenNodes()) {
            removeNode(childNode.getNodePosition());
        }
    }

    public static Map<Location, NodeDefinition> getAllNetworkObjects() {
        return ALL_NETWORK_OBJECTS;
    }

    public static void forgetWorld(World world) {
        ALL_NETWORK_OBJECTS.keySet().removeIf(location -> worldOf(location) == world);
    }

    /**
     * Drops nodes in worlds the server no longer has, which a block break never reaches: an unloaded world,
     * or a world that was never registered with the server, such as the one Slimefun's checks place machines in.
     */
    public static void forgetUnloadedWorlds() {
        ALL_NETWORK_OBJECTS.keySet().removeIf(location -> !isLoaded(worldOf(location)));
    }

    private static boolean isLoaded(World world) {
        return world != null && Bukkit.getWorld(world.getUID()) == world;
    }

    /**
     * @implNote Paper's {@code Location#getWorld} throws once its world is unloaded instead of returning null.
     */
    private static World worldOf(Location location) {
        try {
            return location.getWorld();
        } catch (IllegalArgumentException unloaded) {
            return null;
        }
    }
}
