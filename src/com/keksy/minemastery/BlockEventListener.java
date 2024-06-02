package com.keksy.minemastery;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockEventListener implements Listener {
    private final StatsManager statsManager;
    private final String messagePrefix;

    public BlockEventListener(StatsManager statsManager, String messagePrefix) {
        this.statsManager = statsManager;
        this.messagePrefix = messagePrefix;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (statsManager.isProcessingEvent(player) || statsManager.isExplodeEventInProgress()) return;
        statsManager.setProcessingEvent(player, true);
        statsManager.incrementBlockCount(player, "gem", 1); // Assuming "gem" mastery type, adjust accordingly
        statsManager.setProcessingEvent(player, false);
    }
}
