package com.keksy.minemastery.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.keksy.minemastery.models.PlayerMastery;

public class MasteryManager {
    private final Map<UUID, PlayerMastery> playerMasteries = new HashMap<>();

    public void addMasteryPoints(Player player, int points) {
        UUID playerId = player.getUniqueId();
        PlayerMastery mastery = playerMasteries.getOrDefault(playerId, new PlayerMastery(playerId));
        mastery.addMasteryPoints(points);
        playerMasteries.put(playerId, mastery);

        // Logique pour vérifier et attribuer des compétences/récompenses
    }
}
