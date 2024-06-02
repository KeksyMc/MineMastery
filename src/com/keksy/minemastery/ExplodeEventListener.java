package com.keksy.minemastery;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ExplodeEventListener implements Listener {
    private final StatsManager statsManager;
    private final String messagePrefix;

    public ExplodeEventListener(StatsManager statsManager, String messagePrefix) {
        this.statsManager = statsManager;
        this.messagePrefix = messagePrefix;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTEBlockExplode(TEBlockExplodeEvent event) {
        Player player = event.getPlayer();
        if (statsManager.isProcessingEvent(player)) return;
        statsManager.setExplodeEventInProgress(true);
        statsManager.setProcessingEvent(player, true);
        int blocksBroken = event.blockList().size();
        statsManager.incrementBlockCount(player, "gem", blocksBroken); // Assuming "gem" mastery type, adjust accordingly
        statsManager.setProcessingEvent(player, false);
        statsManager.setExplodeEventInProgress(false);
    }
}
