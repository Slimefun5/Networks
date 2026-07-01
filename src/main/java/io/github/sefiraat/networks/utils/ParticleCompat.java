package io.github.sefiraat.networks.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Version-safe particle spawning. {@code World#spawnParticle} and the whole {@code org.bukkit.Particle}
 * enum were added in Minecraft 1.9, so a direct bytecode reference throws {@link NoClassDefFoundError} /
 * {@link NoSuchMethodError} at class-load on 1.8.8. Everything here is resolved reflectively; when the
 * API is unavailable the (purely cosmetic) particle is silently skipped.
 */
public final class ParticleCompat {

    private static final Class<?> PARTICLE_CLASS = resolveClass("org.bukkit.Particle");
    private static final Class<?> DUST_OPTIONS_CLASS = resolveClass("org.bukkit.Particle$DustOptions");

    private ParticleCompat() {}

    private static Class<?> resolveClass(@Nonnull String name) {
        try {
            return Class.forName(name);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Resolves a {@code Particle} enum constant by name, trying each candidate in order (particle names
     * were reshuffled across versions). Returns {@code null} when none exist.
     */
    @Nullable
    private static Object particle(@Nonnull String... candidates) {
        if (PARTICLE_CLASS == null) {
            return null;
        }
        for (String candidate : candidates) {
            try {
                return Enum.valueOf(PARTICLE_CLASS.asSubclass(Enum.class), candidate);
            } catch (Throwable ignored) {
                // try the next candidate
            }
        }
        return null;
    }

    /**
     * Spawns a coloured redstone "dust" particle. No-op on servers without the particle API.
     */
    public static void spawnDust(@Nonnull Location location, @Nonnull Color color, float size) {
        if (PARTICLE_CLASS == null || DUST_OPTIONS_CLASS == null || location.getWorld() == null) {
            return;
        }
        Object particle = particle("DUST", "REDSTONE");
        if (particle == null) {
            return;
        }
        try {
            Constructor<?> constructor = DUST_OPTIONS_CLASS.getConstructor(Color.class, float.class);
            Object dustOptions = constructor.newInstance(color, size);
            Method spawn = World.class.getMethod(
                "spawnParticle", PARTICLE_CLASS, Location.class,
                int.class, double.class, double.class, double.class, Object.class
            );
            spawn.invoke(location.getWorld(), particle, location, 1, 0.2, 0.2, 0.2, dustOptions);
        } catch (Throwable ignored) {
            // particle support missing - skip cosmetic effect
        }
    }

    /**
     * Spawns a directional redstone "dust" particle (count 0 uses the offset as the dust velocity).
     */
    public static void spawnDirectionalDust(@Nonnull Location location, @Nonnull Color color, float size,
                                            double offsetX, double offsetY, double offsetZ) {
        if (PARTICLE_CLASS == null || DUST_OPTIONS_CLASS == null || location.getWorld() == null) {
            return;
        }
        Object particle = particle("DUST", "REDSTONE");
        if (particle == null) {
            return;
        }
        try {
            Constructor<?> constructor = DUST_OPTIONS_CLASS.getConstructor(Color.class, float.class);
            Object dustOptions = constructor.newInstance(color, size);
            Method spawn = World.class.getMethod(
                "spawnParticle", PARTICLE_CLASS, Location.class,
                int.class, double.class, double.class, double.class, Object.class
            );
            spawn.invoke(location.getWorld(), particle, location, 0, offsetX, offsetY, offsetZ, dustOptions);
        } catch (Throwable ignored) {
            // particle support missing - skip cosmetic effect
        }
    }

    /**
     * Spawns a named particle with a count and per-axis offset. No-op on servers without the API or
     * when none of the candidate names resolve on the running version.
     */
    public static void spawn(@Nonnull Location location, int count, double offsetX, double offsetY,
                             double offsetZ, @Nonnull String... particleNames) {
        if (PARTICLE_CLASS == null || location.getWorld() == null) {
            return;
        }
        Object particle = particle(particleNames);
        if (particle == null) {
            return;
        }
        try {
            Method spawn = World.class.getMethod(
                "spawnParticle", PARTICLE_CLASS, Location.class,
                int.class, double.class, double.class, double.class
            );
            spawn.invoke(location.getWorld(), particle, location, count, offsetX, offsetY, offsetZ);
        } catch (Throwable ignored) {
            // particle support missing - skip cosmetic effect
        }
    }
}
