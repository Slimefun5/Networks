package io.github.sefiraat.networks.utils;

import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;

import io.github.sefiraat.networks.Networks;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Resolves this addon's chat text for one viewer, from {@code languages/<lang>/messages.yml}.
 *
 * @implNote The {@link Theme} prefix deliberately stays at the call site rather than moving into the
 *           translated value: a translator never edits a colour code they cannot see the effect of,
 *           and {@link Theme#CLICK_INFO} is a hex colour that has no {@code &} equivalent on the
 *           legacy versions this jar still runs on.
 */
public final class Messages {

    private Messages() {}

    @Nonnull
    public static String get(@Nullable Player player, @Nonnull String key, @Nonnull String english) {
        return Slimefun.getAddonMessageService().getMessage(player, Networks.getInstance(), key, english);
    }

    public static void send(@Nonnull Player player, @Nonnull Theme theme, @Nonnull String key, @Nonnull String english) {
        player.sendMessage(theme + get(player, key, english));
    }

    public static void send(@Nonnull Player player, @Nonnull Theme theme, @Nonnull String key, @Nonnull String english, @Nonnull UnaryOperator<String> placeholders) {
        player.sendMessage(theme + placeholders.apply(get(player, key, english)));
    }
}
