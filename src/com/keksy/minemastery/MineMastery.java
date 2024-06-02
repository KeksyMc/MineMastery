package com.keksy.minemastery;

import com.keksy.minemastery.listeners.BreakListenerHigh;
import com.keksy.minemastery.listeners.BreakListenerHighest;
import com.keksy.minemastery.listeners.BreakListenerLow;
import com.keksy.minemastery.listeners.BreakListenerLowest;
import com.keksy.minemastery.listeners.BreakListenerMonitor;
import com.keksy.minemastery.listeners.BreakListenerNormal;
import com.keksy.minemastery.listeners.TEListener;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

public class MineMastery extends JavaPlugin {

    private ConfigManager configManager;
    private StatsManager statsManager;
    private RewardManager rewardManager;
    private final String messagePrefix = ChatColor.translateAlternateColorCodes('&', "&5&l[&d&lMineMastery&5&l] ");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        statsManager = new StatsManager(this, configManager, rewardManager);
        rewardManager = new RewardManager(configManager, statsManager, messagePrefix);
        statsManager.setRewardManager(rewardManager);

        registerEventListeners();

        PluginCommand command = this.getCommand("mm");
        if (command != null) {
            command.setExecutor(new CommandHandler(statsManager, configManager, messagePrefix));
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHook(statsManager).register();
        }
    }

    @Override
    public void onDisable() {
        statsManager.saveStats();
    }

    private void registerEventListeners() {
        String priority = getConfig().getString("listenerPriority", "normal").toLowerCase();
        switch (priority) {
            case "lowest":
                new BreakListenerLowest(this, statsManager);
                break;
            case "low":
                new BreakListenerLow(this, statsManager);
                break;
            case "normal":
                new BreakListenerNormal(this, statsManager);
                break;
            case "high":
                new BreakListenerHigh(this, statsManager);
                break;
            case "highest":
                new BreakListenerHighest(this, statsManager);
                break;
            case "monitor":
                new BreakListenerMonitor(this, statsManager);
                break;
            default:
                new BreakListenerNormal(this, statsManager);
                break;
        }

        new TEListener(this, statsManager);
    }
}
