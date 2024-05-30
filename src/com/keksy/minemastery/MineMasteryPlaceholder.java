package com.keksy.minemastery;

import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class MineMasteryPlaceholder extends PlaceholderExpansion {
	
    private final MineMastery plugin;

    public MineMasteryPlaceholder(MineMastery plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "minemastery";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        // Vérifier l'identifiant du placeholder
        if (identifier.equals("points")) {
            // Récupérer les points de maîtrise du joueur
            int points = plugin.getPlayerMastery(player.getUniqueId()).getMasteryPoints();
            return String.valueOf(points);
        }

        return null; // Placeholder non trouvé
    }
}
