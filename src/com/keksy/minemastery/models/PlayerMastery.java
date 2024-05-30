package com.keksy.minemastery.models;

import java.util.UUID;

public class PlayerMastery {
    private final UUID playerUUID;
    private int masteryPoints;

    public PlayerMastery(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.masteryPoints = 0;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getMasteryPoints() {
        return masteryPoints;
    }

    public void setMasteryPoints(int masteryPoints) {
        this.masteryPoints = masteryPoints;
    }

    public void addMasteryPoints(int points) {
        this.masteryPoints += points;
    }

    public void removeMasteryPoints(int points) {
        this.masteryPoints = Math.max(0, this.masteryPoints - points);
    }
}
