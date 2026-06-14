package io.github.sefiraat.networks.utils.datatypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * Version-safe (1.8+) serialization helpers. Everything here serializes to plain {@link String}s so the
 * data can be persisted through the String-keyed {@code PersistentDataAPI} without ever referencing the
 * 1.14+ {@code org.bukkit.persistence.*} types in bytecode.
 */
public final class SerializationUtils {

    private SerializationUtils() {}

    @Nullable
    public static String itemStackToString(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(byteStream)) {
            objectStream.writeObject(itemStack);
            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (Exception exception) {
            return null;
        }
    }

    @Nullable
    public static ItemStack itemStackFromString(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.getDecoder().decode(value));
             BukkitObjectInputStream objectStream = new BukkitObjectInputStream(byteStream)) {
            return (ItemStack) objectStream.readObject();
        } catch (Exception exception) {
            return null;
        }
    }

    @Nullable
    public static String itemStackArrayToString(@Nullable ItemStack[] itemStacks) {
        if (itemStacks == null) {
            return null;
        }
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(byteStream)) {
            objectStream.writeInt(itemStacks.length);
            for (ItemStack itemStack : itemStacks) {
                objectStream.writeObject(itemStack);
            }
            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (Exception exception) {
            return null;
        }
    }

    @Nullable
    public static ItemStack[] itemStackArrayFromString(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.getDecoder().decode(value));
             BukkitObjectInputStream objectStream = new BukkitObjectInputStream(byteStream)) {
            final int length = objectStream.readInt();
            final ItemStack[] itemStacks = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                itemStacks[i] = (ItemStack) objectStream.readObject();
            }
            return itemStacks;
        } catch (Exception exception) {
            return null;
        }
    }

    @Nonnull
    public static String locationToString(@Nonnull Location location) {
        return location.getWorld().getName()
            + ';' + location.getX()
            + ';' + location.getY()
            + ';' + location.getZ()
            + ';' + location.getYaw()
            + ';' + location.getPitch();
    }

    @Nullable
    public static Location locationFromString(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        final String[] parts = value.split(";");
        if (parts.length < 4) {
            return null;
        }
        final World world = Bukkit.getWorld(parts[0]);
        if (world == null) {
            return null;
        }
        try {
            final double x = Double.parseDouble(parts[1]);
            final double y = Double.parseDouble(parts[2]);
            final double z = Double.parseDouble(parts[3]);
            final float yaw = parts.length > 4 ? Float.parseFloat(parts[4]) : 0f;
            final float pitch = parts.length > 5 ? Float.parseFloat(parts[5]) : 0f;
            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
