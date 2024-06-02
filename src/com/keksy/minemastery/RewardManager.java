package com.keksy.minemastery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class RewardManager {

    private final ConfigManager configManager;
    private final StatsManager statsManager;
    private final String messagePrefix;

    public RewardManager(ConfigManager configManager, StatsManager statsManager, String messagePrefix) {
        this.configManager = configManager;
        this.statsManager = statsManager;
        this.messagePrefix = messagePrefix;
    }

    public void checkLevelRewards(Player player, String type, int totalPoints) {
        Map<Integer, Integer> levels = configManager.getLevelRequirements(type);
        Map<Integer, List<String>> typeRewards = configManager.getRewards(type);

        for (Map.Entry<Integer, Integer> entry : levels.entrySet()) {
            int level = entry.getKey();
            int requiredPoints = entry.getValue();

            if (totalPoints >= requiredPoints && totalPoints < requiredPoints + configManager.getBlocksPerPoint(type)) {
                List<String> rewardCommands = typeRewards.get(level);
                if (rewardCommands != null) {
                    for (String command : rewardCommands) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                    }
                    player.sendMessage(messagePrefix + ChatColor.GREEN + "Félicitations ! Vous avez atteint le niveau " + level + " pour la maîtrise " + type + " !");
                }
            }
        }
    }

    public void addMasteryPoints(Player player, String type, int points, FileConfiguration stats) {
        String playerUUID = player.getUniqueId().toString();
        int currentPoints = stats.getInt(playerUUID + ".points", 0);
        int newPoints = currentPoints + points;
        stats.set(playerUUID + ".points", newPoints);
        statsManager.saveStats();
        player.sendMessage(messagePrefix + ChatColor.LIGHT_PURPLE + "Vous avez gagné " + ChatColor.DARK_PURPLE + points + " points de maîtrise " + ChatColor.LIGHT_PURPLE + "!");

        checkLevelRewards(player, type, newPoints);

        // Give emeralds for every 1000 blocks broken
        giveEmeraldsForBlocks(player, points);
    }

    private void giveEmeraldsForBlocks(Player player, int points) {
        int emeraldsToGive = points; // 1 emerald per 1000 blocks
        ItemStack emeralds = new ItemStack(Material.EMERALD, emeraldsToGive);
        player.getInventory().addItem(emeralds);
        player.sendMessage(messagePrefix + ChatColor.GREEN + "Vous avez reçu " + emeraldsToGive + " émeraude(s) pour avoir cassé " + (emeraldsToGive * 1000) + " blocs !");
    }
}
