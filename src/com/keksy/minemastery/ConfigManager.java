package com.keksy.minemastery;

import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigManager {

    private final MineMastery plugin;
    private final Map<String, Integer> blocksPerPoint;
    private final Map<String, Map<Integer, Integer>> levelRequirements;
    private final Map<String, Map<Integer, List<String>>> rewards;
    private FileConfiguration config;

    public ConfigManager(MineMastery plugin) {
        this.plugin = plugin;
        this.blocksPerPoint = new HashMap<>();
        this.levelRequirements = new HashMap<>();
        this.rewards = new HashMap<>();
        loadMasteryConfig();
    }

    public void loadMasteryConfig() {
        FileConfiguration config = plugin.getConfig();

        if (config.isConfigurationSection("mastery")) {
            Set<String> masteryTypes = config.getConfigurationSection("mastery").getKeys(false);
            for (String type : masteryTypes) {
                getBlocksPerPoint().put(type, config.getInt("mastery." + type + ".points-per-block"));

                Map<Integer, Integer> levels = new HashMap<>();
                Map<Integer, List<String>> levelRewards = new HashMap<>();

                if (config.isConfigurationSection("mastery." + type + ".levels")) {
                    Set<String> levelKeys = config.getConfigurationSection("mastery." + type + ".levels").getKeys(false);
                    for (String levelKey : levelKeys) {
                        int level = Integer.parseInt(levelKey);
                        levels.put(level, config.getInt("mastery." + type + ".levels." + levelKey));
                    }
                }
                getLevelRequirements().put(type, levels);

                if (config.isConfigurationSection("mastery." + type + ".rewards")) {
                    Set<String> rewardKeys = config.getConfigurationSection("mastery." + type + ".rewards").getKeys(false);
                    for (String rewardKey : rewardKeys) {
                        int level = Integer.parseInt(rewardKey);
                        levelRewards.put(level, config.getStringList("mastery." + type + ".rewards." + rewardKey));
                    }
                }
                getRewards().put(type, levelRewards);
            }
        } else {
            plugin.getLogger().warning("No mastery configuration found!");
        }
    }

    public int getBlocksPerPoint(String type) {
        return blocksPerPoint.getOrDefault(type, 1000);
    }

    public Map<Integer, Integer> getLevelRequirements(String type) {
        return levelRequirements.getOrDefault(type, new HashMap<>());
    }

    public Map<Integer, List<String>> getRewards(String type) {
        return rewards.getOrDefault(type, new HashMap<>());
    }

    public String getMessagePrefix() {
        return ChatColor.translateAlternateColorCodes('&', "&5&l[&d&lMineMastery&5&l] ");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        loadMasteryConfig();
    }

	public Map<String, Integer> getBlocksPerPoint() {
		return blocksPerPoint;
	}

	public Map<String, Map<Integer, Integer>> getLevelRequirements() {
		return levelRequirements;
	}

	public Map<String, Map<Integer, List<String>>> getRewards() {
		return rewards;
	}

}
