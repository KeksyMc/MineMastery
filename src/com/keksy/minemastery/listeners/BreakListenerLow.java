package com.keksy.minemastery.listeners;

import com.keksy.minemastery.StatsManager;
import com.keksy.minemastery.MineMastery;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListenerLow implements Listener {

    private final StatsManager statsManager;

    public BreakListenerLow(MineMastery plugin, StatsManager statsManager) {
        this.statsManager = statsManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        statsManager.handleBlockBreak(event.getPlayer(), event.getBlock());
    }
}
