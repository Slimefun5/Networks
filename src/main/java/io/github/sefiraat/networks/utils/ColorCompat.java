package io.github.sefiraat.networks.utils;

import net.md_5.bungee.api.ChatColor;

import java.lang.reflect.Method;

/**
 * Version-safe replacement for {@link ChatColor#of(String)} (added in Minecraft 1.16, which exposed
 * hex colors through BungeeCord's {@code ChatColor}).
 * <p>
 * On 1.16+ the real {@code ChatColor.of(String)} is invoked reflectively so no direct bytecode
 * reference exists. On older servers (1.8&ndash;1.15) hex colors are unavailable, so the nearest
 * legacy {@link ChatColor} is returned instead - keeping themed names/lore coloured rather than
 * crashing with {@link NoSuchMethodError} at class-init.
 */
public final class ColorCompat {

    private static final Method OF_METHOD = resolveOfMethod();

    private ColorCompat() {}

    private static Method resolveOfMethod() {
        try {
            return ChatColor.class.getMethod("of", String.class);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Resolves a hex colour string (e.g. {@code "#21588f"}) to a {@link ChatColor}, using the real
     * {@code ChatColor.of} on 1.16+ and the nearest legacy colour on older versions.
     *
     * @param hex The hex colour string
     * @return A non-null {@link ChatColor}
     */
    public static ChatColor of(String hex) {
        if (OF_METHOD != null) {
            try {
                return (ChatColor) OF_METHOD.invoke(null, hex);
            } catch (Throwable ignored) {
                // fall through to the legacy approximation
            }
        }

        return nearestLegacy(hex);
    }

    private static ChatColor nearestLegacy(String hex) {
        int rgb = parseHex(hex);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        ChatColor closest = ChatColor.WHITE;
        double bestDistance = Double.MAX_VALUE;

        for (LegacyColor legacy : LegacyColor.values()) {
            double distance = legacy.distanceTo(red, green, blue);

            if (distance < bestDistance) {
                bestDistance = distance;
                closest = legacy.chatColor;
            }
        }

        return closest;
    }

    private static int parseHex(String hex) {
        try {
            String cleaned = hex.startsWith("#") ? hex.substring(1) : hex;
            return Integer.parseInt(cleaned, 16);
        } catch (Throwable ignored) {
            return 0xFFFFFF;
        }
    }

    /** The 16 legacy {@link ChatColor} colours mapped to their canonical RGB values. */
    private enum LegacyColor {
        BLACK(ChatColor.BLACK, 0, 0, 0),
        DARK_BLUE(ChatColor.DARK_BLUE, 0, 0, 170),
        DARK_GREEN(ChatColor.DARK_GREEN, 0, 170, 0),
        DARK_AQUA(ChatColor.DARK_AQUA, 0, 170, 170),
        DARK_RED(ChatColor.DARK_RED, 170, 0, 0),
        DARK_PURPLE(ChatColor.DARK_PURPLE, 170, 0, 170),
        GOLD(ChatColor.GOLD, 255, 170, 0),
        GRAY(ChatColor.GRAY, 170, 170, 170),
        DARK_GRAY(ChatColor.DARK_GRAY, 85, 85, 85),
        BLUE(ChatColor.BLUE, 85, 85, 255),
        GREEN(ChatColor.GREEN, 85, 255, 85),
        AQUA(ChatColor.AQUA, 85, 255, 255),
        RED(ChatColor.RED, 255, 85, 85),
        LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, 255, 85, 255),
        YELLOW(ChatColor.YELLOW, 255, 255, 85),
        WHITE(ChatColor.WHITE, 255, 255, 255);

        private final ChatColor chatColor;
        private final int red;
        private final int green;
        private final int blue;

        LegacyColor(ChatColor chatColor, int red, int green, int blue) {
            this.chatColor = chatColor;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        private double distanceTo(int otherRed, int otherGreen, int otherBlue) {
            int deltaRed = red - otherRed;
            int deltaGreen = green - otherGreen;
            int deltaBlue = blue - otherBlue;
            return Math.sqrt((double) deltaRed * deltaRed + (double) deltaGreen * deltaGreen + (double) deltaBlue * deltaBlue);
        }
    }
}
