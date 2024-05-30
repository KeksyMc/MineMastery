package com.keksy.minemastery;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class MineMasteryConfig {

	private MineMastery plugin;

	public MineMasteryConfig(MineMastery i) {
		plugin = i;

	}

	@SuppressWarnings("deprecation")
	protected void loadConfigurationFile() {
        FileConfiguration c = plugin.getConfig();
        c.options().header("MineMastery version " + plugin.getDescription().getVersion()
                + " Main configuration file.");

        c.addDefault("mastery.gem.points_per_block", 1);

        for (int i = 0; i <= 10; i++) {
            int pointsNeeded = i == 0 ? 0 : (int) (100 * Math.pow(10, (i - 1) / 9.0));
            c.addDefault("mastery.gem.level." + i, pointsNeeded);
            c.addDefault("mastery.gem.rewards." + i, Arrays.asList(
                    "give {player} dpick " + i,
                    "broadcast chat {player} a gagné " + i
            ));
        }

        c.addDefault("mastery.metal.points_per_block", 1);

        for (int i = 0; i <= 10; i++) {
            int pointsNeeded = i == 0 ? 0 : (int) (100 * Math.pow(10, (i - 1) / 9.0));
            c.addDefault("mastery.metal.level." + i, pointsNeeded);
            c.addDefault("mastery.metal.rewards." + i, Arrays.asList(
                    "give {player} dpick " + (i + 10),
                    "broadcast chat {player} a gagné " + (i + 10)
            ));
        }
		
		c.options().copyDefaults(true);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
	
    public int getGemPointsPerBlock() {
        return plugin.getConfig().getInt("mastery.gem.points_per_block");
    }

    public int getGemLevel(int level) {
        return plugin.getConfig().getInt("mastery.gem.level." + level);
    }

    public List<String> getGemRewards(int level) {
        return plugin.getConfig().getStringList("mastery.gem.rewards." + level);
    }

    public int getMetalPointsPerBlock() {
        return plugin.getConfig().getInt("mastery.metal.points_per_block");
    }

    public int getMetalLevel(int level) {
        return plugin.getConfig().getInt("mastery.metal.level." + level);
    }

    public List<String> getMetalRewards(int level) {
        return plugin.getConfig().getStringList("mastery.metal.rewards." + level);
    }
	
}