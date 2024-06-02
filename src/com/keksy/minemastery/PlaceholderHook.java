package com.keksy.minemastery;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {

    private final StatsManager statsManager;

    public PlaceholderHook(StatsManager statsManager) {
        this.statsManager = statsManager;
    }

    @Override
    public @Nonnull String getIdentifier() {
        return "minemastery";
    }

    @Override
    public @Nonnull String getAuthor() {
        return "keksy";
    }

    @Override
    public @Nonnull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required to let PlaceholderAPI know that this expansion should not be unregistered
                     // when the server reloads or restarts
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @Nonnull String identifier) {
        if (player == null) {
            return "";
        }

        // %minemastery_points%
        if (identifier.equals("points")) {
            String playerUUID = player.getUniqueId().toString();
            int points = statsManager.getStats().getInt(playerUUID + ".points", 0);
            return String.valueOf(points);
        }

        // We return null if an invalid placeholder was provided
        return null;
    }
}
