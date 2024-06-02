package com.keksy.minemastery.listeners;

import com.keksy.minemastery.StatsManager;
import com.keksy.minemastery.MineMastery;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListenerLowest implements Listener {

    private final StatsManager statsManager;

    public BreakListenerLowest(MineMastery plugin, StatsManager statsManager) {
        this.statsManager = statsManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        statsManager.handleBlockBreak(event.getPlayer(), event.getBlock());
    }
}
