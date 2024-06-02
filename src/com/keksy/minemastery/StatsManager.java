package com.keksy.minemastery;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatsManager {

    private final MineMastery plugin;
    private final ConfigManager configManager;
    private RewardManager rewardManager;
    private final Map<Player, Integer> blockCount;
    private final Map<Player, Boolean> processingEvent;
    private boolean explodeEventInProgress = false;
    private FileConfiguration stats;

    public StatsManager(MineMastery plugin, ConfigManager configManager, RewardManager rewardManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.rewardManager = rewardManager;
        this.blockCount = new HashMap<>();
        this.processingEvent = new HashMap<>();
        createStatsFile();
    }

    private void createStatsFile() {
        File statsFile = new File(plugin.getDataFolder(), "stats.yml");
        if (!statsFile.exists()) {
            try {
                statsFile.getParentFile().mkdirs();
                statsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stats = YamlConfiguration.loadConfiguration(statsFile);
    }

    public FileConfiguration getStats() {
        return stats;
    }

    public void saveStats() {
        try {
            stats.save(new File(plugin.getDataFolder(), "stats.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayerToStats(Player player) {
        String playerUUID = player.getUniqueId().toString();
        if (!stats.contains(playerUUID)) {
            stats.set(playerUUID + ".name", player.getName());
            stats.set(playerUUID + ".points", 0);
            saveStats();
        }
    }

    public boolean isExplodeEventInProgress() {
        return explodeEventInProgress;
    }

    public void setExplodeEventInProgress(boolean inProgress) {
        explodeEventInProgress = inProgress;
    }

    public boolean isProcessingEvent(Player player) {
        return processingEvent.getOrDefault(player, false);
    }

    public void setProcessingEvent(Player player, boolean inProgress) {
        processingEvent.put(player, inProgress);
    }

    public void handleBlockBreak(Player player, Block block) {
        if (isProcessingEvent(player) || !isAllowedBlock(block.getType())) {
            return;
        }
        setProcessingEvent(player, true);
        incrementBlockCount(player, "gem", 1);
        setProcessingEvent(player, false);
    }

    private boolean isAllowedBlock(Material material) {
        // Add your logic here to determine if the block type is allowed
        // For now, let's assume all blocks are allowed
        return true;
    }

    public void incrementBlockCount(Player player, String type, int increment) {
        int currentCount = blockCount.getOrDefault(player, 0);
        int newCount = currentCount + increment;

        int blocksPerPoint = configManager.getBlocksPerPoint(type);
        int pointsGained = newCount / blocksPerPoint;
        int remainingBlocks = newCount % blocksPerPoint;

        if (pointsGained > 0) {
            rewardManager.addMasteryPoints(player, type, pointsGained, stats);
        }

        blockCount.put(player, remainingBlocks);
    }

    public void addMasteryPointsByUUID(String uuid, int points) {
        int currentPoints = stats.getInt(uuid + ".points", 0);
        stats.set(uuid + ".points", currentPoints + points);
        saveStats();
    }

    public void setRewardManager(RewardManager rewardManager) {
        this.rewardManager = rewardManager;
    }
}
